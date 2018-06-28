package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public interface ICampaignCache
{
    Campaign makeCampaign(String campaignProfileName) throws PWCGException;
    Campaign makeCampaignForceCreation(String campaignProfileName) throws PWCGException;
}