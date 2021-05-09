package pwcg.mission.ground.building;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.mission.target.TargetType;

public enum PwcgStructure
{
    CHURCH ("church", TargetType.TARGET_NONE, 
            new BuildingSearchCriteria("church_", BuildingSearchMethod.SEARCH_BEGINS)),
    BRIDGE ("bridge", TargetType.TARGET_BRIDGE, new BuildingSearchCriteria("bridge", BuildingSearchMethod.SEARCH_CONTAINS), 
            new BuildingSearchCriteria("br_", BuildingSearchMethod.SEARCH_BEGINS)),
    RAILWAY ("railway facility", TargetType.TARGET_RAIL, new BuildingSearchCriteria("rw", BuildingSearchMethod.SEARCH_BEGINS), 
            new BuildingSearchCriteria("railroad", BuildingSearchMethod.SEARCH_CONTAINS)),
    HANGAR ("hangar", TargetType.TARGET_AIRFIELD, new BuildingSearchCriteria("hangar", BuildingSearchMethod.SEARCH_CONTAINS)),
    TOWER ("airfield tower", TargetType.TARGET_AIRFIELD, new BuildingSearchCriteria("tower", BuildingSearchMethod.SEARCH_CONTAINS)),
    AIRFIELD_ARF ("airfield facility", TargetType.TARGET_AIRFIELD, new BuildingSearchCriteria("arf_", BuildingSearchMethod.SEARCH_BEGINS)),
    AIRFIELD_AF ("airfield structure", TargetType.TARGET_AIRFIELD, new BuildingSearchCriteria("af_", BuildingSearchMethod.SEARCH_BEGINS)),
    PORT_FACILITY ("port facility", TargetType.TARGET_PORT, new BuildingSearchCriteria("port_", BuildingSearchMethod.SEARCH_BEGINS)),
    DEPOT ("depot", TargetType.TARGET_DEPOT, new BuildingSearchCriteria("warehouse", BuildingSearchMethod.SEARCH_CONTAINS)),
    FUEL ("fuel depot", TargetType.TARGET_NONE, new BuildingSearchCriteria("industrial_block_fuel", BuildingSearchMethod.SEARCH_BEGINS)),
    INDUSTRIAL ("industrial facility", TargetType.TARGET_FACTORY, new BuildingSearchCriteria("industrial_", BuildingSearchMethod.SEARCH_BEGINS)),
    CITY ("town", TargetType.TARGET_CITY, new BuildingSearchCriteria("town", BuildingSearchMethod.SEARCH_BEGINS)),
    STATIC_VEHICLE ("vehicle", TargetType.TARGET_FUEL, new BuildingSearchCriteria("static_", BuildingSearchMethod.SEARCH_BEGINS)),
    UNKNOWN ("some random building", TargetType.TARGET_NONE, new BuildingSearchCriteria("", BuildingSearchMethod.SEARCH_NONE));

    private String description;
    private List<BuildingSearchCriteria> searchCriteria = new ArrayList<>();
    private TargetType targetType = TargetType.TARGET_NONE;
    
    PwcgStructure (String description, TargetType targetType, BuildingSearchCriteria ... searchCriteriaArray)
    {
        this.description = description;
        this.targetType = targetType;
        for (BuildingSearchCriteria searchCriteriaElement : searchCriteriaArray)
        {
            this.searchCriteria.add(searchCriteriaElement);
        }
    }

    public String getDescription()
    {
        return description;
    }

    public List<BuildingSearchCriteria> getSearchCriteria()
    {
        return searchCriteria;
    }
    
    public static List<PwcgStructure> getAllBuildings()
    {
        PwcgStructure[] allBuildings = PwcgStructure.class.getEnumConstants();
        return Arrays.asList(allBuildings);
    }
    
    public boolean matches (String buildingidentifier)
    {
        for (BuildingSearchCriteria buildingSearchCriteria : searchCriteria)
        {
            if (buildingSearchCriteria.getSearchMethod() == BuildingSearchMethod.SEARCH_BEGINS)
            {
                if (buildingidentifier.startsWith(buildingSearchCriteria.getSearchValue()))
                {
                    return true;
                }
            }
        }
        
        for (BuildingSearchCriteria buildingSearchCriteria : searchCriteria)
        {
            if (buildingSearchCriteria.getSearchMethod() == BuildingSearchMethod.SEARCH_CONTAINS)
            {
                if (buildingidentifier.contains(buildingSearchCriteria.getSearchValue()))
                {
                    return true;
                }
            }
        }
        
        return false;
    }

    public TargetType toTargetType()
    {
        return targetType;
    }
}
