package pwcg.campaign.factory;

import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.config.RoFMissionAltitudeGenerator;
import pwcg.campaign.ww2.config.BoSMissionAltitudeGenerator;

public class MissionAltitudeGeneratorFactory
{
    public static IMissionAltitudeGenerator createMissionAltitudeGeneratorFactory()
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFMissionAltitudeGenerator();
        }
        else
        {
            return new BoSMissionAltitudeGenerator();
        }
    }
}
