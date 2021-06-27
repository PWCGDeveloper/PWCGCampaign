package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.CoalitionFactory;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightActivateTriggered implements IMissionPointSet
{
    private IFlight flight;

    private MissionBeginUnit missionBeginUnit;
    private McuTimer activationTimer = null;
    private McuTimer takeoffStartTimer = null;
    private McuCheckZone takeoffCheckZone = null;
    private McuActivate activationEntity = null;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;
    private int delaySeconds = 1;

    public MissionPointFlightActivateTriggered(IFlight flight)
    {
        this.flight = flight;
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_ACTIVATE;
    }
    
    public MissionPointFlightActivateTriggered(IFlight flight, int delaySeconds)
    {
        this.flight = flight;
        this.delaySeconds = delaySeconds;
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_ACTIVATE;
    }

    public void createFlightActivate() throws PWCGException, PWCGException 
    {
        createFlightMissionBegin();
        createActivation();
        createTargetAssociations();
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        takeoffStartTimer.setTarget(nextTargetIndex);
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
    public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException
    {
        createObjectAssociations(flightPlanes.getFlightLeader());
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnit.write(writer);
        takeoffCheckZone.write(writer);
        activationTimer.write(writer);
        activationEntity.write(writer);
        takeoffStartTimer.write(writer);
    }
    
    private void createFlightMissionBegin() throws PWCGException
    {
        missionBeginUnit = new MissionBeginUnit(flight.getFlightHomePosition());
        missionBeginUnit.setStartTime(1);
    }

    private void createActivation() throws PWCGException
    {
        FlightInformation flightInformation = flight.getFlightInformation();
   
        Coalition enemyCoalition = CoalitionFactory.getEnemyCoalition(flight.getSquadron().getCountry());

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int triggerRadius = productSpecificConfiguration.getSmallMissionRadius();
        
        takeoffCheckZone = new McuCheckZone("Check Zone Takeoff");
        takeoffCheckZone.setZone(triggerRadius);
        takeoffCheckZone.triggerCheckZoneByCoalition(enemyCoalition);
        takeoffCheckZone.setDesc("Check Zone Takeoff");
        takeoffCheckZone.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());

        activationEntity = new McuActivate();
        activationEntity.setName("Activate");
        activationEntity.setDesc("Activate entity");
        activationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());

        activationTimer = new McuTimer();
        activationTimer.setName("Activation Timer");
        activationTimer.setDesc("Activation Timer");
        activationTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());        
        activationTimer.setTime(1);

        takeoffStartTimer = new McuTimer();
        takeoffStartTimer.setName("Takeoff Start Timer");
        takeoffStartTimer.setDesc("Takeoff Start  Timer");
        takeoffStartTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());        
        takeoffStartTimer.setTime(delaySeconds);
    }

    private void createTargetAssociations()
    {
        missionBeginUnit.linkToMissionBegin(takeoffCheckZone.getIndex());
        takeoffCheckZone.setTarget(activationTimer.getIndex());
        activationTimer.setTarget(activationEntity.getIndex());
        activationTimer.setTarget(takeoffStartTimer.getIndex());
        takeoffStartTimer.setTarget(takeoffStartTimer.getIndex());
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
    public void updateWaypointFromBriefing(BriefingMapPoint waypoint) throws PWCGException
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
    public long addWaypointFromBriefing(BriefingMapPoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                                
    }

    @Override
    public void removeUnwantedWaypoints(List<BriefingMapPoint> waypointsInBriefing) throws PWCGException
    {
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }

    public McuTimer getMissionBeginTimer()
    {
        return activationTimer;
    }    
}
