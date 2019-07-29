package pwcg.campaign.mode;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignDescriptionBuilderSinglePlayer implements ICampaignDescriptionBuilder
{
    private Campaign campaign;
    
    public CampaignDescriptionBuilderSinglePlayer(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public String getCampaignDescription() throws PWCGException
    {
        String campaignDescription = "";        

        SquadronMember referencePlayer = campaign.getReferenceCampaignMember();

        campaignDescription += referencePlayer.getNameAndRank();
        campaignDescription += "     " + DateUtils.getDateString(campaign.getDate());
        
        Squadron squadron =  PWCGContextManager.getInstance().getSquadronManager().getSquadron(referencePlayer.getSquadronId());
        campaignDescription += "     " + squadron.determineDisplayName(campaign.getDate());
        campaignDescription += "     " + squadron.determineCurrentAirfieldName(campaign.getDate());

        return campaignDescription;
    }
}
