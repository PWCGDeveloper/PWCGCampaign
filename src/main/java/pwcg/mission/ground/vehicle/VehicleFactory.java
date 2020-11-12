package pwcg.mission.ground.vehicle;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class VehicleFactory
{
    public static IVehicle createVehicle(ICountry country, Date date, VehicleClass vehicleClass) throws PWCGException
    {
        VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(country.getCountry(), date, vehicleClass);
        IVehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionForRequest(requestDefinition);
        IVehicle vehicle = createVehicleFromDefinition(country, date, vehicleDefinition);
        return vehicle;
    }

    public static IVehicle createVehicleFromDefinition(ICountry country, Date date, IVehicleDefinition vehicleDefinition) throws PWCGException
    {
        IVehicle vehicle = new Vehicle(vehicleDefinition);
        vehicle.makeVehicleFromDefinition(country);
        return vehicle;
    }

    public static TrainLocomotive createLocomotive(ICountry country, Date date) throws PWCGException
    {
        VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(country.getCountry(), date, VehicleClass.TrainLocomotive);
        IVehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionForRequest(requestDefinition);
        TrainLocomotive locomotive = new TrainLocomotive(vehicleDefinition);
        locomotive.makeVehicleFromDefinition(country);
        return locomotive;
    }
}
