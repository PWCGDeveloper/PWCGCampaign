package pwcg.campaign.squadmember;

import pwcg.campaign.plane.PwcgRole;

public class AirVictimGenerator
{    
    public static boolean shouldUse(PwcgRole role)
    {
        if (role == PwcgRole.ROLE_FIGHTER || role == PwcgRole.ROLE_STRATEGIC_INTERCEPT)
        {
            return true;
        }
        return false;
    }
}
