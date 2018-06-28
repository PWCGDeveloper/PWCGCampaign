package pwcg.campaign.factory;

import java.util.Date;

import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.airfield.RoFAirfieldObjectSelector;
import pwcg.campaign.ww2.airfield.BoSAirfieldObjectSelector;

public class AirfieldObjectSelectorFactory
{
    public static IAirfieldObjectSelector createAirfieldObjectSelector(Date date)
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFAirfieldObjectSelector(date);
        }
        else
        {
            return new BoSAirfieldObjectSelector(date);
        }
    }
}
