package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class MissionSquadronRegistry
{
    private Map<Integer, Squadron> squadronsInUse = new HashMap<>();

    public boolean isSquadronAvailable(Squadron squadron)
    {
        if (squadronsInUse.containsKey(squadron.getSquadronId()))
        {
            return false;
        }
        return true;
    }

    public void registerSquadronForUse(Squadron squadron) throws PWCGException
    {
        if (squadronsInUse.containsKey(squadron.getSquadronId()))
        {
            throw new PWCGException("Duplicate use of squadron " + squadron.getSquadronId());
        }
        squadronsInUse.put(squadron.getSquadronId(), squadron);
    }

    public List<Squadron> removeSquadronsInUse(List<Squadron> squadrons)
    {
        List<Squadron> squadronsNotInUse = new ArrayList<>();
        for (Squadron squadron : squadrons)
        {
            if (!squadronsInUse.containsKey(squadron.getSquadronId()))
            {
                squadronsNotInUse.add(squadron);
            }
        }
        return squadronsNotInUse;
    }

}
