package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.CampaignRemover;

public class TestCampaignFactoryBuilder
{
    public static Campaign makeCampaign(String campaignName, SquadronTestProfile campaignProfile) throws PWCGException
    {
        ITestCampaignFactory testCampaignFactory = new TestCampaignFactory();
        Campaign campaign = testCampaignFactory.makeCampaign(campaignName, campaignProfile);
        return campaign;
    }

    public static Campaign makeCampaignOnDisk(String campaignName, SquadronTestProfile campaignProfile) throws PWCGException
    {
        Campaign campaign = makeCampaign(campaignName, campaignProfile);
        campaign.write();

        return campaign;
    }

    public static void removeCampaignFromDisk(String campaignName) throws PWCGException
    {
        CampaignRemover.deleteCampaign(campaignName);
    }
}
