package pwcg.campaign.tank;

public enum PwcgRole
{
    ROLE_FIGHTER (PwcgRoleCategory.FIGHTER, "Fighter"),

    ROLE_ATTACK (PwcgRoleCategory.ATTACK, "Attack"),
    ROLE_RAIDER (PwcgRoleCategory.ATTACK, "Raider"),
    ROLE_TRAIN_BUSTER (PwcgRoleCategory.ATTACK, "Train Buster"),
    ROLE_TANK_BUSTER (PwcgRoleCategory.ATTACK, "Tank Buster"),
    ROLE_DIVE_BOMB (PwcgRoleCategory.ATTACK, "Dive Bomb"),
    ROLE_BOMB (PwcgRoleCategory.BOMBER, "Bomber"),
    ROLE_TRANSPORT (PwcgRoleCategory.TRANSPORT, "Transport"),

    ROLE_GROUND_UNIT (PwcgRoleCategory.OTHER, "Ground Unit"),
    ROLE_MAIN_BATTLE_TANK(PwcgRoleCategory.MAIN_BATTLE_TANK, "Tank Unit"),
    ROLE_TANK_DESTROYER(PwcgRoleCategory.TANK_DESTROYER, "Tank Destroyer Unit"),
    ROLE_SELF_PROPELLED_GUN(PwcgRoleCategory.SELF_PROPELLED_GUN, "SPG Unit"),
    ROLE_SELF_PROPELLED_AAA(PwcgRoleCategory.SELF_PROPELLED_AAA, "AAA Unit"),
    
    ROLE_NONE (PwcgRoleCategory.OTHER, "None");

    private PwcgRoleCategory roleCategory;
    private String roleDescription;
    
    PwcgRole (PwcgRoleCategory roleCategory, String roleDescription) 
    {
        this.roleCategory = roleCategory;
        this.roleDescription = roleDescription;
    }
    
    public static PwcgRole getRoleFromDescription(String description)
    {
        for (PwcgRole role : PwcgRole.values())
        {
            if (role.getRoleDescription().equals(description))
            {
                return role;
            }
        }
        return PwcgRole.ROLE_NONE;
    }

    public boolean isRoleCategory(PwcgRoleCategory askCategory)
    {
        if (roleCategory == askCategory)
        {
            return true;
        }
        return false;
    }

    public PwcgRoleCategory getRoleCategory()
    {
        return roleCategory;
    }

    public String getRoleDescription()
    {
        return roleDescription;
    }
}
