package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.List;

public enum PwcgRole
{
    ROLE_FIGHTER (RoleCategory.FIGHTER, "Fighter"),
    ROLE_STRATEGIC_INTERCEPT (RoleCategory.FIGHTER, "Interceptor"),
    ROLE_SEA_PLANE (RoleCategory.RECON, "Sea Plane"),
    ROLE_SEA_PLANE_SMALL (RoleCategory.FIGHTER, "Sea Plane Interceptor"),

    ROLE_ATTACK (RoleCategory.ATTACK, "Attack"),
    ROLE_RAIDER (RoleCategory.ATTACK, "Raider"),
    ROLE_TRAIN_BUSTER (RoleCategory.ATTACK, "Train Buster"),
    ROLE_TANK_BUSTER (RoleCategory.ATTACK, "Tank Buster"),
    ROLE_ANTI_SHIPPING (RoleCategory.ATTACK, "Maritime Attack"),
    ROLE_DIVE_BOMB (RoleCategory.ATTACK, "Dive Bomb"),
    
    ROLE_RECON (RoleCategory.RECON, "Recon"),
    ROLE_ARTILLERY_SPOT (RoleCategory.RECON, "Artillery Spot"),
    ROLE_SEA_PLANE_LARGE (RoleCategory.FIGHTER, "Interceptor"),

    ROLE_BOMB (RoleCategory.BOMBER, "Bomber"),
    ROLE_STRAT_BOMB (RoleCategory.BOMBER, "Strategic Bomber"),

    ROLE_TRANSPORT (RoleCategory.TRANSPORT, "Transport"),

    ROLE_BALLOON (RoleCategory.OTHER, "Balloon"),
    ROLE_GROUND_UNIT (RoleCategory.OTHER, "Ground Unit"),

    ROLE_NONE (RoleCategory.OTHER, "None"),
    ROLE_UNKNOWN (RoleCategory.OTHER, "Unknown");

    private RoleCategory roleCategory;
    private String roleDescription;
    
    PwcgRole (RoleCategory roleCategory, String roleDescription) 
    {
        this.roleCategory = roleCategory;
        this.roleDescription = roleDescription;
    }
    
    public static List<PwcgRole> getAllRoles ()
    {
        List<PwcgRole> allAircraftRoles = new ArrayList<>();
        allAircraftRoles.add(PwcgRole.ROLE_FIGHTER);
        allAircraftRoles.add(PwcgRole.ROLE_STRATEGIC_INTERCEPT);
        allAircraftRoles.add(PwcgRole.ROLE_SEA_PLANE);
        allAircraftRoles.add(PwcgRole.ROLE_SEA_PLANE_SMALL);

        allAircraftRoles.add(PwcgRole.ROLE_ATTACK);
        allAircraftRoles.add(PwcgRole.ROLE_DIVE_BOMB);

        allAircraftRoles.add(PwcgRole.ROLE_RECON);
        allAircraftRoles.add(PwcgRole.ROLE_ARTILLERY_SPOT);
        allAircraftRoles.add(PwcgRole.ROLE_SEA_PLANE_LARGE);

        allAircraftRoles.add(PwcgRole.ROLE_BOMB);
        allAircraftRoles.add(PwcgRole.ROLE_STRAT_BOMB);
        
        allAircraftRoles.add(PwcgRole.ROLE_TRANSPORT);
        return allAircraftRoles;
    }
    
    public static PwcgRole getRoleFromDescription(String description)
    {
        for (PwcgRole role : PwcgRole.getAllRoles())
        {
            if (role.getRoleDescription().equals(description))
            {
                return role;
            }
        }
        return PwcgRole.ROLE_UNKNOWN;
    }
    
    public boolean isRoleCategory(RoleCategory askCategory)
    {
        if (roleCategory == askCategory)
        {
            return true;
        }
        return false;
    }

    public RoleCategory getRoleCategory()
    {
        return roleCategory;
    }

    public String getRoleDescription()
    {
        return roleDescription;
    }

    public static PwcgRole getApproximateRole(PwcgRole role)
    {
        if (role.getRoleCategory() == RoleCategory.BOMBER)
        {
            return PwcgRole.ROLE_BOMB;
        }
        else if (role.getRoleCategory() == RoleCategory.FIGHTER)
        {
            return PwcgRole.ROLE_FIGHTER;
        }
        else if (role.getRoleCategory() == RoleCategory.TRANSPORT)
        {
            return PwcgRole.ROLE_BOMB;
        }
        return PwcgRole.ROLE_UNKNOWN;
    }
}
