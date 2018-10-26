package pwcg.aar.outofmission.phase2.resupply;

import pwcg.aar.outofmission.phase2.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class ResupplyNeedBuilder
{
    private Campaign campaign;
    private ArmedService service;

    public ResupplyNeedBuilder (Campaign campaign, ArmedService service)
    {
        this.campaign = campaign;
        this.service = service;
    }
    
    public ServiceResupplyNeed determineNeedForService(SquadronNeedType need) throws PWCGException
    {
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(need);
        ServiceResupplyNeed serviceTransferNeed = new ServiceResupplyNeed(campaign, service.getServiceId(), squadronNeedFactory);
        serviceTransferNeed.determineResupplyNeed();
        return serviceTransferNeed;
    }
}
