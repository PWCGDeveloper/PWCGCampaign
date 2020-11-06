package pwcg.campaign;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class CampaignFixer
{
    public static void fixCampaign(Campaign campaign) throws PWCGException
    {
        CampaignCleaner cleaner = new CampaignCleaner(campaign);
        cleaner.cleanDataFiles();
        
        mergeAddedAces(campaign);
    }
    
    private static void mergeAddedAces(Campaign campaign) throws PWCGException
    {
        CampaignAces aces =  PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(campaign.getDate());
        campaign.getPersonnelManager().getCampaignAces().mergeAddedAces(aces);
    }
}
