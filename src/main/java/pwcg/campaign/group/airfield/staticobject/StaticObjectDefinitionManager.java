package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.io.json.StaticObjectIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.IWeight;
import pwcg.core.utils.WeightCalculator;
import pwcg.mission.ground.vehicle.IVehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleRequestDefinition;

public class StaticObjectDefinitionManager
{
    private static StaticObjectDefinitionManager instance = null;
    
    private List<VehicleDefinition> allStaticObjectsDefinitions = new ArrayList<>();

    private StaticObjectDefinitionManager()
    {
    }
    
    public static StaticObjectDefinitionManager getInstance() throws PWCGException
    {
        if (instance == null)
        {
            instance = new StaticObjectDefinitionManager();
            instance.readVehicles();
        }
        
        return instance;
    }
    
    public void readVehicles() throws PWCGException
    {
        allStaticObjectsDefinitions = StaticObjectIOJson.readJson();
    }

    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        return allStaticObjectsDefinitions;
    }
    
    
    public IVehicleDefinition getVehicleDefinitionForRequest(VehicleRequestDefinition requestDefinition) throws PWCGException
    {
        List<VehicleDefinition> matchingDefinitions = new ArrayList<>();
        for (VehicleDefinition definition : allStaticObjectsDefinitions)
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
