package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;

public class ArmedServiceFinder
{    
    public ArmedServiceFinder()
    {
    }
    
    public static  List<ArmedService> getArmedServicesAllSides() throws PWCGException
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

    public static List<ArmedService> getArmedServicesForCampaign(Campaign campaign) throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COOP)
        {
            return getArmedServicesForSameSide(campaign);
        }
        else
        {
            return getArmedServicesForMap(campaign);
        }
    }
    
    public static List<ArmedService> getArmedServicesForMap(Campaign campaign) throws PWCGException
    {
        List<ArmedService> servicesForMap = new ArrayList<>();
        PWCGMap map = PWCGContext.getInstance().getCurrentMap();
        List<ArmedService> allServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
        for (ArmedService armedService : allServices)
        {
            if (map.isMapHasService(armedService.getServiceId()))
            {
                if (armedService.isActive(campaign.getDate()))
                {
                    servicesForMap.add(armedService);
                }
            }
        }

        return servicesForMap;
    }
    
    public static List<ArmedService> getArmedServicesForSameSide(Campaign campaign) throws PWCGException
    {
        List<ArmedService> servicesForMap = new ArrayList<>();
        PWCGMap map = PWCGContext.getInstance().getCurrentMap();
        List<ArmedService> allServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
        for (ArmedService armedService : allServices)
        {
            if (map.isMapHasService(armedService.getServiceId()))
            {
                if (armedService.getCountry().getSide() == campaign.determineCampaignSide() || campaign.determineCampaignSide() == Side.NEUTRAL)
                {
                    if (armedService.isActive(campaign.getDate()))
                    {
                        servicesForMap.add(armedService);
                    }
                }
            }
        }

        return servicesForMap;
    }
}