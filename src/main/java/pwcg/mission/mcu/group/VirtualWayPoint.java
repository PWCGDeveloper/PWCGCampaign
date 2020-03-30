package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.MissionStringHandler;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.FormationGenerator;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuCounter;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;

public final class VirtualWayPoint 
{       
    private Map<Integer, VwpSpawnContainer> vwpSpawnContainers = new TreeMap<>();
    private int index = IndexGenerator.getInstance().getNextIndex();;
    private VirtualWayPointCoordinate vwpPosition;
    
    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();
    private boolean useSubtitles = false;

    private SelfDeactivatingCheckZone checkZone;
    private McuTimer vwpTriggeredTimer = new McuTimer();
    private McuTimer masterSpawnTimer = new McuTimer();
    private McuDeactivate stopNextVwp = new McuDeactivate();    private McuTimer vwpTimer = new McuTimer();
    private McuTimer vwpTimedOutTimer = new McuTimer();
    private McuTimer initiateNextVirtualWaypointTimer = new McuTimer();
    private McuTimer killVwpTimer = new McuTimer();

    
    public static int VWP_TRIGGGER_DISTANCE = 20000;
    
    public VirtualWayPoint()
    {
        index = IndexGenerator.getInstance().getNextIndex();
    }

    public void initialize(
                    IFlight flight,
                    VirtualWayPointCoordinate vwpCoordinate,
                    Coalition coalition) throws PWCGException 
    {
        this.vwpPosition = vwpCoordinate; 
        checkZone = new SelfDeactivatingCheckZone("VWP Check Zone", vwpPosition.getPosition().copy(), VWP_TRIGGGER_DISTANCE);

        buildMcus(vwpCoordinate);
        makeSubtitles(flight);
        setTargetAssociations(vwpCoordinate);
        generateSpawners(flight);
    }

    private void setTargetAssociations(VirtualWayPointCoordinate vwpCoordinate)
    {
        setTargetAssociationsVwpActivateVwp();
        setTargetAssociationsTriggered();
        setTargetAssociationsTimedOut();
        setTargetAssociationsForPlaneLimitReached();
    }
    
    private void setTargetAssociationsVwpActivateVwp()
    {
        vwpTimer.setTarget(checkZone.getActivateEntryPoint());
    }
    
    private void setTargetAssociationsTriggered()
    {
        // Set up activate and deactivate
        // virtualWPTimer activates the self deleting CZ
        // deactivateVWPTimer deactivates the self deleting CZ
        checkZone.setCheckZoneTarget(vwpTriggeredTimer.getIndex());
        vwpTimedOutTimer.setTarget(checkZone.getDeactivateEntryPoint());
        
        // Stop the next VWP from triggering
        vwpTriggeredTimer.setTarget(stopNextVwp.getIndex());
        stopNextVwp.setTarget(vwpTimedOutTimer.getIndex());
        
        // Trigger the Spawn
        vwpTriggeredTimer.setTarget(masterSpawnTimer.getIndex());
    }

    private void setTargetAssociationsTimedOut()
    {
        vwpTimer.setTarget(vwpTimedOutTimer.getIndex());
        vwpTimedOutTimer.setTarget(initiateNextVirtualWaypointTimer.getIndex());
    }

    private void setTargetAssociationsForPlaneLimitReached()
    {
        killVwpTimer.setTarget(stopNextVwp.getIndex());
        killVwpTimer.setTarget(checkZone.getDeactivateEntryPoint());
    }

    private void buildMcus(VirtualWayPointCoordinate vwpCoordinate)
    {
        vwpTimer.setPosition(vwpCoordinate.getPosition().copy());
        vwpTimer.setName("VWP Timer");
        vwpTimer.setDesc("VWP Timer");
        vwpTimer.setTimer(vwpCoordinate.getWaypointWaitTimeSeconds());

        masterSpawnTimer.setPosition(vwpCoordinate.getPosition().copy());
        masterSpawnTimer.setName("VWP Spawn Timer");
        masterSpawnTimer.setDesc("VWP Master Spawn Timer");
        masterSpawnTimer.setTimer(1);

        vwpTimedOutTimer.setPosition(vwpCoordinate.getPosition().copy());
        vwpTimedOutTimer.setName("VWP CZ Deactivate Timer");
        vwpTimedOutTimer.setDesc("VWP CZ Deactivate Timer");
        vwpTimedOutTimer.setTimer(vwpCoordinate.getWaypointWaitTimeSeconds());

        vwpTriggeredTimer.setPosition(vwpCoordinate.getPosition().copy());
        vwpTriggeredTimer.setName("VWP Trigger Timer");
        vwpTriggeredTimer.setDesc("VWP Stop Deactivate Timer");
        vwpTriggeredTimer.setTimer(0);

        stopNextVwp.setPosition(vwpCoordinate.getPosition().copy());
        stopNextVwp.setName("VWP Stop Next");
        stopNextVwp.setDesc("VWP Stop Deactivate");

        initiateNextVirtualWaypointTimer.setPosition(vwpCoordinate.getPosition().copy());
        initiateNextVirtualWaypointTimer.setName("Next VWP Timer");
        initiateNextVirtualWaypointTimer.setDesc("Next VWP Timer");

        killVwpTimer.setPosition(vwpCoordinate.getPosition().copy());
        killVwpTimer.setName("Kill VWP Timer");
        killVwpTimer.setDesc("Kill VWP Timer");
    }

