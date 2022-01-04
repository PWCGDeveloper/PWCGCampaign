package pwcg.campaign.tank;

public enum PwcgRoleCategory
{
    FIGHTER("Fighter"),
    ATTACK("Attack"),
    BOMBER("Bomber"),
    RECON("Recon"), 
    TRANSPORT("Transport"),
    BALLOON("Balloon"),
    GROUND_UNIT("Ground Unit"),
    
    MAIN_BATTLE_TANK("Tank Unit"),
    TANK_DESTROYER("Tank Destroyer Unit"),
    SELF_PROPELLED_GUN("Self Propelled Gun Unit"),
    SELF_PROPELLED_AAA("Self Propelled AAA Unit"),

    OTHER("other");

    private String roleCategoryDescription;
    
    PwcgRoleCategory (String roleCategoryDescription) 
    {
        this.roleCategoryDescription = roleCategoryDescription;
    }

    public String getRoleCategoryDescription()
    {
        return roleCategoryDescription;
    }
    
    public static PwcgRoleCategory getRoleCategoryFromDescription(String description)
    {
        for (PwcgRoleCategory roleCategory : PwcgRoleCategory.values())
        {
            if (roleCategory.getRoleCategoryDescription().equals(description))
            {
                return roleCategory;
            }
        }
        return PwcgRoleCategory.OTHER;
    }
}
