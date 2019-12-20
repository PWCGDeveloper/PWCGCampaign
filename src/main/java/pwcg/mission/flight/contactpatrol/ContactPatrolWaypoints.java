package pwcg.mission.flight.contactpatrol;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.approach.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.egress.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.frontlines.PathAlongFront;
import pwcg.mission.flight.waypoint.frontlines.PathAlongFrontData;
import pwcg.mission.flight.waypoint.ingress.IIngressWaypoint;
import pwcg.mission.flight.waypoint.ingress.IngressWaypointNearFront;
import pwcg.mission.flight.waypoint.initial.InitialWaypointGenerator;
import pwcg.mission.mcu.McuWaypoint;

public class ContactPatrolWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public ContactPatrolWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
        this.campaign = flight.getCampaign();
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        InitialWaypointGenerator climbWaypointGenerator = new InitialWaypointGenerator(flight);
        List<McuWaypoint> initialWPs = climbWaypointGenerator.createInitialFlightWaypoints();
        waypoints.addAll(initialWPs);

        McuWaypoint ingressWaypoint = createIngressWaypoint(flight);
        waypoints.add(ingressWaypoint);
        
        List<McuWaypoint> targetWaypoints = createTargetWaypoints(ingressWaypoint.getPosition());
        waypoints.addAll(targetWaypoints);

        
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        return waypoints;
    }

    private McuWaypoint createIngressWaypoint(Flight flight) throws PWCGException  
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointNearFront(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }

	
	protected List<McuWaypoint> createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{	
	    List<McuWaypoint> targetWaypoints = new ArrayList<>();
	    PathAlongFrontData pathAlongFrontData = buildPathAlongFrontData(startPosition);
        PathAlongFront pathAlongFront = new PathAlongFront();
	    List<Coordinate> patrolCoordinates = pathAlongFront.createPathAlongFront(pathAlongFrontData);
	    
	    for (Coordinate patrolCoordinate : patrolCoordinates)
	    {
	        McuWaypoint waypoint = createWP(patrolCoordinate.copy());
	        waypoint.setTargetWaypoint(true);
	        waypoint.setName(WaypointType.RECON_WAYPOINT.getName());
	        targetWaypoints.add(waypoint);
	    }
        return targetWaypoints;
	}

	protected PathAlongFrontData buildPathAlongFrontData(Coordinate startPosition) throws PWCGException
	{
	    int patrolDistanceBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceBaseKey) * 1000;
	    int patrolDistanceRandom = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceRandomKey) * 1000;

	    int depthOfPenetrationMax = 6000;
	    int depthOfPenetration = RandomNumberGenerator.getRandom(depthOfPenetrationMax);
	    depthOfPenetration -= (depthOfPenetrationMax / 2);
	            
	    PathAlongFrontData pathAlongFrontData = new PathAlongFrontData();
	    pathAlongFrontData.setMission(flight.getMission());
	    pathAlongFrontData.setDate(campaign.getDate());
	    pathAlongFrontData.setOffsetTowardsEnemy(depthOfPenetration);
	    pathAlongFrontData.setPathDistance(patrolDistanceBase);
	    pathAlongFrontData.setRandomDistanceMax(patrolDistanceRandom);
	    pathAlongFrontData.setTargetGeneralLocation(startPosition);
	    pathAlongFrontData.setReturnAlongRoute(false);
	    pathAlongFrontData.setSide(flight.getSquadron().determineSquadronCountry(campaign.getDate()).getSide());
	    
	    return pathAlongFrontData;
	}

	private McuWaypoint createWP(Coordinate coord) throws PWCGException 
	{
        int xOffset = 100 - RandomNumberGenerator.getRandom(200);
        int zOffset = 100 - RandomNumberGenerator.getRandom(200);
		coord.setXPos(coord.getXPos() + xOffset);
		coord.setZPos(coord.getZPos() + zOffset);
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint wp = WaypointFactory.createReconWaypointType();
		wp.setTriggerArea(McuWaypoint.TARGET_AREA);
		wp.setSpeed(flight.getFlightCruisingSpeed());
		wp.setPosition(coord);

		return wp;
	}
}
