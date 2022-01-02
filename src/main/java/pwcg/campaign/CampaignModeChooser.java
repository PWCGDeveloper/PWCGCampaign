package pwcg.campaign;

import pwcg.campaign.api.Side;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class CampaignModeChooser
{
    public static boolean isCampaignModeCompetitive(Campaign campaign) throws PWCGException
    {
        if (campaign.getCampaignData().isCoop())
        {
            return isCoopCampaignModeCompetitive(campaign);
        }
        else
        {
            return false;
        }
    }

    private static boolean isCoopCampaignModeCompetitive(Campaign campaign) throws PWCGException
    {
        boolean isAxis = false;
        boolean isAllied = false;
        
        for (CrewMember player : campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            if (player.determineCountry(campaign.getDate()).getSide() == Side.ALLIED)
            {
                isAllied = true;
            }
            else
            {
                isAxis = true;
            }
        }
        
        if (isAxis && isAllied)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
