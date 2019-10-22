package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public interface ICampaignCache
{
    Campaign makeCampaign(SquadronTestProfile profile) throws PWCGException;
    Campaign makeCampaignForceCreation(SquadronTestProfile profile) throws PWCGException;
}