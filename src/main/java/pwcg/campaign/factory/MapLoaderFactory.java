package pwcg.campaign.factory;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.map.BoSMapLoader;
import pwcg.product.fc.map.FCMapLoader;

public class MapLoaderFactory
{
    public static Runnable createMapLoader()
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCMapLoader();
        }
        else
        {
            return new BoSMapLoader();
        }
    }
}
