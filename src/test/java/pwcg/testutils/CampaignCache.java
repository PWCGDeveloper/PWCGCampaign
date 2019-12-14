package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.CampaignRemover;

public class CampaignCache
{
    private static ICampaignCache bosCampaignCache = new CampaignCacheBoS();
    private static ICampaignCache fcCampaignCache = new CampaignCacheFC();
    
    public static Campaign makeCampaign(SquadronTestProfile campaignProfile) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return fcCampaignCache.makeCampaign(campaignProfile);
        }
        else
        {
            return bosCampaignCache.makeCampaign(campaignProfile);
        }
    }
    
    public static Campaign makeCampaignForceCreation(SquadronTestProfile campaignProfile) throws PWCGException
    {
        CampaignRemover campaignRemover = new CampaignRemover();
        campaignRemover.deleteCampaign(CampaignCacheBase.TEST_CAMPAIGN_NAME);         

        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return fcCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
        else
        {
            return bosCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
    }
}
