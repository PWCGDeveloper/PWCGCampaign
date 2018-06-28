package pwcg.aar.inmission.phase1.parse.event;

import pwcg.core.location.Coordinate;

public interface IAType3 extends IATypeBase
{
    String getVictor();
    String getVictim();
    Coordinate getLocation();
}