    private void makeSubtitles(IFlight flight) throws PWCGException
    {     
        if (!useSubtitles)
        {
            return;
        }

        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();

        String squadronName = flight.getSquadron().determineDisplayName(flight.getCampaign().getDate());
        
        McuSubtitle checkZoneSubtitle = new McuSubtitle();
        checkZoneSubtitle.setName("checkZone Subtitle");
        checkZoneSubtitle.setText("VWP " +  squadronName +  " CZ triggered ");
        checkZoneSubtitle.setPosition(vwpPosition.getPosition().copy());
        checkZone.setCheckZoneTarget(checkZoneSubtitle.getIndex());
        subTitleList.add(checkZoneSubtitle);
        
        subtitleHandler.registerMissionText(checkZoneSubtitle.getLcText(), checkZoneSubtitle.getText());
    }

    private void generateSpawners(IFlight flight) throws PWCGException
    {
        VwpSpawnContainer lastVwpSpawnContainer = null;
        
        for (int i = 0; i < flight.getFlightPlanes().getFlightSize(); ++i)
        {
            PlaneMcu plane = flight.getFlightPlanes().getPlanes().get(i);
            double planeAltitude = vwpPosition.getPosition().getYPos() + (30 * i);
            if (planeAltitude < 800.0)
            {
                planeAltitude = 800.0;
            }

            Coordinate planeCoordinate = FormationGenerator.generatePositionForPlaneInFormation(vwpPosition.getOrientation(), vwpPosition.getPosition(), i);
            planeCoordinate.setYPos(planeAltitude);
            
            // Get the mission points (WPs plus attackarea) and select the one associated with
            // this VWP coordinate.
            List<BaseFlightMcu>  planeMissionPoints = flight.getVirtualWaypointPackage().getAllFlightPointsForPlane(plane);

            // Since every plane has a matching set of mission points, the index will be
            // identical for each plane
            BaseFlightMcu planeMissionPoint = planeMissionPoints.get(vwpPosition.getWaypointindex());
            VwpSpawnContainer vwpSpawnContainer = new VwpSpawnContainer(planeMissionPoint, vwpPosition);

            // Chain the spawns such that they happen one after the other
            if (lastVwpSpawnContainer == null)
            {
                masterSpawnTimer.setTarget(vwpSpawnContainer.getEntryPoint());
            }
            else
            {
                lastVwpSpawnContainer.linkToNextSpawnContainer(vwpSpawnContainer.getEntryPoint());
            }
            
            lastVwpSpawnContainer = vwpSpawnContainer;
            
            vwpSpawnContainer.create(plane, i, planeCoordinate);
            
            vwpSpawnContainers.put(plane.getIndex(), vwpSpawnContainer);
        }        
    }

    public void write(BufferedWriter writer) throws PWCGIOException 
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

            vwpTimer.write(writer);
            masterSpawnTimer.write(writer);
            
            checkZone.write(writer);
            vwpTimedOutTimer.write(writer);
            initiateNextVirtualWaypointTimer.write(writer);
            vwpTriggeredTimer.write(writer);
            stopNextVwp.write(writer);
            killVwpTimer.write(writer);
            
            for (VwpSpawnContainer vwpSpawnContainer : vwpSpawnContainers.values())
            {
                vwpSpawnContainer.write(writer);
            }
            
            for (int i = 0; i < subTitleList.size(); ++i)
            {
                McuSubtitle subtitle = subTitleList.get(i);
                subtitle.write(writer);
                writer.newLine();
            }

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
    

    public void addAdditionalTime(int additionalTime)
    {
        int vwpTimerTime = vwpTimer.getTimer();
        vwpTimerTime += additionalTime;
        vwpTimer.setTimer(vwpTimerTime);
    }


    public McuTimer getEntryPoint()
    {
        return vwpTimer;
    }

    public void linkToNextVirtualWaypoint(VirtualWayPoint nextVWP)
    {
        initiateNextVirtualWaypointTimer.setTarget(nextVWP.getEntryPoint().getIndex());
    }

    public McuTimer getKillVwpTimer()
    {
        return killVwpTimer;
    }
    
    public void registerPlaneCounter(McuCounter counter)
    {
        for (VwpSpawnContainer vwpSpawnContainer : vwpSpawnContainers.values())
        {
            vwpSpawnContainer.registerPlaneCounter(counter.getIndex());
        }
    }
    
    public void setVirtualWaypointTriggerObject(int triggerObject)
    {
        checkZone.setCheckZoneTriggerObject(triggerObject);
    }

    public VwpSpawnContainer getVwpSpawnContainerForPlane(int planeIndex)
    {
        return vwpSpawnContainers.get(planeIndex);
    }

    public List<McuSubtitle> getSubTitleList()
    {
        return subTitleList;
    }

    public boolean isUseSubtitles()
    {
        return useSubtitles;
    }

    public SelfDeactivatingCheckZone getCheckZone()
    {
        return checkZone;
    }

    public McuTimer getVwpTimedOutTimer()
    {
        return vwpTimedOutTimer;
    }

    public McuTimer getVwpTimer()
    {
        return vwpTimer;
    }

    public McuTimer getMasterSpawnTimer()
    {
        return masterSpawnTimer;
    }

    public McuTimer getInitiateNextVirtualWaypointTimer()
    {
        return initiateNextVirtualWaypointTimer;
    }

    public McuTimer getVwpTriggeredTimer()
    {
        return vwpTriggeredTimer;
    }

    public McuDeactivate getStopNextVwp()
    {
        return stopNextVwp;
    }    
    
}
