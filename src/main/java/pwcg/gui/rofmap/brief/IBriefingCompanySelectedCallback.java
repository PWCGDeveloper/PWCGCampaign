package pwcg.gui.rofmap.brief;

import java.util.Map;

import pwcg.core.exception.PWCGException;

public interface IBriefingCompanySelectedCallback
{
    void companiesSelectedChanged(Map<Integer, String> selectedCompanies) throws PWCGException;
}
