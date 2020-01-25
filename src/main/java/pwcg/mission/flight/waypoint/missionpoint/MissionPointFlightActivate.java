package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightActivate implements IMissionPointSet
{
    private IFlight flight;

    private MissionBeginUnit missionBeginUnit;
    private McuTimer activationTimer = null;
    private McuActivate activationEntity = null;
    private boolean linkToNextTarget = true;

    public MissionPointFlightActivate(IFlight flight)
    {
        this.flight = flight;
    }
    
    public void createFlightActivate() throws PWCGException, PWCGException 
    {
        createFlightMissionBegin();
        createActivation();  
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        activationTimer.setTarget(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return activationTimer.getIndex();
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints()
    {
        return new ArrayList<MissionPoint>();
    }

    @Override
    public void disableLinkToNextTarget()
    {
        linkToNextTarget = false;        
    }

    @Override
    public boolean isLinkToNextTarget()
    {
        return linkToNextTarget;
    }

    @Override
    public void finalize(PlaneMcu plane) throws PWCGException
    {
        createTargetAssociations();
        createObjectAssociations(plane);
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnit.write(writer);
        activationTimer.write(writer);
        activationEntity.write(writer);
    }
    
    private void createFlightMissionBegin() throws PWCGException
    {
        
        int startDelay = 1;
        if (flight.getFlightInformation().isAirStart() == false) 
        {
            ConfigManagerCampaign configManager = flight.getCampaign().getCampaignConfigManager();                        
            startDelay = configManager.getIntConfigParam(ConfigItemKeys.TakeoffTimeKey);
            if (startDelay > 30)
            {
                startDelay = 30;
            }
        }

        missionBeginUnit = new MissionBeginUnit(flight.getFlightHomePosition());
        missionBeginUnit.setStartTime(startDelay);
    }

    private void createActivation() throws PWCGException
    {
        IFlightInformation flightInformation = flight.getFlightInformation();
   
        activationEntity = new McuActivate();
        activationEntity.setName("Activate");
        activationEntity.setDesc("Activate entity");
        activationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());

        activationTimer = new McuTimer();
        activationTimer.setName("Activation Timer");
        activationTimer.setDesc("Activation Timer");
        activationTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());
    }

    private void createTargetAssociations()
    {
        missionBeginUnit.linkToMissionBegin(activationTimer.getIndex());
        activationTimer.setTarget(activationEntity.getIndex());
        activationTimer.setTarget(activationEntity.getIndex());
    }

    private void createObjectAssociations(PlaneMcu plane)
    {
        activationEntity.setObject(plane.getLinkTrId());
    }
    
    @Override
    public List<McuWaypoint> getAllWaypoints()
    {
        List<McuWaypoint> allWaypoints = new ArrayList<>();
        return allWaypoints;
    }

    @Override
    public boolean containsWaypoint(long waypointIdToFind)
    {
        return false;
    }

    @Override
    public McuWaypoint getWaypointById(long waypointId) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                
    }

    @Override
    public void replaceWaypoint(McuWaypoint waypoint) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                
    }

    @Override
    public void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                        
    }

    @Override
    public void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                        
    }

    @Override
    public void removeUnwantedWaypoints(List<McuWaypoint> waypointsInBriefing) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                                
    }

    @Override
    public IMissionPointSet duplicateWithOffset(IFlightInformation flightInformation, int positionInFormation) throws PWCGException
    {
        throw new PWCGException("Do not duplicate flight activate");                                
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        return allFlightPoints;
    }
}
