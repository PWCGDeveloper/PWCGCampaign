package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.crewmember.CrewMemberReplacer;
import pwcg.campaign.crewmember.CrewMemberReplacerCoop;
import pwcg.campaign.crewmember.ICrewMemberReplacer;
import pwcg.campaign.mode.CampaignActiveCoop;
import pwcg.campaign.mode.CampaignActiveSinglePlayer;
import pwcg.campaign.mode.CampaignDescriptionBuilderCoop;
import pwcg.campaign.mode.CampaignDescriptionBuilderSinglePlayer;
import pwcg.campaign.mode.ICampaignActive;
import pwcg.campaign.mode.ICampaignDescriptionBuilder;

public class CampaignModeFactory
{
    public static ICampaignDescriptionBuilder makeCampaignDescriptionBuilder (Campaign campaign)
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            return new CampaignDescriptionBuilderSinglePlayer(campaign);
        }
        else
        {
            return new CampaignDescriptionBuilderCoop();
        }
    }
    
    public static ICampaignActive makeCampaignActive (Campaign campaign)
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            return new CampaignActiveSinglePlayer(campaign);
        }
        else
        {
            return new CampaignActiveCoop();
        }
    }
    
    public static ICrewMemberReplacer makeCrewMemberReplacer (Campaign campaign)
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            return new CrewMemberReplacer(campaign);
        }
        else
        {
            return new CrewMemberReplacerCoop(campaign);
        }
    }
}
