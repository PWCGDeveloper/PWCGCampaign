package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

public class CampaignCache
{
    private static ICampaignCache rofCampaignCache = new CampaignCacheRoF();
    private static ICampaignCache bosCampaignCache = new CampaignCacheBoS();
    
    public static Campaign makeCampaign(String campaignProfileName) throws PWCGException
    {
        if (PWCGContextManager.isRoF())
        {
            return rofCampaignCache.makeCampaign(campaignProfileName);
        }
        else
        {
            return bosCampaignCache.makeCampaign(campaignProfileName);
        }
    }
    
    public static Campaign makeCampaignForceCreation(String campaignProfileName) throws PWCGException
    {
        if (PWCGContextManager.isRoF())
        {
            return rofCampaignCache.makeCampaignForceCreation(campaignProfileName);
        }
        else
        {
            return bosCampaignCache.makeCampaignForceCreation(campaignProfileName);
        }
    }
}
