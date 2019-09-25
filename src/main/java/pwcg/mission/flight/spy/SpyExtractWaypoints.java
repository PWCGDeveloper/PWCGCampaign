package pwcg.mission.flight.spy;

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
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class SpyExtractWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public SpyExtractWaypoints(Flight flight) throws PWCGException
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
        
        McuWaypoint targetWaypoint = createTargetWaypoint(ingressWaypoint.getPosition());
        waypoints.add(targetWaypoint);

        
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        WaypointGeneratorBase.setWaypointsNonFighterPriority(waypoints);

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
		Coordinate pickupLocation = flight.getTargetCoords().copy();
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
