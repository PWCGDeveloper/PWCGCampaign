package pwcg.aar.outofmission.phase2.transfer;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;

public class TransferNeedBuilder
{
    private Campaign campaign;
    private Map<Integer, ServiceTransferNeed> serviceTransferNeeds = new HashMap<>();

    public TransferNeedBuilder (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void initialize() throws PWCGException
    {
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        for (ArmedService armedService : serviceManager.getAllArmedServices())
        {
            ServiceTransferNeed serviceTransferNeed = new ServiceTransferNeed(campaign, armedService.getServiceId());
            serviceTransferNeed.determineTransferNeed();
            serviceTransferNeeds.put(armedService.getServiceId(), serviceTransferNeed);
        }
    }
    
    public ServiceTransferNeed getServiceTransferNeed(int serviceId)
    {
        return serviceTransferNeeds.get(serviceId);
    }
}
