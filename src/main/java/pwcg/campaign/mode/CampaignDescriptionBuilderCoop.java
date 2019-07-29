package pwcg.campaign.mode;

import pwcg.core.exception.PWCGException;

public class CampaignDescriptionBuilderCoop implements ICampaignDescriptionBuilder
{
    public String getCampaignDescription() throws PWCGException
    {
        String campaignDescription = "Coop Campaign";
        return campaignDescription;
    }

}
