package pwcg.mission.flight.factory;

import pwcg.campaign.company.Company;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public interface IFlightTypeFactory
{
    FlightTypes getFlightType(Company squadron, boolean isPlayerFlight, PwcgRole roleOverride) throws PWCGException;
}
