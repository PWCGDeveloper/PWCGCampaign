package pwcg.mission.flight.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.ClimbWaypointGenerator;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.mcu.McuWaypoint;

public class TransportWaypoints
{
    private IAirfield fromAirfield;
    private IAirfield toAirfield;

    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public TransportWaypoints(Flight flight, IAirfield fromAirfield, IAirfield toAirfield) throws PWCGException
    {
        this.flight = flight;
        this.toAirfield = toAirfield;
        this.fromAirfield = fromAirfield;
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
        
        List<McuWaypoint> targetWaypoints = createTargetWaypoints();
        waypoints.addAll(targetWaypoints);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        WaypointGeneratorUtils.setWaypointsNonFighterPriority(flight, waypoints);

        return waypoints;
    }

    private List<McuWaypoint> createTargetWaypoints() throws PWCGException
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        
        McuWaypoint midPointWP = createMidWaypoint();
        targetWaypoints.add(midPointWP);
        
        McuWaypoint destinationWP = createDestinationWaypoint(midPointWP);
        targetWaypoints.add(destinationWP);
        
        return targetWaypoints;
    }

    private McuWaypoint createMidWaypoint() throws PWCGException  
    {
        double angleFromTargetToHomeAirfield = MathUtils.calcAngle(fromAirfield.getLandingLocation().getPosition(), toAirfield.getLandingLocation().getPosition());
        double distanceBetweenAirfields = MathUtils.calcDist(fromAirfield.getTakeoffLocation().getPosition(), toAirfield.getLandingLocation().getPosition());
        distanceBetweenAirfields = distanceBetweenAirfields / 2;
        
        Coordinate midPointCoords = MathUtils.calcNextCoord(fromAirfield.getTakeoffLocation().getPosition(), angleFromTargetToHomeAirfield, distanceBetweenAirfields);
        midPointCoords.setYPos(flight.getFlightAltitude());

        McuWaypoint midPointWP = WaypointFactory.createMoveToWaypointType();
        midPointWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        midPointWP.setSpeed(flight.getFlightCruisingSpeed());
        midPointWP.setPosition(midPointCoords);
        midPointWP.setName("Mid Waypoint");
        return midPointWP;
    }
			
    private McuWaypoint createDestinationWaypoint(McuWaypoint lastWp) throws PWCGException  
	{
        double angleFromTargetToHomeAirfield = MathUtils.calcAngle(toAirfield.getLandingLocation().getPosition(), lastWp.getPosition());
        
        Coordinate destinationCoords = MathUtils.calcNextCoord(toAirfield.getLandingLocation().getPosition(), angleFromTargetToHomeAirfield, 10000.0);
        destinationCoords.setYPos(flight.getFlightAltitude());

        McuWaypoint destinationWP = WaypointFactory.createMoveToWaypointType();
        destinationWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        destinationWP.setSpeed(flight.getFlightCruisingSpeed());
        destinationWP.setPosition(destinationCoords);
        destinationWP.setName("Destination");
        return destinationWP;
	}
}
