package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.medals.IMedalManager;
import pwcg.campaign.ww1.medals.RoFMedalManager;
import pwcg.campaign.ww2.medals.BoSMedalManager;
import pwcg.core.exception.PWCGException;

public class MedalManagerFactory
{
    public static IMedalManager createMedalManager(Campaign campaign) throws PWCGException
    {
        if (PWCGContextManager.isRoF())
        {
            return RoFMedalManager.getManager(campaign);
        }
        else
        {
            return BoSMedalManager.getManager(campaign);
        }
    }
}
