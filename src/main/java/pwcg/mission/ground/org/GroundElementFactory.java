package pwcg.mission.ground.org;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.AttackAreaType;

public class GroundElementFactory
{
    public static IGroundElement createGroundElementAreaFire(GroundUnitInformation pwcgGroundUnitInformation, Coordinate targetPosition, IVehicle vehicle, AttackAreaType attackAreaType, int attackAreaDistance) throws PWCGException
    {
        GroundElementAreaFire element = new GroundElementAreaFire(pwcgGroundUnitInformation, targetPosition, vehicle, attackAreaType, attackAreaDistance);
        element.createGroundUnitElement();
        return element;
    }

    public static IGroundElement createGroundElementDirectFire(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle) throws PWCGException
    {
        GroundElementDirectFire element = new GroundElementDirectFire(pwcgGroundUnitInformation, vehicle);
        element.createGroundUnitElement();
        return element;
    }

    public static IGroundElement createGroundElementMovement(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle, int unitSpeed) throws PWCGException
    {
        GroundElementMovement element = new GroundElementMovement(pwcgGroundUnitInformation, vehicle, unitSpeed);
        element.createGroundUnitElement();
        return element;
    }

}
