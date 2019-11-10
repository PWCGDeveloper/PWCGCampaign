package pwcg.mission.flight.balloonBust;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.ClimbWaypointGenerator;
import pwcg.mission.flight.waypoint.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.IIngressWaypoint;
import pwcg.mission.flight.waypoint.IngressWaypointNearFront;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class BalloonBustWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public BalloonBustWaypoints(Flight flight) throws PWCGException
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
        
        McuWaypoint targetWaypoint = createTargetWaypoints();
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
	
    private McuWaypoint createTargetWaypoints() throws PWCGException  
	{
		Coordinate coord = new Coordinate();
		coord.setXPos(flight.getTargetCoords().getXPos() + 50.0);
		coord.setZPos(flight.getTargetCoords().getZPos());
        coord.setYPos(flight.getFlightAltitude());

		McuWaypoint balloonBustWP = WaypointFactory.createBalloonBustWaypointType();
		balloonBustWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		balloonBustWP.setSpeed(flight.getFlightCruisingSpeed());
		balloonBustWP.setPosition(coord);	
		balloonBustWP.setTargetWaypoint(true);
        return balloonBustWP;
	}
}
