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
    
    public List<ArmedService> getArmedServices() throws PWCGException
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