package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.medals.IMedalManager;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.medals.BoSMedalManager;
import pwcg.product.fc.medals.FCMedalManager;

public class MedalManagerFactory
{
    public static IMedalManager createMedalManager(ICountry country, Campaign campaign) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return FCMedalManager.getManager(country, campaign);
        }
        else
        {
            return BoSMedalManager.getManager(country, campaign);
        }
    }
}
