package pwcg.campaign.factory;

import pwcg.campaign.api.IArmedServiceManager;
import pwcg.product.bos.country.BoSServiceManager;

public class ArmedServiceFactory
{
	static IArmedServiceManager serviceManager;
    public static IArmedServiceManager createServiceManager()
    {
        serviceManager = BoSServiceManager.getInstance();
        return serviceManager;
    }
}
