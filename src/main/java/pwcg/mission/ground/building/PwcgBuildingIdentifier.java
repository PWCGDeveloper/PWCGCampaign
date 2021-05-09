package pwcg.mission.ground.building;

import pwcg.campaign.group.Block;

public class PwcgBuildingIdentifier
{
    public static PwcgStructure identifyBuilding(String buildingidentifier)
    {        
        buildingidentifier = parseModel(buildingidentifier);
        PwcgStructure buildingType = determineIndustrialBuildingType(buildingidentifier);
        if (buildingType == PwcgStructure.UNKNOWN)
        {
            buildingType = determineSpecificBuildingType(buildingType, buildingidentifier);
        }
        return buildingType;
    }
    
    private static String parseModel(String buildingidentifier)
    {
        String parsed = buildingidentifier;
        if (parsed.contains("\\"))
        {
            int startModelIndex = parsed.lastIndexOf("\\");
            parsed = parsed.substring(startModelIndex+1);
        }
        return parsed;
    }

    public static boolean isAirfield(Block block)
    {
        PwcgStructure pwcgStructure = identifyBuilding(block.getModel());
        if (pwcgStructure == PwcgStructure.AIRFIELD_AF || pwcgStructure == PwcgStructure.AIRFIELD_ARF || pwcgStructure == PwcgStructure.HANGAR || pwcgStructure == PwcgStructure.TOWER)
        {
            return true;
        }
        return false;
    }

    private static PwcgStructure determineIndustrialBuildingType(String buildingidentifier)
    {
        if (PwcgStructure.PORT_FACILITY.matches(buildingidentifier))
        {
            return PwcgStructure.PORT_FACILITY;
        }
        else if (PwcgStructure.DEPOT.matches(buildingidentifier))
        {
            return PwcgStructure.DEPOT;
        }
        else if (PwcgStructure.FUEL.matches(buildingidentifier))
        {
            return PwcgStructure.FUEL;
        }
        else if (PwcgStructure.INDUSTRIAL.matches(buildingidentifier))
        {
            return PwcgStructure.INDUSTRIAL;
        }
        return PwcgStructure.UNKNOWN;
    }

    private static PwcgStructure determineSpecificBuildingType(PwcgStructure genericBuildingType, String buildingidentifier)
    {
        for (PwcgStructure building : PwcgStructure.getAllBuildings())
        {
            if (building.matches(buildingidentifier))
            {
                return building;
            }
        }
        
        return genericBuildingType;
    }
}
