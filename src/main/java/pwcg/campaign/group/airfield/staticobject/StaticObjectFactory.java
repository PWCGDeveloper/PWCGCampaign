package pwcg.campaign.group.airfield.staticobject;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleRequestDefinition;

public class StaticObjectFactory
{
    public static IVehicle createStaticObject(ICountry country, Date date, VehicleClass vehicleClass) throws PWCGException
    {
        VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(country.getCountry(), date, vehicleClass);
        VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getStaticObjectDefinitionManager().getVehicleDefinitionForRequest(requestDefinition);
        IVehicle vehicle = new StaticObject(vehicleDefinition);
        vehicle.makeVehicleFromDefinition(country);
        return vehicle;
    }
}
