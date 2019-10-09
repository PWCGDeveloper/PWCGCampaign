package pwcg.campaign.factory;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAssaultGenerator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.rof.assault.RoFAssaultGenerator;
import pwcg.product.bos.ground.assault.BoSAssaultGenerator;
import pwcg.mission.Mission;

public class AssaultGeneratorFactory
{
    public static IAssaultGenerator createAssaultGenerator(Campaign campaign, Mission mission, Date date)
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFAssaultGenerator(campaign, mission, date);
        }
        else
        {
            return new BoSAssaultGenerator(campaign, mission, date);
        }
    }
}
