package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.initialposition.FlightPositionAirStart;

public class FlightPositionSetter
{
    public static void setFlightInitialPosition(IFlight flight) throws PWCGException
    {
        FlightPositionAirStart.createPlanePositionAirStart(flight);
    }
}
