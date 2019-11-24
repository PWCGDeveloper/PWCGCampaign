package pwcg.campaign;

import pwcg.campaign.context.PWCGContext;
import pwcg.coop.CoopPersonaManager;
import pwcg.core.exception.PWCGException;

public class CampaignInitialWriter
{
    public static void doInitialCampaignWrite(Campaign campaign) throws PWCGException
    {
        campaign.write();
        if (campaign.isCoop())
        {
            CoopPersonaManager.getIntance().buildHostPersonaForCoopCampaign(campaign);
        }
        PWCGContext.getInstance().setCampaign(campaign);
    }
}
