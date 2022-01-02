package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;

public class MissionSquadronRegistry
{
    private Map<Integer, Company> squadronsInUse = new HashMap<>();

    public boolean isSquadronAvailable(Company squadron)
    {
        if (squadronsInUse.containsKey(squadron.getSquadronId()))
        {
            return false;
        }
        return true;
    }

    public void registerSquadronForUse(Company squadron) throws PWCGException
    {
        if (squadronsInUse.containsKey(squadron.getSquadronId()))
        {
            throw new PWCGException("Duplicate use of squadron " + squadron.getSquadronId());
        }
        squadronsInUse.put(squadron.getSquadronId(), squadron);
    }

    public List<Company> removeSquadronsInUse(List<Company> squadrons)
    {
        List<Company> squadronsNotInUse = new ArrayList<>();
        for (Company squadron : squadrons)
        {
            if (!squadronsInUse.containsKey(squadron.getSquadronId()))
            {
                squadronsNotInUse.add(squadron);
            }
        }
        return squadronsNotInUse;
    }

}
