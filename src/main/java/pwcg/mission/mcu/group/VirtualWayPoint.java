package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuCounter;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;

public final class VirtualWayPoint implements IVirtualWaypoint 
{       
    private ActivateContainer activateContainer;
    private int index = IndexGenerator.getInstance().getNextIndex();
    private VirtualWayPointCoordinate vwpCoordinate;

    private SelfDeactivatingCheckZone checkZone;
    private McuTimer activateTriggeredTimer = new McuTimer();
    private McuTimer masterActivateTimer = new McuTimer();
    private McuDeactivate stopNextActivate = new McuDeactivate();    private McuTimer activateTimer = new McuTimer();
    private McuTimer activateTimedOutTimer = new McuTimer();
    private McuTimer initiateNextActivateWaypointTimer = new McuTimer();
    private McuTimer killVwpTimer = new McuTimer();

    public static int VWP_TRIGGGER_DISTANCE = 20000;
    
    public VirtualWayPoint()
    {
        index = IndexGenerator.getInstance().getNextIndex();
    }

    @Override
    public void initialize(
                    IFlight flight,
                    VirtualWayPointCoordinate activateCoordinate,
                    Coalition coalition) throws PWCGException 
    {
        this.vwpCoordinate = activateCoordinate; 
        this.checkZone = new SelfDeactivatingCheckZone("Activate Check Zone", vwpCoordinate.getPosition().copy(), VWP_TRIGGGER_DISTANCE);
        
        buildMcus(activateCoordinate);
        setTargetAssociations(activateCoordinate);
        generateActivateContainer(flight);
    }

    private void setTargetAssociations(VirtualWayPointCoordinate activateCoordinate)
    {
        setTargetAssociationsActivateActivateActivate();
        setTargetAssociationsTriggered();
        setTargetAssociationsTimedOut();
        setTargetAssociationsForPlaneLimitReached();
    }
    
    private void setTargetAssociationsActivateActivateActivate()
    {
        activateTimer.setTarget(checkZone.getActivateEntryPoint());
    }
    
    private void setTargetAssociationsTriggered()
    {
        // Set up activate and deactivate
        // virtualWPTimer activates the self deleting CZ
        // deactivateActivateTimer deactivates the self deleting CZ
        checkZone.setCheckZoneTarget(activateTriggeredTimer.getIndex());
        activateTimedOutTimer.setTarget(checkZone.getDeactivateEntryPoint());
        
        // Stop the next Activate from triggering
        activateTriggeredTimer.setTarget(stopNextActivate.getIndex());
        stopNextActivate.setTarget(activateTimedOutTimer.getIndex());
        
        // Trigger the Spawn
        activateTriggeredTimer.setTarget(masterActivateTimer.getIndex());
    }

    private void setTargetAssociationsTimedOut()
    {
        activateTimer.setTarget(activateTimedOutTimer.getIndex());
        activateTimedOutTimer.setTarget(initiateNextActivateWaypointTimer.getIndex());
    }

    private void setTargetAssociationsForPlaneLimitReached()
    {
        killVwpTimer.setTarget(stopNextActivate.getIndex());
        killVwpTimer.setTarget(checkZone.getDeactivateEntryPoint());
    }

    private void buildMcus(VirtualWayPointCoordinate activateCoordinate)
    {
        activateTimer.setPosition(activateCoordinate.getPosition().copy());
        activateTimer.setName("Activate Timer");
        activateTimer.setDesc("Activate Timer");
        activateTimer.setTimer(activateCoordinate.getWaypointWaitTimeSeconds());

        masterActivateTimer.setPosition(activateCoordinate.getPosition().copy());
        masterActivateTimer.setName("Activate Spawn Timer");
        masterActivateTimer.setDesc("Activate Master Spawn Timer");
        masterActivateTimer.setTimer(1);

        activateTimedOutTimer.setPosition(activateCoordinate.getPosition().copy());
        activateTimedOutTimer.setName("Activate CZ Deactivate Timer");
        activateTimedOutTimer.setDesc("Activate CZ Deactivate Timer");
        activateTimedOutTimer.setTimer(activateCoordinate.getWaypointWaitTimeSeconds());

        activateTriggeredTimer.setPosition(activateCoordinate.getPosition().copy());
        activateTriggeredTimer.setName("Activate Trigger Timer");
        activateTriggeredTimer.setDesc("Activate Stop Deactivate Timer");
        activateTriggeredTimer.setTimer(0);

        initiateNextActivateWaypointTimer.setPosition(activateCoordinate.getPosition().copy());
        initiateNextActivateWaypointTimer.setName("Next Activate Timer");
        initiateNextActivateWaypointTimer.setDesc("Next Activate Timer");

        killVwpTimer.setPosition(activateCoordinate.getPosition().copy());
        killVwpTimer.setName("Kill Activate Timer");
        killVwpTimer.setDesc("Kill Activate Timer");

        stopNextActivate.setPosition(activateCoordinate.getPosition().copy());
        stopNextActivate.setName("Activate Stop Next");
        stopNextActivate.setDesc("Activate Stop Deactivate");
    }

    private void generateActivateContainer(IFlight flight) throws PWCGException
    {
        activateContainer = new ActivateContainer(vwpCoordinate);
        activateContainer.create(flight, vwpCoordinate.getPosition());
        masterActivateTimer.setTarget(activateContainer.getEntryPoint());
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            writer.write("  Name = \"Virtual WP\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Virtual WP\";");
            writer.newLine();

            activateTimer.write(writer);
            masterActivateTimer.write(writer);
            
            checkZone.write(writer);
            activateTimedOutTimer.write(writer);
            initiateNextActivateWaypointTimer.write(writer);
            activateTriggeredTimer.write(writer);
            stopNextActivate.write(writer);
            killVwpTimer.write(writer);
            
            activateContainer.write(writer);

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
    

    @Override
    public void addAdditionalTime(int additionalTime)
    {
        int activateTimerTime = activateTimer.getTimer();
        activateTimerTime += additionalTime;
        activateTimer.setTimer(activateTimerTime);
    }


    @Override
    public McuTimer getEntryPoint()
    {
        return activateTimer;
    }

    @Override
    public void linkToNextVirtualWaypoint(IVirtualWaypoint nextActivate)
    {
        initiateNextActivateWaypointTimer.setTarget(nextActivate.getEntryPoint().getIndex());
    }

    @Override
    public McuTimer getKillVwpTimer()
    {
        return killVwpTimer;
    }
    
    @Override
    public void registerPlaneCounter(McuCounter counter)
    {
        activateContainer.registerPlaneCounter(counter.getIndex());
    }
    
    @Override
    public void setVwpTriggerObject(int triggerObject)
    {
        checkZone.setCheckZoneTriggerObject(triggerObject);
    }

    @Override
    public SelfDeactivatingCheckZone getCheckZone()
    {
        return checkZone;
    }

    @Override
    public McuTimer getVwpTimedOutTimer()
    {
        return activateTimedOutTimer;
    }

    @Override
    public McuTimer getVwpTimer()
    {
        return activateTimer;
    }

    @Override
    public McuTimer getMasterVwpTimer()
    {
        return masterActivateTimer;
    }

    @Override
    public McuTimer getInitiateNextVwpTimer()
    {
        return initiateNextActivateWaypointTimer;
    }

    @Override
    public McuTimer getVwpTriggeredTimer()
    {
        return activateTriggeredTimer;
    }

    @Override
    public McuDeactivate getStopNextVwp()
    {
        return stopNextActivate;
    }

    @Override
    public ActivateContainer getActivateContainer()
    {
        return activateContainer;
    }    
    
}
