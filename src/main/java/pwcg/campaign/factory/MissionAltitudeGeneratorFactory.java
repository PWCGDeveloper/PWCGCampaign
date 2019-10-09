package pwcg.campaign.factory;

import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.config.BoSMissionAltitudeGenerator;
import pwcg.product.fc.config.FCMissionAltitudeGenerator;
import pwcg.product.rof.config.RoFMissionAltitudeGenerator;

public class MissionAltitudeGeneratorFactory
{
    public static IMissionAltitudeGenerator createMissionAltitudeGenerator()
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFMissionAltitudeGenerator();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCMissionAltitudeGenerator();
        }
        else
        {
            return new BoSMissionAltitudeGenerator();
        }
    }
}
