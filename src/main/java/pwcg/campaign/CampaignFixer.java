package pwcg.campaign;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class CampaignFixer
{
    public static void fixCampaign(Campaign campaign) throws PWCGException
    {
        CampaignCleaner cleaner = new CampaignCleaner(campaign);
        cleaner.cleanDataFiles();
        
        mergeAddedAces(campaign);
        trimNames(campaign);
    }
    
    private static void trimNames(Campaign campaign) throws PWCGException
    {
        for (CrewMember player : campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            String name = player.getName().trim();
            player.setName(name);
        }
    }

    private static void mergeAddedAces(Campaign campaign) throws PWCGException
    {
        CampaignAces aces =  PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(campaign.getDate());
        campaign.getPersonnelManager().getCampaignAces().mergeAddedAces(aces);
    }
}
