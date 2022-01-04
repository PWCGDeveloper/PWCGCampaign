package pwcg.gui.rofmap.brief;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

public interface IFlightChanged
{
    void flightChanged(Company squadron) throws PWCGException;
}
