package pwcg.campaign.ww2.country;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.CountryBase;
import pwcg.campaign.ww1.country.RoFCountry;



public class BoSCountry extends CountryBase implements Cloneable, ICountry
{
    public static int NEUTRAL_CODE = 0;
    public static int RUSSIA_CODE = 101;
    public static int GERMANY_CODE = 201;
    public static int ITALY_CODE = 202;

    public BoSCountry(Country country)
    {
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
        ICountry country = new RoFCountry(Country.NEUTRAL);
	    
		// World War II
		if (service.getServiceId() == BoSServiceManager.LUFTWAFFE)
		{
            country = new BoSCountry(Country.GERMANY);
		}
        else if (service.getServiceId() == BoSServiceManager.VVS)
        {
            country = new BoSCountry(Country.RUSSIA);
        }
        else if (service.getServiceId() == BoSServiceManager.REGIA_AERONAUTICA)
        {
            country = new BoSCountry(Country.ITALY);
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
        
        return country;
    }


    @Override
    public Country getCountry()
    {
        return country;
    }

}
