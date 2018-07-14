package pwcg.campaign.target;

public enum TacticalTarget
{
    TARGET_NONE("whatever you see", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_ASSAULT("assaulting troops", TargetCategory.TARGET_CATEGORY_ARMORED),
    TARGET_DEFENSE("defending troops", TargetCategory.TARGET_CATEGORY_ARMORED),
    TARGET_INFANTRY("infantry", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_TRANSPORT("transport units", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_ARTILLERY("artillery battery", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_TRAIN("train", TargetCategory.TARGET_CATEGORY_ARMORED),
    TARGET_TROOP_CONCENTRATION("troop concentration", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_AIRFIELD("airfield", TargetCategory.TARGET_CATEGORY_MEDIUM),
    TARGET_BALLOON("balloon", TargetCategory.TARGET_CATEGORY_SOFT),
    TARGET_SHIPPING("shipping", TargetCategory.TARGET_CATEGORY_HEAVY),
    TARGET_DRIFTER("transport barges", TargetCategory.TARGET_CATEGORY_MEDIUM),
    TARGET_FACTORY("factory", TargetCategory.TARGET_CATEGORY_STRATEGIC),
    TARGET_CITY("city", TargetCategory.TARGET_CATEGORY_STRATEGIC), 
    TARGET_PORT("port facility", TargetCategory.TARGET_CATEGORY_STRATEGIC),
    TARGET_RAIL("railway facility", TargetCategory.TARGET_CATEGORY_STRATEGIC);
    
    private String targetName;
    private TargetCategory targetCategory;
    
    private TacticalTarget(String targetName, TargetCategory targetCategory)
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
