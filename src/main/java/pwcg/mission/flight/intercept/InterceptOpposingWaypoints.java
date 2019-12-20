package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.approach.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.egress.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.ingress.IIngressWaypoint;
import pwcg.mission.flight.waypoint.ingress.IngressWaypointNearFront;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptOpposingWaypoints
{
    private Flight flight;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public InterceptOpposingWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        McuWaypoint ingressWaypoint = createIngressWaypoint(flight);
        waypoints.add(ingressWaypoint);
        
        List<McuWaypoint> targetWaypoints = createTargetWaypoints();
        waypoints.addAll(targetWaypoints);

        
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        waypoints = WaypointGeneratorUtils.prependInitialToExistingWaypoints(flight, waypoints);

        return waypoints;
    }

    private McuWaypoint createIngressWaypoint(Flight flight) throws PWCGException  
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointNearFront(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }

	protected List<McuWaypoint> createTargetWaypoints() throws PWCGException  
	{		
        List<McuWaypoint> targetWaypoints = new ArrayList<>();

        McuWaypoint targetApproachWP = createTargetApproachWaypoint();
        targetWaypoints.add(targetApproachWP);    

        McuWaypoint targetWP = createTargetWaypoint();
        targetWaypoints.add(targetWP);    
		
        return targetWaypoints;
	}

	protected McuWaypoint createTargetApproachWaypoint() throws PWCGException  
	{
		double angle = 80.0;
		double distance = 4000.0;
		Coordinate coord = MathUtils.calcNextCoord(flight.getTargetPosition(), angle, distance);
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint targetApproachWP = WaypointFactory.createTargetApproachWaypointType();
		targetApproachWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		targetApproachWP.setSpeed(flight.getFlightCruisingSpeed());
		targetApproachWP.setPosition(coord);
		targetApproachWP.setTargetWaypoint(false);
		
		return targetApproachWP;
	}

	private McuWaypoint createTargetWaypoint() throws PWCGException  
	{
		Coordinate coord = flight.getTargetPosition();
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint targetWP = WaypointFactory.createTargetFinalWaypointType();
		targetWP.setTriggerArea(McuWaypoint.TARGET_AREA);
		targetWP.setSpeed(flight.getFlightCruisingSpeed());
		targetWP.setPosition(coord);	
		targetWP.setTargetWaypoint(true);

        return targetWP;
	}	
}
