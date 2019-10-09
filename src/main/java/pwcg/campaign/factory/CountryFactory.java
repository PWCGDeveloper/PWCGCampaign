package pwcg.campaign.factory;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.ICountryFactory;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.country.BoSCountryFactory;
import pwcg.product.fc.country.FCCountryFactory;
import pwcg.product.rof.country.RoFCountryFactory;

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
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            countryFactory = new RoFCountryFactory();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            countryFactory = new BoSCountryFactory();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            countryFactory = new FCCountryFactory();
        }
        
        return countryFactory;
    }
}
