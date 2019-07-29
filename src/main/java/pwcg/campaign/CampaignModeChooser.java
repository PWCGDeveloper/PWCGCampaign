package pwcg.campaign;

import pwcg.campaign.api.Side;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignModeChooser
{
    private Campaign campaign;
    
    public CampaignModeChooser(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public CampaignMode chooseCampaignMode() throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_NONE)
        {
            return determineCampaignMode();
        }
        else
        {
            return campaign.getCampaignData().getCampaignMode();
        }
    }

    private CampaignMode determineCampaignMode() throws PWCGException
    {
        if (campaign.getCampaignData().isCoop())
        {
            return determineCoopCampaignMode();
        }
        else
        {
            return CampaignMode.CAMPAIGN_MODE_SINGLE;
        }
    }

    private CampaignMode determineCoopCampaignMode() throws PWCGException
    {
        boolean isAxis = false;
        boolean isAllied = false;
        
        for (SquadronMember player : campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            if (player.determineCountry(campaign.getDate()).getSide() == Side.ALLIED)
            {
                isAllied = true;
            }
            else
            {
                isAxis = true;
            }
        }
        
        if (isAxis && isAllied)
        {
            return CampaignMode.CAMPAIGN_MODE_COMPETITIVE;
        }
        else
        {
            return CampaignMode.CAMPAIGN_MODE_COOP;
        }
    }
}
