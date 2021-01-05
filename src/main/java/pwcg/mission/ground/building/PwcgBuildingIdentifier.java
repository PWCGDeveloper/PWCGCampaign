package pwcg.mission.ground.building;

public class PwcgBuildingIdentifier
{
    public static PwcgBuilding identifyBuilding(String buildingidentifier)
    {        
        buildingidentifier = parseModel(buildingidentifier);
        PwcgBuilding buildingType = determineGenericBuildingType(buildingidentifier);
        buildingType = determineSpecificBuildingType(buildingType, buildingidentifier);        
        return buildingType;
    }

    private static PwcgBuilding determineSpecificBuildingType(PwcgBuilding genericBuildingType, String buildingidentifier)
    {
        for (PwcgBuilding building : PwcgBuilding.getAllBuildings())
        {
            if (building == PwcgBuilding.UNKNOWN || building == PwcgBuilding.INDUSTRIAL)
            {
                continue;
            }
            else if (building.matches(buildingidentifier))
            {
                return building;
            }
        }
        
        return genericBuildingType;
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

    private static PwcgBuilding determineGenericBuildingType(String buildingidentifier)
    {
        if (PwcgBuilding.INDUSTRIAL.matches(buildingidentifier))
        {
            return PwcgBuilding.INDUSTRIAL;
        }
        return PwcgBuilding.UNKNOWN;
    }
}
