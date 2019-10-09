package pwcg.campaign.factory;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.country.BoSRank;
import pwcg.product.fc.country.FCRank;
import pwcg.product.rof.country.RoFRank;

public class RankFactory
{
    public static IRankHelper createRankHelper()
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFRank();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCRank();
        }
        else
        {
            return new BoSRank();
        }
    }
}
