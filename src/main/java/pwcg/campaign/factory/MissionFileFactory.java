package pwcg.campaign.factory;

import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.mission.Mission;
import pwcg.product.bos.io.BoSMissionFile;
import pwcg.product.fc.io.FCMissionFile;
import pwcg.product.rof.io.RoFMissionFile;

public class MissionFileFactory
{
    public static IMissionFile createMissionFile(Mission mission)
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFMissionFile(mission);
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCMissionFile(mission);
        }
        else
        {
            return new BoSMissionFile(mission);
        }
    }
}
