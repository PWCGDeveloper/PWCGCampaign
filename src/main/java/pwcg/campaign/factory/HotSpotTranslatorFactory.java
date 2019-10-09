package pwcg.campaign.factory;

import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.airfield.BoSHotSpotTranslator;
import pwcg.product.fc.airfield.FCHotSpotTranslator;
import pwcg.product.rof.airfield.RoFHotSpotTranslator;

public class HotSpotTranslatorFactory
{
    public static IHotSpotTranslator createHotSpotTranslatorFactory()
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFHotSpotTranslator();
        }
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCHotSpotTranslator();
        }
        else
        {
            return new BoSHotSpotTranslator();
        }
    }
}
