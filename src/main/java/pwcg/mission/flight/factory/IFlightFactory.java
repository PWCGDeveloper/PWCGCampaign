package pwcg.mission.flight.factory;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public interface IFlightFactory
{
    IFlight buildFlight(
            Mission mission,
            Squadron squadron,
            FlightTypes flightType,
            boolean isPlayerFlight) throws PWCGException;
}
