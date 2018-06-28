package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.List;

public enum Role
{
    ROLE_NONE,
    ROLE_FIGHTER,
    ROLE_ATTACK,
    ROLE_RECON,
    ROLE_ARTILLERY_SPOT,
    ROLE_BOMB,
    ROLE_DIVE_BOMB,
    ROLE_STRAT_BOMB,
    ROLE_HOME_DEFENSE,
    ROLE_SEA_PLANE,
    ROLE_SEA_PLANE_SMALL,
    ROLE_SEA_PLANE_LARGE,
    ROLE_BALLOON,
    ROLE_TRANSPORT,
    ROLE_GROUND_UNIT,
    ROLE_UNKNOWN;

    private static String ROLE_FIGHTER_DESC = "Fighter";
    private static String ROLE_ATTACK_DESC = "Attack";
    private static String ROLE_RECON_DESC = "Recon";
    private static String ROLE_BOMB_DESC = "Bomb";
    private static String ROLE_TRANSPORT_DESC = "Transport";
    private static String ROLE_DIVE_BOMB_DESC = "Dive Bomb";
    private static String ROLE_STRAT_BOMB_DESC = "Strategic Bomb";
    private static String ROLE_SEA_PLANE_DESC = "Sea Plane";
    private static String ROLE_SEA_PLANE_SMALL_DESC = "Small Sea Plane";
    private static String ROLE_SEA_PLANE_LARGE_DESC = "Large Sea Plane";
    
    public static List<Role> getAllRoles ()
    {
        List<Role> allAircraftRoles = new ArrayList<>();
        allAircraftRoles.add(Role.ROLE_FIGHTER);
        allAircraftRoles.add(Role.ROLE_ATTACK);
        allAircraftRoles.add(Role.ROLE_RECON);
        allAircraftRoles.add(Role.ROLE_BOMB);
        allAircraftRoles.add(Role.ROLE_DIVE_BOMB);
        allAircraftRoles.add(Role.ROLE_STRAT_BOMB);
        allAircraftRoles.add(Role.ROLE_SEA_PLANE);
        allAircraftRoles.add(Role.ROLE_SEA_PLANE_SMALL);
        allAircraftRoles.add(Role.ROLE_SEA_PLANE_LARGE);
        return allAircraftRoles;
    }

    public static Role descToRole (String desc)
    {
        Role role =  Role.ROLE_FIGHTER;
        if (desc.equalsIgnoreCase(ROLE_ATTACK_DESC))
        {
            role =  Role.ROLE_ATTACK;
        }
        else if (desc.equalsIgnoreCase(ROLE_RECON_DESC))
        {
            role =  Role.ROLE_RECON;
        }
        else if (desc.equalsIgnoreCase(ROLE_BOMB_DESC))
        {
            role =  Role.ROLE_BOMB;
        }
        else if (desc.equalsIgnoreCase(ROLE_STRAT_BOMB_DESC))
        {
            role =  Role.ROLE_STRAT_BOMB;
        }
        else if (desc.equalsIgnoreCase(ROLE_DIVE_BOMB_DESC))
        {
            role =  Role.ROLE_DIVE_BOMB;
        }
        else if (desc.equalsIgnoreCase(ROLE_SEA_PLANE_DESC))
        {
            role =  Role.ROLE_SEA_PLANE;
        }
        else if (desc.equalsIgnoreCase(ROLE_SEA_PLANE_LARGE_DESC))
        {
            role =  Role.ROLE_SEA_PLANE_LARGE;
        }
        else if (desc.equalsIgnoreCase(ROLE_SEA_PLANE_SMALL_DESC))
        {
            role =  Role.ROLE_SEA_PLANE_SMALL;
        }
        else if (desc.equalsIgnoreCase(ROLE_TRANSPORT_DESC))
        {
            role =  Role.ROLE_TRANSPORT;
        }
        
        return role;
    }

    public static Role getApproximateRole(Role role)
    {
        Role approximateRole = Role.ROLE_UNKNOWN;

        if (role == Role.ROLE_FIGHTER)
        {
            approximateRole = Role.ROLE_FIGHTER;
        }
        else if (role == Role.ROLE_TRANSPORT)
        {
            approximateRole = Role.ROLE_TRANSPORT;
        }
        else if (role == Role.ROLE_STRAT_BOMB)
        {
            approximateRole = Role.ROLE_STRAT_BOMB;
        }
        else if (role == Role.ROLE_ATTACK)
        {
            approximateRole = Role.ROLE_BOMB;
        }
        else if (role == Role.ROLE_RECON)
        {
            approximateRole = Role.ROLE_BOMB;
        }
        else if (role == Role.ROLE_BOMB)
        {
            approximateRole = Role.ROLE_BOMB;
        }
        else if (role == Role.ROLE_DIVE_BOMB)
        {
            approximateRole = Role.ROLE_BOMB;
        }
        else if (role == Role.ROLE_SEA_PLANE)
        {
            approximateRole = Role.ROLE_BOMB;
        }
        else if (role == Role.ROLE_SEA_PLANE_SMALL)
        {
            approximateRole = Role.ROLE_BOMB;
        }
        else if (role == Role.ROLE_SEA_PLANE_LARGE)
        {
            approximateRole = Role.ROLE_BOMB;
        }
        else if (role == Role.ROLE_BALLOON)
        {
            approximateRole = Role.ROLE_BALLOON;
        }
        else if (role == Role.ROLE_GROUND_UNIT)
        {
            approximateRole = Role.ROLE_GROUND_UNIT;
        }
        
        return approximateRole;
    }

    public static String roleToSDesc (Role role)
    {
        String desc =  ROLE_FIGHTER_DESC;
        if (role == Role.ROLE_ATTACK)
        {
            desc =  ROLE_ATTACK_DESC;
        }
        else if (role == Role.ROLE_RECON)
        {
            desc =  ROLE_RECON_DESC;
        }
        else if (role == Role.ROLE_BOMB)
        {
            desc =  ROLE_BOMB_DESC;
        }
        else if (role == Role.ROLE_TRANSPORT)
        {
            desc =  ROLE_TRANSPORT_DESC;
        }
        else if (role == Role.ROLE_STRAT_BOMB)
        {
            desc =  ROLE_STRAT_BOMB_DESC;
        }
        else if (role == Role.ROLE_DIVE_BOMB)
        {
            desc =  ROLE_DIVE_BOMB_DESC;
        }
        else if (role == Role.ROLE_SEA_PLANE)
        {
            desc =  ROLE_SEA_PLANE_DESC;
        }
        
        return desc;
    }

    public static boolean isRoleSeaPlane(Role role)
    {
        if (role == Role.ROLE_SEA_PLANE || 
            role == Role.ROLE_SEA_PLANE_LARGE || 
            role == Role.ROLE_SEA_PLANE_SMALL)
        {
            return true;
        }
        
        return false;
    }
}
