package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.mode.CampaignActiveCoop;
import pwcg.campaign.mode.CampaignActiveSinglePlayer;
import pwcg.campaign.mode.CampaignCountryBuilderCoop;
import pwcg.campaign.mode.CampaignCountryBuilderSinglePlayer;
import pwcg.campaign.mode.CampaignDescriptionBuilderCoop;
import pwcg.campaign.mode.CampaignDescriptionBuilderSinglePlayer;
import pwcg.campaign.mode.ICampaignActive;
import pwcg.campaign.mode.ICampaignCountryBuilder;
import pwcg.campaign.mode.ICampaignDescriptionBuilder;
import pwcg.campaign.squadmember.ISquadronMemberReplacer;
import pwcg.campaign.squadmember.SquadronMemberReplacer;
import pwcg.campaign.squadmember.SquadronMemberReplacerCoop;

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
    
    public static ICampaignCountryBuilder makeCampaignCountryBuilder (Campaign campaign)
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            return new CampaignCountryBuilderSinglePlayer(campaign);
        }
        else
        {
            return new CampaignCountryBuilderCoop();
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
    
    public static ISquadronMemberReplacer makeSquadronMemberReplacer (Campaign campaign)
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            return new SquadronMemberReplacer(campaign);
        }
        else
        {
            return new SquadronMemberReplacerCoop(campaign);
        }
    }
}
