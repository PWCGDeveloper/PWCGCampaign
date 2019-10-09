package pwcg.campaign.factory;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.map.BoSMapLoader;
import pwcg.product.rof.map.RoFMapLoader;

public class MapLoaderFactory
{
    public static Runnable createMapLoader()
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFMapLoader();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new RoFMapLoader();
        }
        else
        {
            return new BoSMapLoader();
        }
    }
}
