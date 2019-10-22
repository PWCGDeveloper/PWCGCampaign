package pwcg.campaign.group.airfield.staticobject;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleRequestDefinition;

public class StaticObjectFactory
{
    public static IVehicle createStaticObject(ICountry country, Date date, VehicleClass vehicleClass) throws PWCGException
    {
        VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(country.getCountry(), date, vehicleClass);
        IVehicleDefinition vehicleDefinition = StaticObjectDefinitionManager.getInstance().getVehicleDefinitionForRequest(requestDefinition);
        IVehicle vehicle = new StaticObject(vehicleDefinition);
        vehicle.makeVehicleFromDefinition(country);
        return vehicle;
    }
}
