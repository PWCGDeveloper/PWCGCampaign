package pwcg.mission.ground.vehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
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
    
    public VehicleDefinition getVehicleDefinitionByVehicleType(String vehicleType) throws PWCGException
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
    
    public VehicleDefinition getVehicleDefinitionByVehicleName(String vehicleName) throws PWCGException
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
    
    public VehicleDefinition getVehicleDefinitionByVehicleDisplayName(String vehicleName) throws PWCGException
    {
        for (VehicleDefinition definition : allVehiclesDefinitions)
        {
            if (definition.getDisplayName().equals(vehicleName))
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
        
    public VehicleDefinition getVehicleDefinitionForRequest(VehicleRequestDefinition requestDefinition) throws PWCGException
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

    private VehicleDefinition chooseMatchingVehicle(List<VehicleDefinition> matchingDefinitions)
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
    
    
    public List<VehicleDefinition> getPlayerVehicleDefinitionsOfTypeForCountries(VehicleClass vehicleClass, Set<Country> countries, Date battleDate) throws PWCGException
    {
        List<VehicleDefinition> playerVehiclesOfType = new ArrayList<>();
        List<VehicleDefinition> vehiclesOfType = getVehicleDefinitionsOfTypeForCountries(vehicleClass, countries, battleDate);
        for (VehicleDefinition vehicleDefinition : vehiclesOfType)
        {
            if (vehicleDefinition.isPlayerDrivable())
            {
                playerVehiclesOfType.add(vehicleDefinition);
            }
        }
        return playerVehiclesOfType;
    }
    
    public List<VehicleDefinition> getNonPlayerVehicleDefinitionsOfTypeForCountries(VehicleClass vehicleClass, Set<Country> countries, Date battleDate) throws PWCGException
    {
        List<VehicleDefinition> nonPlayerVehiclesOfType = new ArrayList<>();
        List<VehicleDefinition> vehiclesOfType = getVehicleDefinitionsOfTypeForCountries(vehicleClass, countries, battleDate);
        for (VehicleDefinition vehicleDefinition : vehiclesOfType)
        {
            if (!vehicleDefinition.isPlayerDrivable())
            {
                nonPlayerVehiclesOfType.add(vehicleDefinition);
            }
        }
        return nonPlayerVehiclesOfType;
    }

    private List<VehicleDefinition> getVehicleDefinitionsOfTypeForCountries(VehicleClass vehicleClass, Set<Country> countries, Date battleDate) throws PWCGException
    {
        Map<String, VehicleDefinition> matchingVehiclesAllied = new TreeMap<>();
        Map<String, VehicleDefinition> matchingVehiclesAxis = new TreeMap<>();
        for (Country country : countries)
        {
            VehicleRequestDefinition vehicleRequestDefinition = new VehicleRequestDefinition(country, battleDate, vehicleClass);
            for (VehicleDefinition vehicleDefinition : getAllVehicleDefinitions())
            {
                if (vehicleDefinition != null && vehicleDefinition.shouldUse(vehicleRequestDefinition))
                {
                    ICountry icountry = CountryFactory.makeCountryByCountry(country);
                    if (icountry.getSide() == Side.ALLIED)
                    {
                        matchingVehiclesAllied.put(vehicleDefinition.getVehicleName(), vehicleDefinition);
                    }
                    else
                    {
                        matchingVehiclesAxis.put(vehicleDefinition.getVehicleName(), vehicleDefinition);
                    }
                }
            }
        }
        
        List<VehicleDefinition> matchingVehicles = new ArrayList<>();
        matchingVehicles.addAll(matchingVehiclesAllied.values());
        matchingVehicles.addAll(matchingVehiclesAxis.values());
        return matchingVehicles;
    }

}
