package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;

public class FlightPositionSetter
{
    public static void setPlayerFlightInitialPosition(Flight flight) throws PWCGException
    {
        if (flight.isAirStart())
        {
            FlightPositionAirStart airStart = new FlightPositionAirStart(flight);
            airStart.createPlanePositionAirStart();
        }
        else if (flight.isParkedStart())
        {
            FlightPositionParkedStart parkedStart = new FlightPositionParkedStart(flight);
            parkedStart.createPlanePositionParkedStart();
        }
        else
        {
            FlightPositionRunwayStart runwayStart = new FlightPositionRunwayStart(flight);
            runwayStart.createPlanePositionRunway();
        }
    }
    
    public static void setEscortedFlightToRendezvous(Flight escortFlight, Flight escortedFlight) throws PWCGException
    {
        McuWaypoint rendezvousWP = escortFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_RENDEZVOUS);
        FlightPositionRendezvous rendezvous = new FlightPositionRendezvous(escortedFlight);
        
        Coordinate rendezvousAirStart = rendezvousWP.getPosition().copy();
        rendezvousAirStart.setYPos(rendezvousAirStart.getYPos() - 500);
        rendezvous.createPlanePositionAtRendezvous(rendezvousAirStart);
    }
}
