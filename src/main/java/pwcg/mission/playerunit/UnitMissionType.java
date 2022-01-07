package pwcg.mission.playerunit;

import java.util.ArrayList;
import java.util.List;

public enum UnitMissionType
{
    ASSAULT(UnitMissionTypeCategory.TANK), 
    DEFENSE(UnitMissionTypeCategory.TANK), 
    INFANTRY_SUPPORT(UnitMissionTypeCategory.ASSAULT_GUN), 
    AAA(UnitMissionTypeCategory.AAA), 
    ANY(UnitMissionTypeCategory.INVALID);

    UnitMissionTypeCategory category = UnitMissionTypeCategory.INVALID;

    private UnitMissionType(UnitMissionTypeCategory category)
    {
        this.category = category;
    }

    public boolean isCategory(UnitMissionTypeCategory categoryToFind)
    {
        if (category == categoryToFind)
        {
            return true;
        }
        return false;
    }

    public static List<UnitMissionType> getUnitTypesByCategory(UnitMissionTypeCategory categoryToFind)
    {
        List<UnitMissionType> unitTypesByCategory = new ArrayList<>();
        for (UnitMissionType unitType : UnitMissionType.values())
        {
            if (unitType.category == categoryToFind)
            {
                unitTypesByCategory.add(unitType);
            }
        }
        return unitTypesByCategory;
    }
}
