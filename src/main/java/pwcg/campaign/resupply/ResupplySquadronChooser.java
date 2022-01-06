package pwcg.campaign.resupply;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.utils.RandomNumberGenerator;

public class ResupplySquadronChooser
{
    private Campaign campaign;
    private Map<Integer, ISquadronNeed> squadronNeeds = new TreeMap<>();
    private List<ISquadronNeed> playerSquadronNeeds = new ArrayList<>();
    private List<ISquadronNeed> desperateSquadronNeeds = new ArrayList<>();
    private List<ISquadronNeed> genericSquadronNeeds = new ArrayList<>();

    public ResupplySquadronChooser(Campaign campaign, Map<Integer, ISquadronNeed> squadronNeeds)
    {
        this.campaign = campaign;
        this.squadronNeeds = squadronNeeds;
    }
    
    public ISquadronNeed getNeedySquadron()
    {
        clearPreviousAssessment();
        sortNeedySquadrons();
        return chooseNeedySquadron();
    }

    private void clearPreviousAssessment()
    {
        playerSquadronNeeds.clear();
        desperateSquadronNeeds.clear();
        genericSquadronNeeds.clear();        
    }

    private ISquadronNeed chooseNeedySquadron()
    {
        if (playerSquadronNeeds.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(playerSquadronNeeds.size());
            return playerSquadronNeeds.get(index);
        }
        else if (desperateSquadronNeeds.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(desperateSquadronNeeds.size());
            return desperateSquadronNeeds.get(index);
        }
        else if (genericSquadronNeeds.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(genericSquadronNeeds.size());
            return genericSquadronNeeds.get(index);
        }
        else
        {
            return null;
        }
    }

    private void sortNeedySquadrons()
    {
        for (ISquadronNeed squadronNeed : squadronNeeds.values())
        {
            if (Company.isPlayerCompany(campaign, squadronNeed.getSquadronId()))
            {
                desperateSquadron(playerSquadronNeeds, squadronNeed, 4);
            }
            else
            {
                desperateSquadron(desperateSquadronNeeds, squadronNeed, 6);
            }
        }
    }
    
    private void desperateSquadron(List<ISquadronNeed> desperatePool, ISquadronNeed squadronNeed, int limit)
    {
        if (squadronNeed.getNumNeeded() > 0)
        {
            if (squadronNeed.getNumNeeded() > limit)
            {
                desperatePool.add(squadronNeed);
            }
            else
            {
                genericSquadronNeeds.add(squadronNeed);
            }
        }
    }
}
