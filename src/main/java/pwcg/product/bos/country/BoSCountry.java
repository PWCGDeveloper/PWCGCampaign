package pwcg.product.bos.country;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.CountryBase;



public class BoSCountry extends CountryBase implements Cloneable, ICountry
{
    public static int NEUTRAL_CODE = 0;
    public static int RUSSIA_CODE = 101;
    public static int BRITAIN_CODE = 102;
    public static int USA_CODE = 103;
    public static int GERMANY_CODE = 201;
    public static int ITALY_CODE = 202;

    public BoSCountry(Country country)
    {
        if (country == null)
        {
            country = Country.NEUTRAL;
        }
        
        this.country = country;
    }

    public BoSCountry(int countryCode)
    {
        this.country = this.countryCodeToCountry(countryCode);
    }

    @Override
    public ICountry copy()
    {
        ICountry clone = new BoSCountry(this.country);
        
        return clone;
    }

    @Override
    public Side getSide()
    {
        if (country == Country.NEUTRAL)
        {
            return Side.NEUTRAL;
        }
        
        if (country == Country.GERMANY || country == Country.ITALY)
        {
            return Side.AXIS;
        }
        
        return Side.ALLIED;
    }

	public static ICountry getCountryByService (ArmedService service)
	{
        ICountry country = new BoSCountry(Country.NEUTRAL);
	    
		// World War II
		if (service.getServiceId() == BoSServiceManager.WEHRMACHT)
		{
            country = new BoSCountry(Country.GERMANY);
		}
        else if (service.getServiceId() == BoSServiceManager.SVV)
        {
            country = new BoSCountry(Country.RUSSIA);
        }
        else if (service.getServiceId() == BoSServiceManager.US_ARMY)
        {
            country = new BoSCountry(Country.USA);
        }       
        else if (service.getServiceId() == BoSServiceManager.BRITISH_ARMY)
        {
            country = new BoSCountry(Country.BRITAIN);
        }       

		return country;
	}

    @Override
    protected int adjustCountryCode()
    {
        if (country == Country.ITALY)
        {
            return countryToCountryCode(Country.GERMANY);
        }

        return getCountryCode();
    }

    @Override
    protected int countryToCountryCode (Country country)
    {
        int countryCode = NEUTRAL_CODE;
        
        // World War II
        if (country == Country.RUSSIA)
        {
            countryCode = RUSSIA_CODE;
        }
        else if (country == Country.GERMANY)
        {
            countryCode = GERMANY_CODE;
        }
        else if (country == Country.ITALY)
        {
            countryCode = ITALY_CODE;
        }       
        else if (country == Country.USA)
        {
            countryCode = USA_CODE;
        }       
        else if (country == Country.BRITAIN)
        {
            countryCode = BRITAIN_CODE;
        }       

        return countryCode;
    }

    @Override
    protected Country countryCodeToCountry (int countryCode)
    {
        Country country = Country.NEUTRAL;
        
        // World War II
        if (countryCode == RUSSIA_CODE)
        {
            country = Country.RUSSIA;
        }
        else if (countryCode == GERMANY_CODE)
        {
            country = Country.GERMANY;
        }
        else if (countryCode == ITALY_CODE)
        {
            country = Country.ITALY;
        }       
        else if (countryCode == USA_CODE)
        {
            country = Country.USA;
        }       
        else if (countryCode == BRITAIN_CODE)
        {
            country = Country.BRITAIN;
        }       

        return country;
    }


    @Override
    public Country getCountry()
    {
        return country;
    }

    @Override
    public ICountry getOppositeSideCountry()
    {
        if (getSide() == Side.ALLIED)
        {
            return new BoSCountry(GERMANY_CODE);
        }
        else
        {
            return new BoSCountry(RUSSIA_CODE);
        }
    }

}
