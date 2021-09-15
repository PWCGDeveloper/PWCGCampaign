package pwcg.campaign.plane;

public enum PwcgRole
{
    ROLE_FIGHTER (PwcgRoleCategory.FIGHTER, "Fighter"),
    ROLE_STRATEGIC_INTERCEPT (PwcgRoleCategory.FIGHTER, "Interceptor"),

    ROLE_ATTACK (PwcgRoleCategory.ATTACK, "Attack"),
    ROLE_RAIDER (PwcgRoleCategory.ATTACK, "Raider"),
    ROLE_TRAIN_BUSTER (PwcgRoleCategory.ATTACK, "Train Buster"),
    ROLE_TANK_BUSTER (PwcgRoleCategory.ATTACK, "Tank Buster"),
    ROLE_ANTI_SHIPPING (PwcgRoleCategory.ATTACK, "Maritime Attack"),
    ROLE_DIVE_BOMB (PwcgRoleCategory.ATTACK, "Dive Bomb"),
    
    ROLE_RECON (PwcgRoleCategory.RECON, "Recon"),
    ROLE_ARTILLERY_SPOT (PwcgRoleCategory.RECON, "Artillery Spot"),
    ROLE_SEA_PLANE (PwcgRoleCategory.RECON, "Sea Plane"),

    ROLE_BOMB (PwcgRoleCategory.BOMBER, "Bomber"),
    ROLE_STRAT_BOMB (PwcgRoleCategory.BOMBER, "Strategic Bomber"),

    ROLE_TRANSPORT (PwcgRoleCategory.TRANSPORT, "Transport"),

    ROLE_BALLOON (PwcgRoleCategory.BALLOON, "Balloon"),
    ROLE_GROUND_UNIT (PwcgRoleCategory.OTHER, "Ground Unit"),
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
    
    public static boolean isStrategic(PwcgRole role)
    {
        if (role == ROLE_STRATEGIC_INTERCEPT || role == ROLE_STRAT_BOMB)
        {
            return true;
        }
        return false;
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
