package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

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
        List<ArmedService> allServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
        for (ArmedService armedService : allServices)
        {
            servicesForMap.add(armedService);
        }
        
        return servicesForMap;
    }

    public static List<ArmedService> getArmedServicesForCrewMemberCreation(Campaign campaign) throws PWCGException
    {
        return getArmedServicesForMap(campaign);
    }
    
    public static List<ArmedService> getArmedServicesForDate(Campaign campaign) throws PWCGException
    {
        List<ArmedService> servicesForDate = new ArrayList<>();
        List<ArmedService> allServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
        for (ArmedService armedService : allServices)
        {
            if (armedService.isActive(campaign.getDate()))
            {
                servicesForDate.add(armedService);
            }
        }

        return servicesForDate;
    }

    private static List<ArmedService> getArmedServicesForMap(Campaign campaign) throws PWCGException
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
}