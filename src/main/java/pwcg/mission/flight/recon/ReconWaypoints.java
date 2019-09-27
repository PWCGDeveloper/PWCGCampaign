package pwcg.mission.flight.recon;

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
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.mcu.McuWaypoint;

public abstract class ReconWaypoints
{
    protected Flight flight;
    protected Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    abstract protected List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException;
    
    public ReconWaypoints(Flight flight) throws PWCGException
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
        
        List<McuWaypoint> targetWaypoints = createTargetWaypoints(ingressWaypoint.getPosition());
        waypoints.addAll(targetWaypoints);

        
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        WaypointGeneratorUtils.setWaypointsNonFighterPriority(waypoints);

        return waypoints;
    }

    private McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointNearFront(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }


	protected McuWaypoint createWP(Coordinate coord) throws PWCGException 
	{
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint wp = WaypointFactory.createReconWaypointType();
		wp.setTriggerArea(McuWaypoint.TARGET_AREA);
		wp.setSpeed(flight.getFlightCruisingSpeed());
		wp.setPosition(coord);

		return wp;
	}
}
