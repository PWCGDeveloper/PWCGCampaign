package pwcg.campaign.mode;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;

public class CampaignCountryBuilderCoop implements ICampaignCountryBuilder
{    
    public ICountry determineCampaignCountry() throws PWCGException
    {
        return CountryFactory.makeNeutralCountry();
     }
}
