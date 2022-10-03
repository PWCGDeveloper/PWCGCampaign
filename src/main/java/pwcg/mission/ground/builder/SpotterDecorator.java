package pwcg.mission.ground.builder;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.vehicle.IVehicle;

public class SpotterDecorator
{
    public static void createSpotter(GroundUnitCollection spotter, int spotterRange) throws PWCGException
    {
        if (spotter != null)
        {
            IGroundUnit groundUnit = spotter.getGroundUnits().get(0);
            IVehicle vehicle = groundUnit.getVehicles().get(0);
            vehicle.setSpotterRange(spotterRange);
        }
    }

}
