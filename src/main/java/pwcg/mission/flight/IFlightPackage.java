package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;

public interface IFlightPackage
{
    List<IFlight> createPackage (Mission mission, ICountry country) throws PWCGException;
}
