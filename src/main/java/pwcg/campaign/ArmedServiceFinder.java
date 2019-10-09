package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;

public class ArmedServiceFinder
{
    private Campaign campaign;
    
    public ArmedServiceFinder(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public List<ArmedService> getArmedServices() throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COMPETITIVE)
        {
            return getArmedServicesAllSides();
        }
        else
        {
            return getArmedServicesSameSide();
        }
    }
    
    private List<ArmedService> getArmedServicesSameSide() throws PWCGException
    {
        List<ArmedService> servicesForMap = new ArrayList<>();
        PWCGMap map = PWCGContext.getInstance().getCurrentMap();
        List<ArmedService> allServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
        for (ArmedService armedService : allServices)
        {
            if (map.isMapHasService(armedService.getServiceId()))
            {
                if (armedService.getCountry().getSide() == campaign.determineCampaignSide())
                {
                    servicesForMap.add(armedService);
                }
            }
        }
        
        return servicesForMap;
    }

    private List<ArmedService> getArmedServicesAllSides() throws PWCGException
    {
        List<ArmedService> servicesForMap = new ArrayList<>();
        PWCGMap map = PWCGContext.getInstance().getCurrentMap();
        List<ArmedService> allServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
        for (ArmedService armedService : allServices)
        {
            if (map.isMapHasService(armedService.getServiceId()))
            {
                servicesForMap.add(armedService);
            }
        }
        
        return servicesForMap;
    }

}