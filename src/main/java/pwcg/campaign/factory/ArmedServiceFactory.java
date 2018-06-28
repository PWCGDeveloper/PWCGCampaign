package pwcg.campaign.factory;

import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.country.RoFServiceManager;
import pwcg.campaign.ww2.country.BoSServiceManager;

public class ArmedServiceFactory
{
	static IArmedServiceManager serviceManager;
    public static IArmedServiceManager createServiceManager()
    {
        if (PWCGContextManager.isRoF())
        {
            serviceManager = RoFServiceManager.getInstance();
        }
        else
        {
            serviceManager = BoSServiceManager.getInstance();
        }
        
        return serviceManager;
    }

}
