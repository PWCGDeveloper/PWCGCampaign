package pwcg.mission.flight.factory;

import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public interface IFlightTypeFactory
{
    FlightTypes getFlightType(Company squadron, boolean isPlayerFlight, PwcgRole roleOverride) throws PWCGException;
}
