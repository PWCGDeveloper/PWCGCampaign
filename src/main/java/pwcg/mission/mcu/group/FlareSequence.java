package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuFlare;
import pwcg.mission.mcu.McuTimer;

public class FlareSequence
{
    public static final int FLARES_IN_SEQUENCE = 3;

    protected MissionBeginUnitCheckZone missionBeginUnit = null;
    protected McuTimer[] flareTimers = new McuTimer[FLARES_IN_SEQUENCE];
    protected McuFlare[] flares = new McuFlare[FLARES_IN_SEQUENCE];

    public FlareSequence()
    {
    }

    public void setFlare(Coalition friendlyCoalition, Coordinate position, int color, int object) throws PWCGException 
    {        
        buildFlareTimers(position, color, object);

        missionBeginUnit = new MissionBeginUnitCheckZone(position.copy(), 1000);
        missionBeginUnit.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalition(friendlyCoalition);
        
        SelfDeactivatingCheckZone checkZone = missionBeginUnit.getSelfDeactivatingCheckZone();
        checkZone.setCZTarget(flareTimers[0].getIndex());
        
        // Link from MB to each timer
        missionBeginUnit.linkToMissionBegin(flareTimers[0].getIndex());
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

    public MissionBeginUnitCheckZone getMissionBeginUnit()
    {
        return missionBeginUnit;
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
