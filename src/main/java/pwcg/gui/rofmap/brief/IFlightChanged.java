package pwcg.gui.rofmap.brief;

import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;

public interface IFlightChanged
{
    void flightChanged(Company squadron) throws PWCGException;
}
