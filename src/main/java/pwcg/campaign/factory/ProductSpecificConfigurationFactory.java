package pwcg.campaign.factory;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.config.BoSProductSpecificConfiguration;
import pwcg.product.fc.config.FCProductSpecificConfiguration;

public class ProductSpecificConfigurationFactory
{
    
    public static IProductSpecificConfiguration createProductSpecificConfiguration()
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCProductSpecificConfiguration();
        }
        else
        {
            return new BoSProductSpecificConfiguration();
        }
    }
}
