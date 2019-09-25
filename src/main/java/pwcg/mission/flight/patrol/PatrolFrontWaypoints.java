package pwcg.mission.flight.patrol;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.ClimbWaypointGenerator;
import pwcg.mission.flight.waypoint.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.IIngressWaypoint;
import pwcg.mission.flight.waypoint.IngressWaypointNearFront;
import pwcg.mission.flight.waypoint.PathAlongFront;
import pwcg.mission.flight.waypoint.PathAlongFrontData;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class PatrolFrontWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public PatrolFrontWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
        this.campaign = flight.getCampaign();
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        if (flight.isPlayerFlight())
        {
            ClimbWaypointGenerator climbWaypointGenerator = new ClimbWaypointGenerator(campaign, flight);
            List<McuWaypoint> climbWPs = climbWaypointGenerator.createClimbWaypoints(flight.getFlightInformation().getAltitude());
            waypoints.addAll(climbWPs);
        }

        McuWaypoint ingressWaypoint = createIngressWaypoint();
        waypoints.add(ingressWaypoint);
        
        List<McuWaypoint> targetWaypoints = createTargetWaypoints(ingressWaypoint.getPosition());
        waypoints.addAll(targetWaypoints);

        
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        return waypoints;
    }

    private McuWaypoint createIngressWaypoint() throws PWCGException  
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
            waypoint.setName(WaypointType.PATROL_WAYPOINT.getName());
            targetWaypoints.add(waypoint);
        }
        return targetWaypoints;
    }

    protected PathAlongFrontData buildPathAlongFrontData(Coordinate ingressPosition) throws PWCGException
    {
        int patrolDistanceBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceBaseKey) * 1000;
        patrolDistanceBase = patrolDistanceBase * 2;
        int patrolDistanceRandom = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceRandomKey) * 1000;

        int depthOfPenetrationMax = 3000;
        int depthOfPenetration = RandomNumberGenerator.getRandom(depthOfPenetrationMax);
        depthOfPenetration += 500;
        depthOfPenetration *= -1;
                
        PathAlongFrontData pathAlongFrontData = new PathAlongFrontData();
        pathAlongFrontData.setMission(flight.getMission());
        pathAlongFrontData.setDate(campaign.getDate());
        pathAlongFrontData.setOffsetTowardsEnemy(depthOfPenetration);
        pathAlongFrontData.setPathDistance(patrolDistanceBase / 2);
        pathAlongFrontData.setRandomDistanceMax(patrolDistanceRandom / 2);
        pathAlongFrontData.setTargetGeneralLocation(ingressPosition);
        pathAlongFrontData.setReturnAlongRoute(true);
        pathAlongFrontData.setSide(flight.getSquadron().determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide());
        
        return pathAlongFrontData;
    }

	private McuWaypoint createWP(Coordinate coord) throws PWCGException 
	{
		McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
		wp.setTriggerArea(McuWaypoint.COMBAT_AREA);
		wp.setSpeed(flight.getFlightCruisingSpeed());
		wp.setPosition(coord);
		wp.getPosition().setYPos(flight.getFlightAltitude());

		return wp;
	}
}
