package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public interface ITestCampaignFactory
{
    Campaign makeCampaign(String campaignName, SquadronTestProfile profile) throws PWCGException;
}