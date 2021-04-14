package pwcg.mission.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.io.json.VehicleDefinitionIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;

public class VehicleSetBuilderComprehensive
{
    private List<IVehicle> allVehicles = new ArrayList<>();

    public List<IVehicle> makeOneOfEachType() throws PWCGException
    {
        List< VehicleDefinition> allVehicleDefinitions = VehicleDefinitionIOJson.readJson();
        for (VehicleDefinition vehicleDefinition : allVehicleDefinitions)
        {
            IVehicle vehicle = new Vehicle(vehicleDefinition);
            allVehicles.add(vehicle);
        }
        return allVehicles;
    }
    
    public void scatterAroundPosition(Coordinate coordinate)
    {
        for (IVehicle vehicle : allVehicles)
        {
            int xOffset = RandomNumberGenerator.getRandom(4000) - 2000;
            int zOffset = RandomNumberGenerator.getRandom(4000) - 2000;
            Coordinate unitPosition = new Coordinate(coordinate.getXPos() + xOffset, 0.0, coordinate.getZPos() + zOffset);
            vehicle.setPosition(unitPosition);
            vehicle.setOrientation(new Orientation(RandomNumberGenerator.getRandom(360)));
        }
    }
     public List<IVehicle> getAllVehicles()
    {
        return allVehicles;
    }
}
