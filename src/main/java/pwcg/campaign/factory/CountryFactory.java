package pwcg.campaign.factory;

import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.ICountryFactory;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.product.bos.country.BoSCountryFactory;

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
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            countryFactory = new BoSCountryFactory();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            countryFactory = new BoSCountryFactory();
        }
        
        return countryFactory;
    }

    public static ICountry makeAssaultProximityCountry(Side side, Coordinate assaultPosition, Date date) throws PWCGException
    {
        ICountry country = null;
        List<Company> squadrons = PWCGContext.getInstance().getCompanyManager().getActiveCompaniesBySideAndProximity(side, date, assaultPosition, 10000);
        if (squadrons.size() > 0)
        {
            country = squadrons.get(0).getCountry();
        }
        
        if (country == null)
        {
            country = makeMapReferenceCountry(side);
        }
        
        if (country.getCountry() == Country.ITALY  || country.getCountry() == Country.AUSTRIA)
        {
            country = makeCountryByCountry(Country.GERMANY);
        }
        
        if (country.getCountry() == Country.FRANCE || country.getCountry() == Country.BELGIUM || country.getCountry() == Country.CANADA)
        {
            country = makeCountryByCountry(Country.BRITAIN);
        }
        
        return country;
    }
    
    
}
