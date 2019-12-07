package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.transport.TransportFlight;

public class TransportObjective
{

    static String getMissionObjective(TransportFlight flight) throws PWCGException 
    {
        String objective = "Transport supplies to the airfield at " + flight.getArrivalAirfield().getName() + ".";
        return objective;
    }

}
