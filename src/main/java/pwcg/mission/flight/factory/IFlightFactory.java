package pwcg.mission.flight.factory;

import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public interface IFlightFactory
{
    IFlight buildFlight(
            Mission mission,
            Company squadron,
            FlightTypes flightType,
            boolean isPlayerFlight) throws PWCGException;
}
