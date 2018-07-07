package pwcg.aar.outofmission.phase2.resupply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class ServiceResupplyNeed
{
    private Campaign campaign;
    private int serviceId;
    private SquadronNeedFactory squadronNeedFactory;
    private Map<Integer, ISquadronNeed> squadronNeeds = new HashMap<>();

    public ServiceResupplyNeed (Campaign campaign, int serviceId, SquadronNeedFactory squadronNeedFactory)
    {
        this.campaign = campaign;
        this.serviceId = serviceId;
        this.squadronNeedFactory = squadronNeedFactory;
    }
    
    public void determineResupplyNeed() throws PWCGException
    {
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
            int serviceIdForSquadron = squadronPersonnel.getSquadron().determineServiceForSquadron(campaign.getDate()).getServiceId();
            if (serviceId == serviceIdForSquadron)
            {
                ISquadronNeed squadronResupplyNeed = squadronNeedFactory.buildSquadronNeed(campaign, squadronPersonnel.getSquadron());
                squadronResupplyNeed.determineResupplyNeeded();
                if (squadronResupplyNeed.needsResupply())
                {
                    squadronNeeds.put(squadronResupplyNeed.getSquadronId(), squadronResupplyNeed);
                }
            }
        }
    }
    
    public boolean hasNeedySquadron()
    {
        if (squadronNeeds.isEmpty())
        {
            return false;
        }
        return true;
    }

    public int chooseNeedySquadron() throws PWCGException
    {
        if (squadronNeeds.isEmpty())
        {
            throw new PWCGException("No needy squadron to select");
        }
        
        List<Integer> squadronKeys = new ArrayList<>(squadronNeeds.keySet());
        int index = RandomNumberGenerator.getRandom(squadronKeys.size());
        int squadronKey = squadronKeys.get(index);
        
        ISquadronNeed squadronForReplacement = squadronNeeds.get(squadronKey);
        squadronForReplacement.noteResupply();
        if (!squadronForReplacement.needsResupply())
        {
            removeNeedySquadron(squadronKey);
        }
        
        return squadronForReplacement.getSquadronId();
    }

    public void removeNeedySquadron(int needySquadronId)
    {
        squadronNeeds.remove(needySquadronId);
    }
}
