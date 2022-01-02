package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.medals.IMedalManager;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.medals.BoSMedalManager;

public class MedalManagerFactory
{
    public static IMedalManager createMedalManager(ICountry country, Campaign campaign) throws PWCGException
    {
        return BoSMedalManager.getManager(country, campaign);
    }
}
