package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.outofmission.phase2.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;

public class EquipmentNeedBuilder
{
    private Campaign campaign;
    private Map<Integer, ServiceResupplyNeed> serviceTransferNeeds = new HashMap<>();

    public EquipmentNeedBuilder (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void initialize(SquadronNeedType need) throws PWCGException
    {
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        for (ArmedService armedService : serviceManager.getAllArmedServices())
        {
            SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
            ServiceResupplyNeed serviceResupplyNeed = new ServiceResupplyNeed(campaign, armedService.getServiceId(), squadronNeedFactory);
            serviceResupplyNeed.determineResupplyNeed();
            serviceTransferNeeds.put(armedService.getServiceId(), serviceResupplyNeed);
        }
    }
    
    public ServiceResupplyNeed getServiceTransferNeed(int serviceId)
    {
        return serviceTransferNeeds.get(serviceId);
    }
}
