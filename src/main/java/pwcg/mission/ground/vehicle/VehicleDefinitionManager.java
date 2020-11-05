package pwcg.mission.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.io.json.VehicleDefinitionIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.IWeight;
import pwcg.core.utils.WeightCalculator;

public class VehicleDefinitionManager
{
    private List<VehicleDefinition> allVehiclesDefinitions = new ArrayList<>();

    public VehicleDefinitionManager()
    {
    }
    
    public void initialize() throws PWCGException
    {
        readVehicles();
    }
    
    private void readVehicles() throws PWCGException
    {
        allVehiclesDefinitions = VehicleDefinitionIOJson.readJson();
    }

    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        return allVehiclesDefinitions;
    }
    
    public IVehicleDefinition getVehicleDefinitionByVehicleType(String vehicleType) throws PWCGException
    {
        for (VehicleDefinition definition : allVehiclesDefinitions)
        {
            if (definition.getVehicleType().equals(vehicleType))
            {
                return definition;
            }
        }
        
        return null;
    }
    
    public IVehicleDefinition getVehicleDefinitionByVehicleName(String vehicleName) throws PWCGException
    {
        for (VehicleDefinition definition : allVehiclesDefinitions)
        {
            if (definition.getVehicleName().equals(vehicleName))
            {
                return definition;
            }
        }
        
        return getVehicleDefinitionByVehicleType(vehicleName);
    }
    
    public static boolean isLocomotive(String vehicleIdentifier)
    {
        if (vehicleIdentifier.toLowerCase().equals("e") || vehicleIdentifier.toLowerCase().equals("g8"))
        {
            return true;
        }
        return false;
    }
    
    public static boolean isTrainCar(String vehicleIdentifier)
    {
        if (vehicleIdentifier.toLowerCase().contentEquals("wagon"))
        {
            return true;
        }        
        return false;
    }
        
    public IVehicleDefinition getVehicleDefinitionForRequest(VehicleRequestDefinition requestDefinition) throws PWCGException
    {
        List<VehicleDefinition> matchingDefinitions = new ArrayList<>();
        for (VehicleDefinition definition : allVehiclesDefinitions)
        {
            if (definition.shouldUse(requestDefinition))
            {
                matchingDefinitions.add(definition);
            }
        }
        
        if (matchingDefinitions.size() == 0)
        {
            throw new PWCGException ("No definition found for request " + requestDefinition.toString());
        }
        
        return chooseMatchingVehicle(matchingDefinitions);
    }

    private IVehicleDefinition chooseMatchingVehicle(List<VehicleDefinition> matchingDefinitions)
    {
        List<IWeight> vehiclesByWeight = new ArrayList<>();
        for (VehicleDefinition definition : matchingDefinitions)
        {
            vehiclesByWeight.add(definition);
        }
        
        WeightCalculator weightCalculator = new WeightCalculator(vehiclesByWeight);
        int index = weightCalculator.getItemFromWeight();
        return matchingDefinitions.get(index);
    }

}
