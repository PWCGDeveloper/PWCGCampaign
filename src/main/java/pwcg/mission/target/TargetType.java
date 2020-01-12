package pwcg.mission.target;

public enum TargetType
{
    TARGET_NONE("No Target", TargetCategory.TARGET_CATEGORY_NONE),
    TARGET_ANY("Build on the fly", TargetCategory.TARGET_CATEGORY_NONE),
    TARGET_ASSAULT("assaulting troops", TargetCategory.TARGET_CATEGORY_ARMORED),
    TARGET_DEFENSE("defending troops", TargetCategory.TARGET_CATEGORY_ARMORED),
    TARGET_INFANTRY("infantry", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_TRANSPORT("transport units", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_ARTILLERY("artillery battery", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_TRAIN("train", TargetCategory.TARGET_CATEGORY_ARMORED),
    TARGET_AAA("AAA", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_AIRFIELD("airfield", TargetCategory.TARGET_CATEGORY_MEDIUM),
    TARGET_BALLOON("balloon", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_SHIPPING("shipping", TargetCategory.TARGET_CATEGORY_HEAVY),
    TARGET_DRIFTER("transport barges", TargetCategory.TARGET_CATEGORY_MEDIUM),
    TARGET_FACTORY("factory", TargetCategory.TARGET_CATEGORY_STRATEGIC),
    TARGET_CITY("city", TargetCategory.TARGET_CATEGORY_STRATEGIC), 
    TARGET_PORT("port facility", TargetCategory.TARGET_CATEGORY_STRATEGIC),
    TARGET_RAIL("railway facility", TargetCategory.TARGET_CATEGORY_STRATEGIC),
    TARGET_AIR("aircraft", TargetCategory.TARGET_CATEGORY_AIR_TO_AIR);
    
    private String targetName;
    private TargetCategory targetCategory;
    
    private TargetType(String targetName, TargetCategory targetCategory)
    {
        this.targetName = targetName;
        this.targetCategory = targetCategory;
    }
    
    public String getTargetName()
    {
        return targetName;
    }

    public TargetCategory getTargetCategory()
    {
        return targetCategory;
    }
}
