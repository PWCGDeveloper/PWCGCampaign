package pwcg.aar.inmission.phase1.parse.event;

import pwcg.core.location.Coordinate;

public interface IAType2 extends IATypeBase
{
    String getVictor();
    String getVictim();
    double getDamageLevel();
    Coordinate getLocation();
}