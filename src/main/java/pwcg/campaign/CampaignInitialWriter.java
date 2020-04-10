package pwcg.campaign;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class CampaignInitialWriter
{
    public static void doInitialCampaignWrite(Campaign campaign) throws PWCGException
    {
        campaign.write();
        PWCGContext.getInstance().setCampaign(campaign);
    }
}
