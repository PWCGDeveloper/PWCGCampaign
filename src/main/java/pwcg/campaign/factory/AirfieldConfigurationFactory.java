package pwcg.campaign.factory;

import pwcg.campaign.api.IAirfieldConfiguration;
import pwcg.campaign.group.airfield.AirfieldConfiguration;

public class AirfieldConfigurationFactory
{
    public static IAirfieldConfiguration createAirfieldConfiguration()
    {
        return new AirfieldConfiguration();
    }
}
