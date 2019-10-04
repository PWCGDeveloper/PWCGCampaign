package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.ClimbWaypointGenerator;
import pwcg.mission.flight.waypoint.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.IIngressWaypoint;
import pwcg.mission.flight.waypoint.IngressWaypointNearFront;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptOpposingWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public InterceptOpposingWaypoints(Flight flight) throws PWCGException
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

        McuWaypoint ingressWaypoint = createIngressWaypoint(flight);
        waypoints.add(ingressWaypoint);
        
        List<McuWaypoint> targetWaypoints = createTargetWaypoints();
        waypoints.addAll(targetWaypoints);

        
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        WaypointGeneratorUtils.setWaypointsNonFighterPriority(flight, waypoints);

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
		Coordinate coord = MathUtils.calcNextCoord(flight.getTargetCoords(), angle, distance);
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
		Coordinate coord = flight.getTargetCoords();
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint targetWP = WaypointFactory.createTargetFinalWaypointType();
		targetWP.setTriggerArea(McuWaypoint.TARGET_AREA);
		targetWP.setSpeed(flight.getFlightCruisingSpeed());
		targetWP.setPosition(coord);	
		targetWP.setTargetWaypoint(true);

        return targetWP;
	}	
}
