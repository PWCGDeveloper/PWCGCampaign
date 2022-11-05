package pwcg.campaign;

import pwcg.core.exception.PWCGException;

public class CampaignInitialWriter
{
    public static void doInitialCampaignWrite(Campaign campaign) throws PWCGException
    {
        campaign.write();
    }
}
