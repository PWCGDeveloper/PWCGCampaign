package pwcg.mission.flight.initialposition;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class FlightPositionSetter
{
    public static void setFlightInitialPosition(IFlight flight) throws PWCGException
    {
        if (flight.getFlightInformation().isAirStart())
        {
            FlightPositionAirStart.createPlanePositionAirStart(flight);
        }
        else if (flight.getFlightInformation().isParkedStart())
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
