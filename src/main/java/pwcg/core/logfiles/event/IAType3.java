package pwcg.core.logfiles.event;

import pwcg.core.location.Coordinate;

public interface IAType3 extends IATypeBase
{
    String getVictor();
    String getVictim();
    Coordinate getLocation();
}