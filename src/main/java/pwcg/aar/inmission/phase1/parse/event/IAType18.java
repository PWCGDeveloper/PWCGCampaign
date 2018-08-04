package pwcg.aar.inmission.phase1.parse.event;

import pwcg.core.location.Coordinate;

public interface IAType18 extends IATypeBase
{
    String getBotId();
    String getVehicleId();
    Coordinate getLocation();
}