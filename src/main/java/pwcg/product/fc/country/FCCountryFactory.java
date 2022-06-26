package pwcg.product.fc.country;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.ICountryFactory;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;

public class FCCountryFactory implements ICountryFactory
{
    public ICountry makeMapReferenceCountry(Side side)
    {
        if (side == Side.ALLIED)
        {
            return new FCCountry(Country.BRITAIN);
        }
        else
        {
            return new FCCountry(Country.GERMANY);
        }
    }

    public ICountry makeCountryByCode(int countryCode)
    {
        return new FCCountry(countryCode);
    }

    public ICountry makeCountryByService(ArmedService service)
    {
        return FCCountry.getCountryByService(service);
    }

    @Override
    public ICountry makeCountryByCountry(Country country)
    {
        return new FCCountry(country);
    }
}
