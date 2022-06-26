package pwcg.campaign.api;

import pwcg.campaign.ArmedService;
import pwcg.campaign.context.Country;

public interface ICountryFactory
{
    public ICountry makeMapReferenceCountry(Side side);
    public ICountry makeCountryByCode(int countryCode);
    public ICountry makeCountryByService(ArmedService service);
    public ICountry makeCountryByCountry(Country country);
}