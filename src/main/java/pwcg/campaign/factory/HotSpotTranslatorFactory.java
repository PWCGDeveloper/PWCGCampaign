package pwcg.campaign.factory;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.hotspot.AirfieldHotSpotTranslator;
import pwcg.mission.Mission;

public class HotSpotTranslatorFactory
{
    public static AirfieldHotSpotTranslator createHotSpotTranslatorFactory(Mission mission, Airfield airfield)
    {
        return new AirfieldHotSpotTranslator(mission, airfield);
    }
}
