package pwcg.campaign.factory;

import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.config.BoSMissionAltitudeGenerator;
import pwcg.product.fc.config.FCMissionAltitudeGenerator;

public class MissionAltitudeGeneratorFactory
{
    public static IMissionAltitudeGenerator createMissionAltitudeGenerator()
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCMissionAltitudeGenerator();
        }
        else
        {
            return new BoSMissionAltitudeGenerator();
        }
    }
}
