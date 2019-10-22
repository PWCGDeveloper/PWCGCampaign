package pwcg.campaign.factory;

import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.product.fc.country.FCServiceManager;

public class ArmedServiceFactory
{
	static IArmedServiceManager serviceManager;
    public static IArmedServiceManager createServiceManager()
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            serviceManager = FCServiceManager.getInstance();
        }
        else
        {
            serviceManager = BoSServiceManager.getInstance();
        }
        
        return serviceManager;
    }

}
