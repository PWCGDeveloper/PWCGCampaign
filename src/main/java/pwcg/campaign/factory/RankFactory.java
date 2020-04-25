package pwcg.campaign.factory;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.country.BoSRank;
import pwcg.product.fc.country.FCRank;

public class RankFactory
{
    // One instance for each of BoS/FC, as tests can run both
    private static IRankHelper bosHelper;
    private static IRankHelper fcHelper;

    public static IRankHelper createRankHelper()
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            if (fcHelper == null)
            {
                fcHelper = new FCRank();
            }
            return fcHelper;
        }
        else
        {
            if (bosHelper == null)
            {
                bosHelper = new BoSRank();
            }
            return bosHelper;
        }
    }
}
