package pwcg.campaign.factory;

import pwcg.campaign.api.IRankHelper;
import pwcg.product.bos.country.BoSRank;

public class RankFactory
{
    private static IRankHelper bosHelper;

    public static IRankHelper createRankHelper()
    {
        if (bosHelper == null)
        {
            bosHelper = new BoSRank();
        }
        return bosHelper;
    }
}
