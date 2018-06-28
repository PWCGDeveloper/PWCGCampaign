package pwcg.mission.flight.waypoint;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class IngressWaypointFactory
{

    public static IIngressWaypoint getIngressGenerator(Flight flight, Coordinate lastPosition, Coordinate targetPosition, int waypointSpeed, int flightAlt) throws PWCGException, PWCGException
    {
        IIngressWaypoint ingressWaypointGenerator = null;
        if ((!flight.isFighterFlight()) && flight.isPlayerFlight())
        {
            ingressWaypointGenerator =  new IngressWaypointEscortedFlight(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else if (flight.getFlightType() == FlightTypes.SCRAMBLE_OPPOSE)
        {
            ingressWaypointGenerator = new IngressWaypointScrambleOpposition(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else if (flight.getFlightType() == FlightTypes.HOME_DEFENSE)
        {
            ingressWaypointGenerator = new IngressWaypointNearTarget(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else if (flight.getFlightType() == FlightTypes.ANTI_SHIPPING)
        {
            ingressWaypointGenerator = new IngressWaypointNearTarget(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            ingressWaypointGenerator = new IngressWaypointNearTarget(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else if (flight.getFlightType() == FlightTypes.STRATEGIC_BOMB)
        {
            ingressWaypointGenerator = new IngressWaypointNearTarget(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else if (flight.getFlightType() == FlightTypes.BOMB)
        {
            ingressWaypointGenerator = new IngressWaypointNearTarget(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else if (flight.getFlightType() == FlightTypes.LOW_ALT_BOMB)
        {
            ingressWaypointGenerator = new IngressWaypointNearTarget(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else if (flight.getFlightType() == FlightTypes.DIVE_BOMB)
        {
            ingressWaypointGenerator = new IngressWaypointNearTarget(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            ingressWaypointGenerator = new IngressWaypointNearFront(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }
        else
        {
            ingressWaypointGenerator = new IngressWaypointNearFront(flight, lastPosition, targetPosition, waypointSpeed, flightAlt);
        }

        return ingressWaypointGenerator;
    }


}
