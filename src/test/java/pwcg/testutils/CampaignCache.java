package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

public class CampaignCache
{
    private static ICampaignCache rofCampaignCache = new CampaignCacheRoF();
    private static ICampaignCache bosCampaignCache = new CampaignCacheBoS();
    
    public static Campaign makeCampaign(SquadrontTestProfile campaignProfile) throws PWCGException
    {
        if (PWCGContextManager.isRoF())
        {
            return rofCampaignCache.makeCampaign(campaignProfile);
        }
        else
        {
            return bosCampaignCache.makeCampaign(campaignProfile);
        }
    }
    
    public static Campaign makeCampaignForceCreation(SquadrontTestProfile campaignProfile) throws PWCGException
    {
        if (PWCGContextManager.isRoF())
        {
            return rofCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
        else
        {
            return bosCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
    }
}
