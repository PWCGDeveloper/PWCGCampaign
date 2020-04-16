package pwcg.campaign;

import pwcg.core.exception.PWCGException;

public class CampaignFixer
{
    public static void fixCampaign(Campaign campaign) throws PWCGException
    {
        CampaignCoopConverter converter = new CampaignCoopConverter(campaign);
        converter.convertToV8Coop();

        CampaignCleaner cleaner = new CampaignCleaner(campaign);
        cleaner.cleanDataFiles();
    }
}
