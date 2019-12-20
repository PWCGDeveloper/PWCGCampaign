package pwcg.mission.flight.seapatrolscout;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.approach.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.egress.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.initial.InitialWaypointGenerator;
import pwcg.mission.mcu.McuWaypoint;

public class SeaPatrolWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public SeaPatrolWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
        this.campaign = flight.getCampaign();
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        InitialWaypointGenerator climbWaypointGenerator = new InitialWaypointGenerator(flight);
        List<McuWaypoint> initialWPs = climbWaypointGenerator.createInitialFlightWaypoints();
        waypoints.addAll(initialWPs);

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
        double angleToPatrolArea = MathUtils.calcAngle(flight.getPosition(), flight.getTargetPosition());
        double distanceToIngress = MathUtils.calcDist(flight.getPosition(), flight.getTargetPosition());
        distanceToIngress = distanceToIngress / 2;
        
        Coordinate midPointCoords = MathUtils.calcNextCoord(flight.getPosition(), angleToPatrolArea, distanceToIngress);
        midPointCoords.setYPos(flight.getFlightAltitude());

        McuWaypoint midPointWP = WaypointFactory.createMoveToWaypointType();
        midPointWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        midPointWP.setSpeed(flight.getFlightCruisingSpeed());
        midPointWP.setPosition(midPointCoords);
        midPointWP.setName("Mid Waypoint");
        return midPointWP;
    }

    protected List<McuWaypoint> createTargetWaypoints(Coordinate startPosition) throws PWCGException  
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<>();

        // Total waypoints in the patrol pattern
		int numWaypoints = 3 + RandomNumberGenerator.getRandom(2);
		
		// This is the waypoint where we will meet the enemy
		int targetWaypointIndex = RandomNumberGenerator.getRandom(numWaypoints);
		
		// This is where we will meet the enemy
		McuWaypoint targetWP = createWP(flight.getTargetPosition(), WaypointType.RECON_WAYPOINT.getName());
		
		// pattern orientation is the basic direction we are going
		int patternOrientation = RandomNumberGenerator.getRandom(360);

		// These are the waypoints before we will meet the enemy
		// For previous waypoints, zig zag towards the target		
		List<McuWaypoint> before = new ArrayList<McuWaypoint>();
		Coordinate lastCoord = targetWP.getPosition().copy();

		int angleAlternator = 0;
		double backAngle = MathUtils.adjustAngle(patternOrientation, -90);
		for (int i = 0; i < targetWaypointIndex; ++i)
		{
			double angle = MathUtils.adjustAngle(backAngle, (90 * angleAlternator * -1));
			
			int distance = 5 + RandomNumberGenerator.getRandom(5);
			distance *= 1000;
			Coordinate coord = MathUtils.calcNextCoord(lastCoord, angle, distance);
			McuWaypoint wp = createWP(coord, WaypointType.PATROL_WAYPOINT.getName());
			before.add(wp);
			
			lastCoord = coord;
			if (angleAlternator == 0)
			{
				angleAlternator = 1;
			}
			else
			{
				angleAlternator = 0;
			}
		}
		
		// These are the waypoints after we will meet the enemy
		// reverse the zig zag
        List<McuWaypoint> after = new ArrayList<McuWaypoint>();
		lastCoord = targetWP.getPosition().copy();

		angleAlternator = 0;
		int frontAngle = patternOrientation;
		for (int i = targetWaypointIndex + 1; i < numWaypoints; ++i)
		{
			double angle = MathUtils.adjustAngle(frontAngle, (90 * angleAlternator));

			int distance = 5 + RandomNumberGenerator.getRandom(5);
			distance *= 1000;
			Coordinate coord = MathUtils.calcNextCoord(lastCoord, angle, distance);
			McuWaypoint wp = createWP(coord, WaypointType.PATROL_WAYPOINT.getName());
			after.add(wp);
			
			lastCoord = coord;
			if (angleAlternator == 0)
			{
				angleAlternator = 1;
			}
			else
			{
				angleAlternator = 0;
			}
		}

		for (int i = before.size() - 1; i >= 0; --i)
		{
		    targetWaypoints.add(before.get(i));
		}
		
		targetWaypoints.add(targetWP);
		
		for (McuWaypoint afterWp : after)
		{
		    targetWaypoints.add(afterWp);
		}
		
        return after;
	}
    
	private McuWaypoint createWP(Coordinate coord, String wpName) throws PWCGException 
	{
		coord.setXPos(coord.getXPos());
		coord.setZPos(coord.getZPos());
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint patrolWaypoint = WaypointFactory.createPatrolWaypointType();
		patrolWaypoint.setTriggerArea(McuWaypoint.COMBAT_AREA);
		patrolWaypoint.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), wpName);
		patrolWaypoint.setSpeed(flight.getFlightCruisingSpeed());
		patrolWaypoint.setPosition(coord);
		
		return patrolWaypoint;
	}
}
