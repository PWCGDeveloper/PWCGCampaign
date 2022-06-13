package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuTimer;

public class FlareSequence
{
    private static final int FLARES_IN_SEQUENCE = 5;
    private static final int FLARE_TRIGGER_DISTANCE = 1500;

    private MissionBeginSelfDeactivatingCheckZone missionBeginUnit;
    private List<Flare> flares = new ArrayList<>();
    private McuTimer flareMasterTimer = new McuTimer();

    public FlareSequence()
    {
    }

    public void createFlareSequence(IFlight triggeringFlight, Coordinate position, int color, int flareVehicleId) throws PWCGException 
    {        
        buildMissionBeginUnit(position);
        createflareMasterTimer(position);
        buildFlareSequence(position, color, flareVehicleId);
        createTargetAssociations();
        createObjectAssociations(triggeringFlight);

    }

    private void buildMissionBeginUnit(Coordinate position)
    {
        missionBeginUnit = new MissionBeginSelfDeactivatingCheckZone("Flare Sequence Check Zone", position, FLARE_TRIGGER_DISTANCE);
    }

    public void createflareMasterTimer(Coordinate position) throws PWCGException 
    {        
        flareMasterTimer.setName("flareMasterTimer");
        flareMasterTimer.setTime(1);
        flareMasterTimer.setPosition(position.copy());
    }

    private void buildFlareSequence(Coordinate position, int color, int flareVehicleId) throws PWCGException
    {
        for (int i = 0; i < FLARES_IN_SEQUENCE; ++i)
        {
            Flare flare = new Flare();
            flare.createFlare(position, color, flareVehicleId);
            flares.add(flare);
        }
    }
    
    private void createTargetAssociations()
    {
        missionBeginUnit.linkCheckZoneTarget(flareMasterTimer.getIndex());
        
        Flare previousFlare = null;
        for (Flare flare : flares)
        {
            if (previousFlare == null)
            {
                flareMasterTimer.setTimerTarget(flare.getEntryPoint());
            }
            else
            {
                previousFlare.setNextTarget(flare.getEntryPoint());
            }
            previousFlare = flare;
        }
    }
    
    private void createObjectAssociations(IFlight triggeringFlight)
    {
        for (PlaneMcu triggeringPlane : triggeringFlight.getFlightPlanes().getPlanes())
        {
            missionBeginUnit.setCheckZoneTriggerObject(triggeringPlane.getLinkTrId());
        }
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnit.write(writer);
        flareMasterTimer.write(writer);
        for (Flare flare : flares)
        {
            flare.write(writer);
        }
    }
}
