package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.approach.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.egress.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.ingress.IIngressWaypoint;
import pwcg.mission.flight.waypoint.ingress.IngressWaypointNearFront;
import pwcg.mission.flight.waypoint.initial.InitialWaypointGenerator;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortWaypoints
{
    private Flight flight;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public PlayerEscortWaypoints(Flight flight) throws PWCGException
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
        
        McuWaypoint rendezvousWaypoint = createTargetWaypoint();
        waypoints.add(rendezvousWaypoint);      

        
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

	protected McuWaypoint createTargetWaypoint() throws PWCGException  
	{
		Coordinate coord = new Coordinate();
		coord.setXPos(flight.getTargetPosition().getXPos() + 50.0);
		coord.setZPos(flight.getTargetPosition().getZPos());
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint rendezvousWaypoint = WaypointFactory.createRendezvousWaypointType();
		rendezvousWaypoint.setTriggerArea(McuWaypoint.COMBAT_AREA);		
		rendezvousWaypoint.setPosition(coord);	
		rendezvousWaypoint.setTargetWaypoint(true);
        return rendezvousWaypoint;
	}
}
