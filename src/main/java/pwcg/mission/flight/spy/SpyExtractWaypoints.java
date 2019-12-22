package pwcg.mission.flight.spy;

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

public class SpyExtractWaypoints
{
    private Flight flight;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public SpyExtractWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        InitialWaypointGenerator initialWaypointGenerator = new InitialWaypointGenerator(flight);
        List<McuWaypoint> initialWPs = initialWaypointGenerator.createInitialFlightWaypoints();
        waypoints.addAll(initialWPs);

        McuWaypoint ingressWaypoint = createIngressWaypoint();
        waypoints.add(ingressWaypoint);
        
        McuWaypoint targetWaypoint = createTargetWaypoint(ingressWaypoint.getPosition());
        waypoints.add(targetWaypoint);

        
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
    
	protected McuWaypoint createTargetWaypoint(Coordinate startPosition) throws PWCGException  
	{
	    Coordinate pickupLocation = getSpyExtractLocation(startPosition);
	    McuWaypoint approachWP = createSpyExtractWaypoint(pickupLocation);
        return approachWP;		
	}

    private Coordinate getSpyExtractLocation(Coordinate startPosition) throws PWCGException
    {
		Coordinate pickupLocation = flight.getTargetPosition().copy();
        pickupLocation.setYPos(flight.getFlightAltitude());
        return pickupLocation;
    }

    private McuWaypoint createSpyExtractWaypoint(Coordinate pickupLocation)
    {
        McuWaypoint approachWP = WaypointFactory.createSpyExtractWaypointType();
        approachWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        approachWP.setSpeed(flight.getFlightCruisingSpeed());
        approachWP.setPosition(pickupLocation);
        approachWP.setTargetWaypoint(true);
        return approachWP;
    }
}
