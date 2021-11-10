package pwcg.core.logfiles.event;

import pwcg.core.location.Coordinate;

public interface IAType2 extends IATypeBase
{
    String getVictor();
    String getVictim();
    double getDamageLevel();
    Coordinate getLocation();
}