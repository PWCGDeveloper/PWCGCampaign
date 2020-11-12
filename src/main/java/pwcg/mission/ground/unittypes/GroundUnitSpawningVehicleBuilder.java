package pwcg.mission.ground.unittypes;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundUnitSpawningVehicleBuilder
{

    public static IVehicle createVehicleToSpawnFromDefinition(GroundUnitInformation pwcgGroundUnitInformation, VehicleClass vehicleClass, IVehicleDefinition vehicleDefinition) throws PWCGException
    {
        IVehicle spawningVehicle;
        spawningVehicle = pwcg.mission.ground.vehicle.VehicleFactory.createVehicleFromDefinition(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), vehicleDefinition);
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.setCountry(pwcgGroundUnitInformation.getCountry());
        spawningVehicle.populateEntity();
                
        determineEngageable(spawningVehicle, vehicleClass);

        return spawningVehicle;
    }

    public static IVehicle createVehicleToSpawn(GroundUnitInformation pwcgGroundUnitInformation, VehicleClass vehicleClass) throws PWCGException
    {
        IVehicle spawningVehicle;
        spawningVehicle = pwcg.mission.ground.vehicle.VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), vehicleClass);
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.setCountry(pwcgGroundUnitInformation.getCountry());
        spawningVehicle.populateEntity();
        
        determineEngageable(spawningVehicle, vehicleClass);
        
        return spawningVehicle;
    }
    
    private static void determineEngageable(IVehicle spawningVehicle, VehicleClass vehicleClass)
    {
        if (vehicleClass == VehicleClass.AAAArtillery || vehicleClass == VehicleClass.AAAMachineGun)
        {
            spawningVehicle.setEngageable(0);
        }
    }
}
