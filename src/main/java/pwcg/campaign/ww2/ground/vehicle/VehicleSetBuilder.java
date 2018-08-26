package pwcg.campaign.ww2.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ww1.ground.vehicle.VehicleDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.IVehicle;

public class VehicleSetBuilder<T extends IVehicle>
{
    private Class<T> vehicleInstanceClass;

    public VehicleSetBuilder(Class<T> clazz) 
    {
        this.vehicleInstanceClass = clazz;
    }
    
    public List<IVehicle> makeVehicles() throws PWCGException
    {
        List<IVehicle> vehicles = new ArrayList<>();
        try
        {
            T vehicleReferenceInstance = vehicleInstanceClass.newInstance();
            List<VehicleDefinition> vehicleDefinitions = vehicleReferenceInstance.getAllVehicleDefinitions();
            for (VehicleDefinition vehicleDefinition : vehicleDefinitions)
            {
                T vehicleInstance = vehicleInstanceClass.newInstance();
                vehicleInstance.makeVehicleFromDefinition(vehicleDefinition);
                System.out.println(vehicleInstance.getDescription());
                vehicles.add(vehicleInstance);
            }
        }
        catch (Exception e)
        {
            throw new PWCGException("Failed to create vehicle for class " + vehicleInstanceClass.getName());
        }

        return vehicles;
    }
}
