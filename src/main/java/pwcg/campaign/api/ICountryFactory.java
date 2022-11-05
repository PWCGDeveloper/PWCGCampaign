package pwcg.campaign.api;

import pwcg.campaign.ArmedService;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;

public interface ICountryFactory
{
    public ICountry makeMapReferenceCountry(FrontMapIdentifier mapIdentifier, Side side);
    public ICountry makeCountryByCode(int countryCode);
    public ICountry makeCountryByService(ArmedService service);
    public ICountry makeCountryByCountry(Country country);
}