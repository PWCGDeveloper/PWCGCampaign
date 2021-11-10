package pwcg.core.logfiles.event;

import pwcg.core.location.Coordinate;

public interface IAType18 extends IATypeBase
{
    String getBotId();
    String getVehicleId();
    Coordinate getLocation();
}