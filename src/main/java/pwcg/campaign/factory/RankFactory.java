package pwcg.campaign.factory;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.country.RoFRank;
import pwcg.campaign.ww2.country.BoSRank;

public class RankFactory
{
    public static IRankHelper createRankHelper()
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFRank();
        }
        else
        {
            return new BoSRank();
        }
    }
}
