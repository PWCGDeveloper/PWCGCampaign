package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.List;

public enum Role
{
    ROLE_FIGHTER (RoleCategory.FIGHTER, "Fighter"),
    ROLE_STRATEGIC_INTERCEPT (RoleCategory.FIGHTER, "Interceptor"),
    ROLE_SEA_PLANE (RoleCategory.RECON, "Sea Plane"),
    ROLE_SEA_PLANE_SMALL (RoleCategory.FIGHTER, "Sea Plane Interceptor"),

    ROLE_ATTACK (RoleCategory.ATTACK, "Attack"),
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
    
    Role (RoleCategory roleCategory, String roleDescription) 
    {
        this.roleCategory = roleCategory;
        this.roleDescription = roleDescription;
    }
    
    public static List<Role> getAllRoles ()
    {
        List<Role> allAircraftRoles = new ArrayList<>();
        allAircraftRoles.add(Role.ROLE_FIGHTER);
        allAircraftRoles.add(Role.ROLE_STRATEGIC_INTERCEPT);
        allAircraftRoles.add(Role.ROLE_SEA_PLANE);
        allAircraftRoles.add(Role.ROLE_SEA_PLANE_SMALL);

        allAircraftRoles.add(Role.ROLE_ATTACK);
        allAircraftRoles.add(Role.ROLE_DIVE_BOMB);

        allAircraftRoles.add(Role.ROLE_RECON);
        allAircraftRoles.add(Role.ROLE_ARTILLERY_SPOT);
        allAircraftRoles.add(Role.ROLE_SEA_PLANE_LARGE);

        allAircraftRoles.add(Role.ROLE_BOMB);
        allAircraftRoles.add(Role.ROLE_STRAT_BOMB);
        
        allAircraftRoles.add(Role.ROLE_TRANSPORT);
        return allAircraftRoles;
    }
    
    public static Role getRoleFromDescription(String description)
    {
        for (Role role : Role.getAllRoles())
        {
            if (role.getRoleDescription().equals(description))
            {
                return role;
            }
        }
        return Role.ROLE_UNKNOWN;
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

    public static Role getApproximateRole(Role role)
    {
        if (role.getRoleCategory() == RoleCategory.BOMBER)
        {
            return Role.ROLE_BOMB;
        }
        else if (role.getRoleCategory() == RoleCategory.FIGHTER)
        {
            return Role.ROLE_FIGHTER;
        }
        else if (role.getRoleCategory() == RoleCategory.TRANSPORT)
        {
            return Role.ROLE_BOMB;
        }
        return Role.ROLE_UNKNOWN;
    }
}
