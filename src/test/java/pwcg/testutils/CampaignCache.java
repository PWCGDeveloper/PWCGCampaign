package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

public class CampaignCache
{
    private static ICampaignCache rofCampaignCache = new CampaignCacheRoF();
    private static ICampaignCache bosCampaignCache = new CampaignCacheBoS();
    private static ICampaignCache fcCampaignCache = new CampaignCacheBoS();
    
    public static Campaign makeCampaign(SquadrontTestProfile campaignProfile) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return rofCampaignCache.makeCampaign(campaignProfile);
        }
        else if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            return bosCampaignCache.makeCampaign(campaignProfile);
        }
        else
        {
            return fcCampaignCache.makeCampaign(campaignProfile);
        }
    }
    
    public static Campaign makeCampaignForceCreation(SquadrontTestProfile campaignProfile) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return rofCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
        else if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            return bosCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
        else
        {
            return fcCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
    }
}
