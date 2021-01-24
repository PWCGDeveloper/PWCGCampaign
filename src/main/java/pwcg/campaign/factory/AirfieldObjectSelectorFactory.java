package pwcg.campaign.factory;

import java.util.Date;

import pwcg.campaign.group.airfield.staticobject.AirfieldObjectSelector;

public class AirfieldObjectSelectorFactory
{
    public static AirfieldObjectSelector createAirfieldObjectSelector(Date date)
    {
        return new AirfieldObjectSelector(date);
    }
}
