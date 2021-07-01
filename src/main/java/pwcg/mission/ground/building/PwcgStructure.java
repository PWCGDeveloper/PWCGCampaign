package pwcg.mission.ground.building;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.core.utils.IWeight;
import pwcg.mission.target.TargetType;

public enum PwcgStructure implements IWeight
{
    CHURCH ("church", TargetType.TARGET_NONE, 0, 
            new BuildingSearchCriteria("church_", BuildingSearchMethod.SEARCH_BEGINS)),
    BRIDGE ("bridge", TargetType.TARGET_BRIDGE, 10, new BuildingSearchCriteria("bridge", BuildingSearchMethod.SEARCH_CONTAINS), 
            new BuildingSearchCriteria("br_", BuildingSearchMethod.SEARCH_BEGINS)),
    RAILWAY ("railway facility", TargetType.TARGET_RAIL, 10, new BuildingSearchCriteria("rw", BuildingSearchMethod.SEARCH_BEGINS), 
            new BuildingSearchCriteria("railroad", BuildingSearchMethod.SEARCH_CONTAINS)),
    HANGAR ("hangar", TargetType.TARGET_AIRFIELD, 4, new BuildingSearchCriteria("hangar", BuildingSearchMethod.SEARCH_CONTAINS)),
    TOWER ("airfield tower", TargetType.TARGET_AIRFIELD, 0, new BuildingSearchCriteria("tower", BuildingSearchMethod.SEARCH_CONTAINS)),
    AIRFIELD_ARF ("airfield facility", TargetType.TARGET_AIRFIELD, 3, new BuildingSearchCriteria("arf_", BuildingSearchMethod.SEARCH_BEGINS)),
    AIRFIELD_AF ("airfield structure", TargetType.TARGET_AIRFIELD, 5, new BuildingSearchCriteria("af_", BuildingSearchMethod.SEARCH_BEGINS)),
    PORT_FACILITY ("port facility", TargetType.TARGET_PORT, 2, new BuildingSearchCriteria("port_", BuildingSearchMethod.SEARCH_BEGINS)),
    DEPOT ("depot", TargetType.TARGET_DEPOT, 2, new BuildingSearchCriteria("warehouse", BuildingSearchMethod.SEARCH_CONTAINS)),
    FUEL ("fuel depot", TargetType.TARGET_NONE, 2, new BuildingSearchCriteria("industrial_block_fuel", BuildingSearchMethod.SEARCH_BEGINS)),
    INDUSTRIAL ("industrial facility", TargetType.TARGET_FACTORY, 10, new BuildingSearchCriteria("industrial_", BuildingSearchMethod.SEARCH_BEGINS)),
    CITY ("town", TargetType.TARGET_CITY, 0, new BuildingSearchCriteria("town", BuildingSearchMethod.SEARCH_BEGINS)),
    STATIC_VEHICLE ("vehicle", TargetType.TARGET_NONE, 0, new BuildingSearchCriteria("static_", BuildingSearchMethod.SEARCH_BEGINS)),
    UNKNOWN ("some random building", TargetType.TARGET_NONE, 0, new BuildingSearchCriteria("", BuildingSearchMethod.SEARCH_NONE));

    private String description;
    private List<BuildingSearchCriteria> searchCriteria = new ArrayList<>();
    private TargetType targetType = TargetType.TARGET_NONE;
    private int weight;

    PwcgStructure (String description, TargetType targetType, int weight, BuildingSearchCriteria ... searchCriteriaArray)
    {
        this.description = description;
        this.targetType = targetType;
        this.weight = weight;
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

    @Override
    public int getWeight()
    {
        return weight;
    }
}
