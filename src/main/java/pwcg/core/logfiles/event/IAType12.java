package pwcg.core.logfiles.event;

import pwcg.campaign.api.ICountry;
import pwcg.core.location.Coordinate;

public interface IAType12 extends IATypeBase
{
    String getId();
    String getName();
    String getType();
    ICountry getCountry();
    String getPid();
    Coordinate getPosition();
}