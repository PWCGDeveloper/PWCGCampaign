package pwcg.campaign.factory;

import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.group.airfield.hotspot.AirfieldHotSpotTranslator;

public class HotSpotTranslatorFactory
{
    public static IHotSpotTranslator createHotSpotTranslatorFactory()
    {
        return new AirfieldHotSpotTranslator();
    }
}
