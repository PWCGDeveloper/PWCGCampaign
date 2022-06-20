package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.CampaignRemover;

public class CampaignCache
{
    private static ICampaignCache bosCampaignCache = new TestCampaignFactoryBoS();
    private static ICampaignCache fcCampaignCache = new TestCampaignFactoryFC();

    public static Campaign makeCampaign(SquadronTestProfile campaignProfile) throws PWCGException
    {
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

    public static Campaign makeCampaignOnDisk(SquadronTestProfile campaignProfile) throws PWCGException
    {
        CampaignRemover.deleteCampaign(TestCampaignFactoryBase.TEST_CAMPAIGN_NAME);
        System.out.println("Remove Test Campaign");

        Campaign campaign = makeCampaign(campaignProfile);
        campaign.write();

        return campaign;
    }
}
