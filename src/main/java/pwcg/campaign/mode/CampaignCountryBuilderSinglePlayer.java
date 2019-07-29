package pwcg.campaign.mode;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignCountryBuilderSinglePlayer implements ICampaignCountryBuilder
{
    private Campaign campaign;
    
    public CampaignCountryBuilderSinglePlayer(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public ICountry determineCampaignCountry() throws PWCGException
    {
        SquadronMember referencePlayer = campaign.getReferenceCampaignMember();
        return CountryFactory.makeCountryByCountry(referencePlayer.getCountry());
     }
}
