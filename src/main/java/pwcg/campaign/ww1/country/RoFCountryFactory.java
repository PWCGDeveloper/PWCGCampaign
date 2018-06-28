package pwcg.campaign.ww1.country;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.ICountryFactory;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;

public class RoFCountryFactory implements ICountryFactory
{

    public ICountry makeNeutralCountry()
    {
        return new RoFCountry(Country.NEUTRAL);
    }

    public ICountry makeMapReferenceCountry(Side side)
    {
        if (side == Side.ALLIED)
        {
            return new RoFCountry(Country.FRANCE);
        }
        else
        {
            return new RoFCountry(Country.GERMANY);
        }
    }

    public ICountry makeCountryByCode(int countryCode)
    {
        return new RoFCountry(countryCode);
    }

    public ICountry makeCountryByService(ArmedService service)
    {
        return RoFCountry.getCountryByService(service);
    }

    @Override
    public ICountry makeCountryByCountry(Country country)
    {
        return new RoFCountry(country);
    }
}
