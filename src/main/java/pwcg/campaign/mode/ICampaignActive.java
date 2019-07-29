package pwcg.campaign.mode;

import pwcg.core.exception.PWCGException;

public interface ICampaignActive
{
    boolean isCampaignActive() throws PWCGException;
}