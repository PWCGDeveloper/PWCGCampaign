package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class EscortForPlayerWaypoints
{
    private Flight flight;
    private Flight playerFlight;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public EscortForPlayerWaypoints(Flight flight, Flight playerFlight) throws PWCGException
    {
        this.flight = flight;
        this.playerFlight = playerFlight;
    }

    protected List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException
    {
        McuWaypoint ingressWP = createIngressWaypoint();
        McuWaypoint rtbWP = createReturnToBaseWaypoint();

        waypoints.add(ingressWP);        
        waypoints.add(rtbWP);  
        
        waypoints = WaypointGeneratorUtils.prependInitialToExistingWaypoints(flight, waypoints);
        
        return waypoints;
    }


    private McuWaypoint createIngressWaypoint() throws PWCGException
    {
        Coordinate ingresseCoords = getCoverPosition();
        Orientation orient = new Orientation();
        orient.setyOri(flight.getFlightInformation().getDepartureAirfield().getOrientation().getyOri());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.START_AREA);
        ingressWP.setSpeed(flight.getFlightCruisingSpeed());
        ingressWP.setPosition(ingresseCoords);
        ingressWP.setOrientation(orient);
        return ingressWP;
    }

    private Coordinate getCoverPosition()
    {
        McuWaypoint ingressWP = WaypointGeneratorUtils.findWaypointByType(playerFlight.getAllFlightWaypoints(), 
                WaypointType.INGRESS_WAYPOINT.getName());

        Coordinate coverPosition = ingressWP.getPosition().copy();
        coverPosition.setYPos(coverPosition.getYPos() + 400);
        return coverPosition;
    }

    private McuWaypoint createReturnToBaseWaypoint() throws PWCGException
    {
        Coordinate returnToBaseCoords = flight.getFlightInformation().getDepartureAirfield().getPosition().copy();
        returnToBaseCoords.setYPos(2000.0);

        Orientation orient = new Orientation();
        orient.setyOri(flight.getFlightInformation().getDepartureAirfield().getOrientation().getyOri());

        McuWaypoint rtbWP = WaypointFactory.createReturnToBaseWaypointType();
        rtbWP.setTriggerArea(McuWaypoint.START_AREA);
        rtbWP.setSpeed(flight.getFlightCruisingSpeed());
        rtbWP.setPosition(returnToBaseCoords);
        rtbWP.setOrientation(orient);
        return rtbWP;
    }
}
