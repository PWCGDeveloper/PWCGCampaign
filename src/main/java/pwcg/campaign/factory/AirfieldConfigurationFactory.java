package pwcg.campaign.factory;

import pwcg.campaign.api.IAirfieldConfiguration;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.airfield.RoFAirfieldConfiguration;
import pwcg.campaign.ww2.airfield.BoSAirfieldConfiguration;

public class AirfieldConfigurationFactory
{
    public static IAirfieldConfiguration createAirfieldConfiguration()
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFAirfieldConfiguration();
        }
        else
        {
            return new BoSAirfieldConfiguration();
        }
    }
}
