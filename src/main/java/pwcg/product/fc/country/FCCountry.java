package pwcg.product.fc.country;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.CountryBase;



public class FCCountry extends CountryBase  implements ICountry, Cloneable
{
    public static int FRANCE_CODE = 301;
    public static int BRITAIN_CODE = 302;
    public static int USA_CODE = 303;
    public static int BELGIUM_CODE = 304;
    public static int GERMANY_CODE = 401;
	
	public FCCountry(Country country)
	{
	    this.country = country;
	}

    public FCCountry(int countryCode)
    {
        this.country = this.countryCodeToCountry(countryCode);
    }

    @Override
    public ICountry copy()
    {
        ICountry clone = new FCCountry(this.country);
        
        return clone;
    }

    @Override
    public Side getSide()
    {
        if (country == Country.NEUTRAL)
        {
            return Side.NEUTRAL;
        }
        
        if (country == Country.GERMANY)
        {
            return Side.AXIS;
        }
        
        return Side.ALLIED;
    }

	public static ICountry getCountryByService (ArmedService service)
	{
	    ICountry country = new FCCountry(Country.NEUTRAL);
	    
		// World War I
		if (service.getServiceId() == FCServiceManager.LAVIATION_MILITAIRE)
		{
			country = new FCCountry(Country.FRANCE);
		}
		else if (service.getServiceId() == FCServiceManager.AVIATION_MILITAIRE_BELGE)
		{
            country = new FCCountry(Country.BELGIUM);
		}
		else if (service.getServiceId() == FCServiceManager.RFC)
		{
            country = new FCCountry(Country.BRITAIN);
		}
		else if (service.getServiceId() == FCServiceManager.RNAS)
		{
            country = new FCCountry(Country.BRITAIN);
		}
		else if (service.getServiceId() == FCServiceManager.RAF)
		{
            country = new FCCountry(Country.BRITAIN);
		}
		else if (service.getServiceId() == FCServiceManager.USAS)
		{
            country = new FCCountry(Country.USA);
		}
		else if (service.getServiceId() == FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE)
		{
            country = new FCCountry(Country.GERMANY);
		}
		
		return country;
	}

    protected int countryToCountryCode (Country country)
    {
        int countryCode = NEUTRAL_CODE;
        
        // World War I
        if (country == Country.FRANCE)
        {
            countryCode = FRANCE_CODE;
        }
        else if (country == Country.BELGIUM)
        {
            countryCode = BELGIUM_CODE;
        }
        else if (country == Country.BRITAIN)
        {
            countryCode = BRITAIN_CODE;
        }
        else if (country == Country.USA)
        {
            countryCode = USA_CODE;
        }
        else if (country == Country.GERMANY)
        {
            countryCode = GERMANY_CODE;
        }
        
        return countryCode;
    }

    @Override
    protected Country countryCodeToCountry (int countryCode)
    {
        Country country = Country.NEUTRAL;
        
        // World War I
        if (countryCode == FRANCE_CODE)
        {
            country = Country.FRANCE;
        }
        else if (countryCode == BELGIUM_CODE)
        {
            country = Country.BELGIUM;
        }
        else if (countryCode == BRITAIN_CODE)
        {
            country = Country.BRITAIN;
        }
        else if (countryCode == USA_CODE)
        {
            country = Country.USA;
        }
        else if (countryCode == GERMANY_CODE)
        {
            country = Country.GERMANY;
        }
        
        return country;
    }

    protected int adjustCountryCode()
    {
        return getCountryCode();
    }

    @Override
    public ICountry getOppositeSideCountry()
    {
        if (getSide() == Side.ALLIED)
        {
            return new FCCountry(GERMANY_CODE);
        }
        else
        {
            return new FCCountry(FRANCE_CODE);
        }
    }
}
