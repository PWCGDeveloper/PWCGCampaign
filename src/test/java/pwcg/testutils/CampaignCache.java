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
        CampaignRemover.deleteCampaign(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        System.out.println("Remove Test Campaign");

        Campaign campaign;
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            campaign = fcCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
        else
        {
            campaign = bosCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
        
        PWCGContext.getInstance().setCampaign(campaign);
        PWCGContext.getInstance().changeContext(campaignProfile.getMapIdentifier());
        return campaign;
    }
}
