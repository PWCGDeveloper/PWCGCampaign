package pwcg.campaign.squadmember;

import pwcg.campaign.plane.PwcgRoleCategory;

public class AirVictimGenerator
{    
    public static boolean shouldUse(PwcgRoleCategory squadronPrimaryRoleCategory)
    {
        if (squadronPrimaryRoleCategory == PwcgRoleCategory.FIGHTER)
        {
            return true;
        }
        return false;
    }
}
