package pwcg.campaign.factory;

import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.airfield.RoFHotSpotTranslator;
import pwcg.campaign.ww2.airfield.BoSHotSpotTranslator;

public class HotSpotTranslatorFactory
{
    public static IHotSpotTranslator createHotSpotTranslatorFactory()
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFHotSpotTranslator();
        }
        else
        {
            return new BoSHotSpotTranslator();
        }
    }
}
