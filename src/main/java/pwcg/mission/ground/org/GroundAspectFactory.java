package pwcg.mission.ground.org;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.AttackAreaType;

public class GroundAspectFactory
{
    public static IGroundAspect createGroundAspectAreaFire(Coordinate targetPosition, IVehicle vehicle, AttackAreaType attackAreaType, int attackAreaDistance) throws PWCGException
    {
        GroundAspectAreaFire element = new GroundAspectAreaFire(targetPosition, vehicle, attackAreaType, attackAreaDistance);
        element.createGroundUnitAspect();
        return element;
    }

    public static IGroundAspect createGroundAspectDirectFire(IVehicle vehicle) throws PWCGException
    {
        GroundAspectDirectFire element = new GroundAspectDirectFire(vehicle);
        element.createGroundUnitAspect();
        return element;
    }

    public static IGroundAspect createGroundAspectMovement(IVehicle vehicle, int unitSpeed, Coordinate destination) throws PWCGException
    {
        GroundAspectMovement element = new GroundAspectMovement(vehicle, unitSpeed, destination);
        element.createGroundUnitAspect();
        return element;
    }

}
