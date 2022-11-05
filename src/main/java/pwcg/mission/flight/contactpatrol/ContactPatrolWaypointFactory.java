package pwcg.mission.flight.contactpatrol;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.flight.waypoint.patterns.PathAlongFront;
import pwcg.mission.flight.waypoint.patterns.PathAlongFrontData;
import pwcg.mission.flight.waypoint.patterns.PathAlongFrontDataBuilder;
import pwcg.mission.mcu.McuWaypoint;

public class ContactPatrolWaypointFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public ContactPatrolWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> contactpatrolWaypoints = createTargetWaypoints(ingressWaypoint);
        missionPointSet.addWaypoints(contactpatrolWaypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }
    
    private List<McuWaypoint> createTargetWaypoints(McuWaypoint ingressWaypoint) throws PWCGException  
    {        
        PathAlongFrontDataBuilder pathAlongFrontDataBuilder = new PathAlongFrontDataBuilder(flight);
        PathAlongFrontData pathAlongFrontData = pathAlongFrontDataBuilder.buildPathAlongFrontData(ingressWaypoint.getPosition(), calculateDepthOfPenetration(), calculatePatrolDistance());
        
        PathAlongFront pathAlongFront = new PathAlongFront(flight.getCampaign());
        List<Coordinate> patrolCoordinates = pathAlongFront.createPathAlongFront(pathAlongFrontData);
        
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        for (Coordinate patrolCoordinate : patrolCoordinates)
        {
            McuWaypoint waypoint = createWP(patrolCoordinate.copy());
            waypoint.setTargetWaypoint(true);
            waypoint.setName(WaypointType.RECON_WAYPOINT.getName());
            targetWaypoints.add(waypoint);
        }
        return targetWaypoints;
    }

    private int calculatePatrolDistance() throws PWCGException
    {
        Campaign campaign = flight.getCampaign();
        int patrolDistanceBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceBaseKey) * 1000;
        int patrolDistanceRandom = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceRandomKey) * 1000;
        int patrolDistance = patrolDistanceBase + RandomNumberGenerator.getRandom(patrolDistanceRandom);
        patrolDistance = patrolDistance / 2;
        return patrolDistance;
    }

    private int calculateDepthOfPenetration()
    {
        int depthOfPenetration = RandomNumberGenerator.getRandom(6000);
        return depthOfPenetration;
    }

	private McuWaypoint createWP(Coordinate patrolCoord) throws PWCGException 
	{
		patrolCoord.setYPos(flight.getFlightInformation().getAltitude());

		McuWaypoint wp = WaypointFactory.createReconWaypointType();
		wp.setTriggerArea(McuWaypoint.TARGET_AREA);
		wp.setSpeed(flight.getFlightCruisingSpeed());
		wp.setPosition(patrolCoord);
		return wp;
	}
}
