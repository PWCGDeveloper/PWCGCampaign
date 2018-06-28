package pwcg.campaign.factory;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAssaultGenerator;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.ground.assault.RoFAssaultGenerator;
import pwcg.campaign.ww2.ground.assault.BoSAssaultGenerator;
import pwcg.mission.Mission;

public class AssaultGeneratorFactory
{
    public static IAssaultGenerator createAssaultGenerator(Campaign campaign, Mission mission, Date date)
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFAssaultGenerator(campaign, mission, date);
        }
        else
        {
            return new BoSAssaultGenerator(campaign, mission, date);
        }
    }
}
