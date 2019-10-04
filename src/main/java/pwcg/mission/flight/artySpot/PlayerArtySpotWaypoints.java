package pwcg.mission.flight.artySpot;

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
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerArtySpotWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public PlayerArtySpotWaypoints(Flight flight) throws PWCGException
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
        
        McuWaypoint targetWaypoint = createTargetWaypoints(ingressWaypoint.getPosition());
        waypoints.add(targetWaypoint);

        
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

	private McuWaypoint createTargetWaypoints(Coordinate startPosition) throws PWCGException, PWCGException
	{
		McuWaypoint artySpotWP = WaypointFactory.createArtillerySpotWaypointType();

		artySpotWP.setTriggerArea(McuWaypoint.TARGET_AREA);
		artySpotWP.setSpeed(flight.getFlightCruisingSpeed());
		
		// Don't put the player right over the target
		double offsetDirection = RandomNumberGenerator.getRandom(360);
		double offsetDistance = 200 + RandomNumberGenerator.getRandom(1300);
		Coordinate targetCoord = MathUtils.calcNextCoord(flight.getTargetCoords(), offsetDirection, offsetDistance);
        targetCoord.setYPos(flight.getFlightAltitude());
		artySpotWP.setPosition(targetCoord);	

		artySpotWP.setTargetWaypoint(true);

		return artySpotWP;
	}
}
