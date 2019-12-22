package pwcg.mission.flight.attack;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.approach.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.attack.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.egress.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.ingress.IIngressWaypoint;
import pwcg.mission.flight.waypoint.ingress.IngressWaypointNearFront;
import pwcg.mission.flight.waypoint.initial.InitialWaypointGenerator;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackWaypoints
{
    private Flight flight;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public GroundAttackWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        InitialWaypointGenerator initialWaypointGenerator = new InitialWaypointGenerator(flight);
        List<McuWaypoint> initialWPs = initialWaypointGenerator.createInitialFlightWaypoints();
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

	protected List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException  
	{
	    int attackAltitude = createAttackAltitude();
		GroundAttackWaypointHelper groundAttackWaypointHelper = new GroundAttackWaypointHelper(flight, ingressPosition, attackAltitude);
		List<McuWaypoint> groundAttackWaypoints = groundAttackWaypointHelper.createTargetWaypoints();
        return groundAttackWaypoints;
	}

	private int createAttackAltitude()
	{
		int randomVariation = RandomNumberGenerator.getRandom(30) + 1;
		randomVariation = randomVariation * 10;
		randomVariation = 100 - randomVariation;
		return 1000 + randomVariation;
	}
}
