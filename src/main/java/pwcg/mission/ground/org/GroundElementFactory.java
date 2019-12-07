package pwcg.mission.ground.org;

import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.IVehicle;

public class GroundElementFactory
{
    public static IGroundElement createGroundElementAreaFire(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle, int attackAreaDistance)
    {
        GroundElementAreaFire element = new GroundElementAreaFire(pwcgGroundUnitInformation, vehicle, attackAreaDistance);
        element.createGroundUnitElement();
        return element;
    }

    public static IGroundElement createGroundElementDirectFire(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle)
    {
        GroundElementDirectFire element = new GroundElementDirectFire(pwcgGroundUnitInformation, vehicle);
        element.createGroundUnitElement();
        return element;
    }

    public static IGroundElement createGroundElementMovement(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle, int unitSpeed)
    {
        GroundElementMovement element = new GroundElementMovement(pwcgGroundUnitInformation, vehicle, unitSpeed);
        element.createGroundUnitElement();
        return element;
    }

}
