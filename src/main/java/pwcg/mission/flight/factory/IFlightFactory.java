package pwcg.mission.flight.factory;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public interface IFlightFactory
{
    Flight buildFlight(
            Mission mission,
            Squadron squadron,
            FlightTypes flightType,
            boolean isPlayerFlight) throws PWCGException;
}
