package pwcg.mission.flight.initialposition;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;

public class FlightPositionSetter
{
    public static void setFlightInitialPosition(Flight flight) throws PWCGException
    {
        if (flight.isAirStart())
        {
            FlightPositionAirStart.createPlanePositionAirStart(flight);
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
}
