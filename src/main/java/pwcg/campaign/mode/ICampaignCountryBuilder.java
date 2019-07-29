package pwcg.campaign.mode;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;

public interface ICampaignCountryBuilder
{
    public ICountry determineCampaignCountry() throws PWCGException;
}
