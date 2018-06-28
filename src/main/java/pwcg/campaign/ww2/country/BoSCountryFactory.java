package pwcg.campaign.ww2.country;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.ICountryFactory;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;

public class BoSCountryFactory implements ICountryFactory
{

    public ICountry makeNeutralCountry()
    {
        return new BoSCountry(BoSCountry.NEUTRAL_CODE);
    }

    public ICountry makeMapReferenceCountry(Side side)
    {
        if (side == Side.ALLIED)
        {
            return new BoSCountry(BoSCountry.RUSSIA_CODE);
        }
        else
        {
            return new BoSCountry(BoSCountry.GERMANY_CODE);
        }
    }

    public ICountry makeCountryByCode(int countryCode)
    {
        return new BoSCountry(countryCode);
    }

    public ICountry makeCountryByService(ArmedService service)
    {
        return BoSCountry.getCountryByService(service);
    }

    @Override
    public ICountry makeCountryByCountry(Country country)
    {
        return new BoSCountry(country);
    }
}
