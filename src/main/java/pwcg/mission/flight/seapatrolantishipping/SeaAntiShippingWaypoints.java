package pwcg.mission.flight.seapatrolantishipping;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.ClimbWaypointGenerator;
import pwcg.mission.flight.waypoint.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.IIngressWaypoint;
import pwcg.mission.flight.waypoint.IngressWaypointNearFront;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class SeaAntiShippingWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public SeaAntiShippingWaypoints(Flight flight) throws PWCGException
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
        
        List<McuWaypoint> targetWaypoints = createTargetWaypoints();
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

    private List<McuWaypoint> createTargetWaypoints() throws PWCGException  
	{
        List<McuWaypoint> targetWaypoints = new ArrayList<>();

		// Total waypoints in the patrol pattern
		int numWaypoints = 3 + RandomNumberGenerator.getRandom(2);
		
		// This is the waypoint where we will meet the enemy
		int targetWaypointIndex = RandomNumberGenerator.getRandom(numWaypoints);
		
		// This is where we will meet the enemy
		McuWaypoint targetWP = createWP(flight.getTargetCoords(), WaypointType.RECON_WAYPOINT.getName());
		
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

		// Now form the complete list
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

		McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
		wp.setTriggerArea(McuWaypoint.COMBAT_AREA);
		wp.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), wpName);
		wp.setSpeed(flight.getFlightCruisingSpeed());

		wp.setPosition(coord);
		
		return wp;
	}	
}
