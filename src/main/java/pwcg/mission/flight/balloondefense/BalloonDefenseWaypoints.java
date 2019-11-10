package pwcg.mission.flight.balloondefense;

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

public class BalloonDefenseWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public BalloonDefenseWaypoints(Flight flight) throws PWCGException
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
        
        McuWaypoint targetWaypoint = createTargetWaypoints();
        waypoints.add(targetWaypoint);

        
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
    
    
	
	
	protected McuWaypoint createTargetWaypoints() throws PWCGException  
	{
		Coordinate coord = new Coordinate();
		coord.setXPos(flight.getTargetCoords().getXPos() + 50.0);
		coord.setZPos(flight.getTargetCoords().getZPos() + 50.0);
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint balloonDefenseWP = WaypointFactory.createBalloonDefenseWaypointType();		
		balloonDefenseWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		balloonDefenseWP.setSpeed(flight.getFlightCruisingSpeed());
		balloonDefenseWP.setPosition(coord);	
		balloonDefenseWP.setTargetWaypoint(true);
        return balloonDefenseWP;
	}
}
