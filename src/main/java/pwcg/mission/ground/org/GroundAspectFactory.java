package pwcg.mission.ground.org;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.AttackAreaType;

public class GroundAspectFactory
{
    public static IGroundAspect createGroundAspectAreaFire(GroundUnitInformation pwcgGroundUnitInformation, Coordinate targetPosition, IVehicle vehicle, AttackAreaType attackAreaType, int attackAreaDistance) throws PWCGException
    {
        GroundAspectAreaFire element = new GroundAspectAreaFire(pwcgGroundUnitInformation, targetPosition, vehicle, attackAreaType, attackAreaDistance);
        element.createGroundUnitAspect();
        return element;
    }

    public static IGroundAspect createGroundAspectDirectFire(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle) throws PWCGException
    {
        GroundAspectDirectFire element = new GroundAspectDirectFire(pwcgGroundUnitInformation, vehicle);
        element.createGroundUnitAspect();
        return element;
    }

    public static IGroundAspect createGroundAspectMovement(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle, int unitSpeed) throws PWCGException
    {
        GroundAspectMovement element = new GroundAspectMovement(pwcgGroundUnitInformation, vehicle, unitSpeed);
        element.createGroundUnitAspect();
        return element;
    }

}
