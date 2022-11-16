package pwcg.campaign.resupply;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class ServiceResupplyNeed
{
    private Campaign campaign;
    private int serviceId;
    private SquadronNeedFactory squadronNeedFactory;
    private Map<Integer, ISquadronNeed> squadronNeeds = new TreeMap<>();

    public ServiceResupplyNeed (Campaign campaign, int serviceId, SquadronNeedFactory squadronNeedFactory)
    {
        this.campaign = campaign;
        this.serviceId = serviceId;
        this.squadronNeedFactory = squadronNeedFactory;
    }
    
    public void determineResupplyNeed() throws PWCGException
    {
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllActiveSquadronPersonnel(campaign.getDate()))
        {
            Squadron squadron = squadronPersonnel.getSquadron();
            int serviceIdForSquadron = squadron.determineServiceForSquadron(campaign.getDate()).getServiceId();
            if (serviceId == serviceIdForSquadron)
            {
                ISquadronNeed squadronResupplyNeed = squadronNeedFactory.buildSquadronNeed(campaign, squadron);
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

    public ISquadronNeed chooseNeedySquadron() throws PWCGException
    {        
        ResupplySquadronChooser resupplySquadronChooser = new ResupplySquadronChooser(campaign, squadronNeeds);
        return resupplySquadronChooser.getNeedySquadron();
    }

    public void removeNeedySquadron(ISquadronNeed squadronNeed)
    {
        squadronNeeds.remove(squadronNeed.getSquadronId());
    }

    public void noteResupply(ISquadronNeed squadronNeed)
    {
        squadronNeed.noteResupply();
        if (!squadronNeed.needsResupply())
        {
            removeNeedySquadron(squadronNeed);
        }
    }

    public Map<Integer, ISquadronNeed> getSquadronNeeds()
    {
        return squadronNeeds;
    }
}
