package pwcg.campaign.mode;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
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

        CrewMember referencePlayer = campaign.findReferencePlayer();

        campaignDescription += referencePlayer.getNameAndRank();
        campaignDescription += "     " + DateUtils.getDateString(campaign.getDate());
        
        Company squadron =  PWCGContext.getInstance().getCompanyManager().getCompany(referencePlayer.getCompanyId());
        campaignDescription += "     " + squadron.determineDisplayName(campaign.getDate());
        campaignDescription += "     " + squadron.determineCurrentAirfieldName(campaign.getDate());

        return campaignDescription;
    }
}
