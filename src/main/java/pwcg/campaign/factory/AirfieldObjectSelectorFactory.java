package pwcg.campaign.factory;

import java.util.Date;

import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.group.airfield.staticobject.AirfieldObjectSelector;

public class AirfieldObjectSelectorFactory
{
    public static IAirfieldObjectSelector createAirfieldObjectSelector(Date date)
    {
        return new AirfieldObjectSelector(date);
    }
}
