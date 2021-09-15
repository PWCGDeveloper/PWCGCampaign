package pwcg.campaign.plane;

public enum PwcgRole
{
    ROLE_FIGHTER (PwcgRoleCategory.FIGHTER, "Fighter"),
    ROLE_STRATEGIC_INTERCEPT (PwcgRoleCategory.FIGHTER, "Interceptor"),
    ROLE_SEA_PLANE (PwcgRoleCategory.RECON, "Sea Plane"),
    ROLE_SEA_PLANE_SMALL (PwcgRoleCategory.FIGHTER, "Sea Plane Interceptor"),

    ROLE_ATTACK (PwcgRoleCategory.ATTACK, "Attack"),
    ROLE_RAIDER (PwcgRoleCategory.ATTACK, "Raider"),
    ROLE_TRAIN_BUSTER (PwcgRoleCategory.ATTACK, "Train Buster"),
    ROLE_TANK_BUSTER (PwcgRoleCategory.ATTACK, "Tank Buster"),
    ROLE_ANTI_SHIPPING (PwcgRoleCategory.ATTACK, "Maritime Attack"),
    ROLE_DIVE_BOMB (PwcgRoleCategory.ATTACK, "Dive Bomb"),
    
    ROLE_RECON (PwcgRoleCategory.RECON, "Recon"),
    ROLE_ARTILLERY_SPOT (PwcgRoleCategory.RECON, "Artillery Spot"),
    ROLE_SEA_PLANE_LARGE (PwcgRoleCategory.FIGHTER, "Interceptor"),

    ROLE_BOMB (PwcgRoleCategory.BOMBER, "Bomber"),
    ROLE_STRAT_BOMB (PwcgRoleCategory.BOMBER, "Strategic Bomber"),

    ROLE_TRANSPORT (PwcgRoleCategory.TRANSPORT, "Transport"),

    ROLE_BALLOON (PwcgRoleCategory.OTHER, "Balloon"),
    ROLE_GROUND_UNIT (PwcgRoleCategory.OTHER, "Ground Unit"),

    ROLE_NONE (PwcgRoleCategory.OTHER, "None"),
    ROLE_UNKNOWN (PwcgRoleCategory.OTHER, "Unknown");

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
        return PwcgRole.ROLE_UNKNOWN;
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

    public static PwcgRole getApproximateRole(PwcgRole role)
    {
        if (role.getRoleCategory() == PwcgRoleCategory.BOMBER)
        {
            return PwcgRole.ROLE_BOMB;
        }
        else if (role.getRoleCategory() == PwcgRoleCategory.FIGHTER)
        {
            return PwcgRole.ROLE_FIGHTER;
        }
        else if (role.getRoleCategory() == PwcgRoleCategory.TRANSPORT)
        {
            return PwcgRole.ROLE_BOMB;
        }
        return PwcgRole.ROLE_UNKNOWN;
    }
}
