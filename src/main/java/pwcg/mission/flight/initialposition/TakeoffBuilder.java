package pwcg.mission.flight.initialposition;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuTakeoff;

public class TakeoffBuilder
{
    public static McuTakeoff createTakeoff(Flight flight) throws PWCGException
    {
        if (flight.getFlightInformation().isPlayerFlight())
        {
            if (!flight.getFlightInformation().isAirStart())
            {
                McuTakeoff takeoff = new McuTakeoff();
                takeoff.setPosition(flight.getFlightInformation().getDepartureAirfield().getTakeoffLocation().getPosition().copy());
                takeoff.setOrientation(flight.getFlightInformation().getDepartureAirfield().getTakeoffLocation().getOrientation().copy());
                return takeoff;
            }
        }
        return null;
    }
}
