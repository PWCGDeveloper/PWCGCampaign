package pwcg.campaign.resupply;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.CompanyPersonnel;
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
        for (CompanyPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
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
