package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;
import pwcg.mission.mcu.group.virtual.VirtualWaypoint;



public class SelfDeactivatingCheckZone
{
	private String name = "Self Deactivating CZ";
	private String desc = "Self Deactivating CZ";
    private int index = IndexGenerator.getInstance().getNextIndex();;
	
    private McuTimer activateCZTimer = new McuTimer();
    private McuCheckZone checkZone;
    
    private McuTimer deactivateCZTimer = new McuTimer();
    private McuDeactivate deactivateCZ = new McuDeactivate();
	

    public SelfDeactivatingCheckZone (String name, Coordinate coordinate, int zoneMeters)
    {
        checkZone = new McuCheckZone(name);
        initialize(coordinate, zoneMeters);
        linkTargets();
    }

    private void initialize(Coordinate coordinate, int zoneMeters) 
    {
        checkZone.setDesc("CZ");
        checkZone.setPosition(coordinate.copy());
        checkZone.setZone(zoneMeters);
          
        deactivateCZTimer.setPosition(coordinate.copy());
        deactivateCZTimer.setName("CZ Deactivate Timer");
        deactivateCZTimer.setDesc("CZ Deactivate Timer");
        deactivateCZTimer.setTime(1);

        deactivateCZ.setPosition(coordinate.copy());
        deactivateCZ.setName("CZ Deactivate");
        deactivateCZ.setDesc("CZ Deactivate");

        activateCZTimer.setPosition(coordinate.copy());
        activateCZTimer.setName("CZ Activate Timer");
        activateCZTimer.setDesc("CZ Activate Timer");
        activateCZTimer.setTime(1);
    }

    private void linkTargets()
    {
        activateCZTimer.setTarget(checkZone.getIndex());

        // 2. If the CZ triggers, deactivate the CZ to avoid repeat triggers
        checkZone.setTarget(deactivateCZTimer.getIndex());
        deactivateCZTimer.setTarget(deactivateCZ.getIndex());
        
        // Deactivate both the CZ and the activate timer
        deactivateCZ.setTarget(checkZone.getIndex());        
        deactivateCZ.setTarget(activateCZTimer.getIndex());    
    }
    
    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"" + name + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"" +  desc + "\";");
            writer.newLine();
            writer.newLine();

            activateCZTimer.write(writer);
            checkZone.write(writer);
            deactivateCZTimer.write(writer);
            deactivateCZ.write(writer);

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
    
    public int getActivateEntryPoint()
    {
        return activateCZTimer.getIndex();
    }

    public int getDeactivateEntryPoint()
    {
        return deactivateCZTimer.getIndex();
    }

    public void setCheckZoneTarget(int targetMcuIndex)
    {
        checkZone.setTarget(targetMcuIndex);
    }

    public int getCheckZoneIndex()
    {
        return checkZone.getIndex();
    }

    public void setCheckZoneTriggerObject(int objectMcuIndex)
    {
        checkZone.setObject(objectMcuIndex);
    }

    public void setCheckZoneTriggerCoalition(Coalition coalition)
    {
        checkZone.triggerCheckZoneByCoalition(coalition);
    }

    public void setCheckZoneTriggerCoalitions(List<Coalition> coalitions)
    {
        checkZone.triggerCheckZoneByCoalitions(coalitions);
    }

    public void setCheckZoneTriggerDistance(int zoneMeters)
    {
        checkZone.setZone(zoneMeters);
    }

    public void validate() throws PWCGException
    {
        if (!McuValidator.hasTarget(activateCZTimer, checkZone.getIndex()))
        {
            throw new PWCGException("SelfDeactivatingCheckZone: activate timer not linked to activate");
        }
        
        if (!McuValidator.hasTarget(checkZone, deactivateCZTimer.getIndex()))
        {
            throw new PWCGException("SelfDeactivatingCheckZone: check zone not linked to deactivate");
        }
        
        if (McuValidator.getNumTargets(checkZone) < 2)
        {
            throw new PWCGException("SelfDeactivatingCheckZone: check zone not linked to external entity");
        }
        
        if (!McuValidator.hasTarget(deactivateCZTimer, deactivateCZ.getIndex()))
        {
            throw new PWCGException("SelfDeactivatingCheckZone: deactivate timer not linked to deactivate");
        }
        
        if (!McuValidator.hasTarget(deactivateCZ, activateCZTimer.getIndex()))
        {
            throw new PWCGException("SelfDeactivatingCheckZone: deactivate timer not linked to activate timer");
        }
    }

    public void validateTarget(int entryPoint) throws PWCGException
    {
        if (!McuValidator.hasTarget(checkZone, entryPoint))
        {
            throw new PWCGException("Unit not linked to check zone");
        }
    }

    public void triggerCheckZone(Mission mission) throws PWCGException
    {
        triggerCheckZoneByPlayer(mission);
        triggerCheckZoneByActualFlights(mission);
        triggerCheckZoneByVirtualFlights(mission);
    }

    private void triggerCheckZoneByPlayer(Mission mission) throws PWCGException
    {
        List<Integer> triggerVehicles = mission.getUnits().getPlayersInMission();
        checkZone.triggerCheckZoneByMultiplePlaneIds(triggerVehicles);        
    }
    
    private void triggerCheckZoneByActualFlights(Mission mission) throws PWCGException
    {
        List<Integer> triggerPlanes = new ArrayList<>();
        List<IFlight> flights = mission.getFlights().getAllAerialFlights();
        for (IFlight flight : flights)
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                triggerPlanes.add(plane.getLinkTrId());
            }
        }
        checkZone.triggerCheckZoneByMultiplePlaneIds(triggerPlanes);        
    }

    private void triggerCheckZoneByVirtualFlights(Mission mission) throws PWCGException
    {
        List<Integer> triggerPlanes = new ArrayList<>();
        List<IFlight> flights = mission.getFlights().getAllAerialFlights();
        for (IFlight flight : flights)
        {
            if (flight.getFlightInformation().isVirtual())
            {
                for (VirtualWaypoint virtualWaypoint : flight.getVirtualWaypointPackage().getVirtualWaypoints())
                {
                    for (PlaneMcu plane : virtualWaypoint.getVwpPlanes().getAllPlanes())
                    {
                        triggerPlanes.add(plane.getLinkTrId());
                    }
                }
            }
        }
        checkZone.triggerCheckZoneByMultiplePlaneIds(triggerPlanes);        
    }
}
