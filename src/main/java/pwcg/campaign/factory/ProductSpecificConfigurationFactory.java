package pwcg.campaign.factory;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.product.bos.config.BoSProductSpecificConfiguration;

public class ProductSpecificConfigurationFactory
{
    
    public static IProductSpecificConfiguration createProductSpecificConfiguration()
    {
        return new BoSProductSpecificConfiguration();
    }
}
