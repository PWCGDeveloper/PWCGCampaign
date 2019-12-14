package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.McuFlare;
import pwcg.mission.mcu.McuTimer;

public class FlareSequence
{
    public static final int FLARES_IN_SEQUENCE = 3;
    public static final int FLARE_TRIGGER_DISTANCE = 200;

    private MissionBeginSelfDeactivatingCheckZone missionBeginUnit;
    protected McuTimer[] flareTimers = new McuTimer[FLARES_IN_SEQUENCE];
    protected McuFlare[] flares = new McuFlare[FLARES_IN_SEQUENCE];

    public FlareSequence()
    {
    }

    public void setFlare(Flight triggeringFlight, Coordinate position, int color, int object) throws PWCGException 
    {        
        buildFlareTimers(position, color, object);

        missionBeginUnit = new MissionBeginSelfDeactivatingCheckZone(position, FLARE_TRIGGER_DISTANCE);
        missionBeginUnit.linkCheckZoneTarget(flareTimers[0].getIndex());

        for (PlaneMCU triggeringPlane : triggeringFlight.getPlanes())
        {
            missionBeginUnit.setCheckZoneTriggerObject(triggeringPlane.getEntity().getIndex());
        }
        
        for (int i = 1; i < FLARES_IN_SEQUENCE; ++i)
        {
            flareTimers[i-1].setTarget(flareTimers[i].getIndex());
        }
    }

    private void buildFlareTimers(Coordinate position, int color, int object)
    {
        for (int i = 0; i < FLARES_IN_SEQUENCE; ++i)
        {
            McuTimer flareTimer = new McuTimer();
            flareTimer.setName("FlareTimer " + (i+1));
            flareTimer.setTimer(3);
            flareTimer.setPosition(position.copy());
            flareTimers[i] = flareTimer;
            
            McuFlare flare = new McuFlare();
            flare.setName("Flare " + (i+1));
            flare.setColor(color);
            flare.setPosition(position.copy());
            flares[i] = flare;
            
            flareTimer.setTarget(flare.getIndex());
            
            flare.setObject(object);
        }
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnit.write(writer);
        for (int i = 0; i < FLARES_IN_SEQUENCE; ++i)
        {
            flareTimers[i].write(writer);
            flares[i].write(writer);
        }
    }

    public McuTimer[] getFlareTimers()
    {
        return flareTimers;
    }

    public McuFlare[] getFlares()
    {
        return flares;
    }
}
