package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public interface ICampaignCache
{
    Campaign makeCampaign(SquadrontTestProfile profile) throws PWCGException;
    Campaign makeCampaignForceCreation(SquadrontTestProfile profile) throws PWCGException;
}