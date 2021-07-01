package pwcg.campaign.squadmember;

import pwcg.campaign.plane.Role;

public class AirVictimGenerator
{    
    public static boolean shouldUse(Role role)
    {
        if (role == Role.ROLE_FIGHTER || role == Role.ROLE_STRATEGIC_INTERCEPT)
        {
            return true;
        }
        return false;
    }
}
