package pwcg.campaign.mode;

import pwcg.core.exception.PWCGException;

public class CampaignActiveCoop implements ICampaignActive 
{
    public boolean isCampaignActive() throws PWCGException
    {
        return true;
    }
}
