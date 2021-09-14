package pwcg.mission.flight.factory;

import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public interface IFlightTypeFactory
{
    FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight, PwcgRole roleOverride) throws PWCGException;
}
