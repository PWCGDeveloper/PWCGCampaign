package pwcg.campaign.factory;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.config.RoFProductSpecificConfiguration;
import pwcg.campaign.ww2.config.BoSProductSpecificConfiguration;

public class ProductSpecificConfigurationFactory
{
    
    public static IProductSpecificConfiguration createProductSpecificConfiguration()
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFProductSpecificConfiguration();
        }
        else
        {
            return new BoSProductSpecificConfiguration();
        }
    }
}
