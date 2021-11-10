package pwcg.core.logfiles.event;

import pwcg.campaign.api.ICountry;
import pwcg.core.location.Coordinate;

public interface IAType10 extends IATypeBase
{
    String getId();
    String getType();
    ICountry getCountry();
    Coordinate getLocation();
}