package pwcg.campaign.factory;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAssaultGenerator;
import pwcg.mission.Mission;
import pwcg.product.bos.ground.assault.BoSAssaultGenerator;

public class AssaultGeneratorFactory
{
    public static IAssaultGenerator createAssaultGenerator(Campaign campaign, Mission mission, Date date)
    {
        return new BoSAssaultGenerator(campaign, mission, date);
    }
}
