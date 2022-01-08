package pwcg.gui.rofmap.brief;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

public interface IUnitChanged
{
    void unitChanged(Company squadron) throws PWCGException;
}
