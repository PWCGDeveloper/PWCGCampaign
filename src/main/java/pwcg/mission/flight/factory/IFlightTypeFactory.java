package pwcg.mission.flight.factory;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public interface IFlightTypeFactory
{
    public FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException;
}
