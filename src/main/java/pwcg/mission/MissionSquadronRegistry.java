package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

public class MissionSquadronRegistry
{
    private Map<Integer, Company> squadronsInUse = new HashMap<>();

    public boolean isSquadronAvailable(Company squadron)
    {
        if (squadronsInUse.containsKey(squadron.getCompanyId()))
        {
            return false;
        }
        return true;
    }

    public void registerSquadronForUse(Company squadron) throws PWCGException
    {
        if (squadronsInUse.containsKey(squadron.getCompanyId()))
        {
            throw new PWCGException("Duplicate use of squadron " + squadron.getCompanyId());
        }
        squadronsInUse.put(squadron.getCompanyId(), squadron);
    }

    public List<Company> removeSquadronsInUse(List<Company> squadrons)
    {
        List<Company> squadronsNotInUse = new ArrayList<>();
        for (Company squadron : squadrons)
        {
            if (!squadronsInUse.containsKey(squadron.getCompanyId()))
            {
                squadronsNotInUse.add(squadron);
            }
        }
        return squadronsNotInUse;
    }

}
