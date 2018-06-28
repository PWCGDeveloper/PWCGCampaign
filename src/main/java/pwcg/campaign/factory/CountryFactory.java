package pwcg.campaign.factory;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.ICountryFactory;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.country.RoFCountryFactory;
import pwcg.campaign.ww2.country.BoSCountryFactory;

public class CountryFactory
{

    public static ICountry makeNeutralCountry()
    {
        ICountryFactory countryFactory = getCountryFactory();        
        return countryFactory.makeNeutralCountry();
    }

    public static ICountry makeMapReferenceCountry(Side side)
    {
        ICountryFactory countryFactory = getCountryFactory();        
        return countryFactory.makeMapReferenceCountry(side);
    }

    public static ICountry makeCountryByCode(int countryCode)
    {
        ICountryFactory countryFactory = getCountryFactory();        
        return countryFactory.makeCountryByCode(countryCode);
    }

    public static ICountry makeCountryByService(ArmedService service)
    {
        ICountryFactory countryFactory = getCountryFactory();        
        return countryFactory.makeCountryByService(service);
    }

    public static ICountry makeCountryByCountry(Country country)
    {
        ICountryFactory countryFactory = getCountryFactory();        
        return countryFactory.makeCountryByCountry(country);
    }

    private static ICountryFactory getCountryFactory()
    {
        ICountryFactory countryFactory = null;
        if (PWCGContextManager.isRoF())
        {
            countryFactory = new RoFCountryFactory();
        }
        else
        {
            countryFactory = new BoSCountryFactory();
        }
        
        return countryFactory;
    }
}
