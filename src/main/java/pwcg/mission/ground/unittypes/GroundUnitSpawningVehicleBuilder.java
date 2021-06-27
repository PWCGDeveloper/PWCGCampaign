package pwcg.mission.ground.unittypes;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleFactory;

public class GroundUnitSpawningVehicleBuilder
{

    public static IVehicle createVehicleToSpawnFromDefinition(GroundUnitInformation pwcgGroundUnitInformation, VehicleClass vehicleClass, VehicleDefinition vehicleDefinition) throws PWCGException
    {
        IVehicle spawningVehicle = VehicleFactory.createVehicleFromDefinition(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), vehicleDefinition);
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.setCountry(pwcgGroundUnitInformation.getCountry());
        spawningVehicle.populateEntity();

        return spawningVehicle;
    }

    public static IVehicle createVehicleToSpawn(GroundUnitInformation pwcgGroundUnitInformation, VehicleClass vehicleClass) throws PWCGException
    {
        IVehicle spawningVehicle = VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), vehicleClass);
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.setCountry(pwcgGroundUnitInformation.getCountry());
        spawningVehicle.populateEntity();
        
        return spawningVehicle;
    }

    public static IVehicle getRequestedVehicle(GroundUnitInformation pwcgGroundUnitInformation) throws PWCGException
    {
        IVehicle spawningVehicle = VehicleFactory.createSpecificVehicle(pwcgGroundUnitInformation);
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.setCountry(pwcgGroundUnitInformation.getCountry());
        spawningVehicle.populateEntity();
        return spawningVehicle;
    }
}
