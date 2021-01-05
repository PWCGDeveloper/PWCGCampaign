package pwcg.mission.ground.building;

public class BuildingSearchCriteria
{
    private String searchValue;
    private BuildingSearchMethod searchMethod;
    
    BuildingSearchCriteria (String searchValue, BuildingSearchMethod searchMethod)
    {
        this.searchValue = searchValue;
        this.searchMethod = searchMethod;
    }

    public String getSearchValue()
    {
        return searchValue;
    }

    public BuildingSearchMethod getSearchMethod()
    {
        return searchMethod;
    }
}
