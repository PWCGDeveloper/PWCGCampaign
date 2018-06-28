package pwcg.campaign.ww1.country;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.CountryBase;



public class RoFCountry extends CountryBase  implements ICountry, Cloneable
{
    public static int FRANCE_CODE = 101;
    public static int BRITAIN_CODE = 102;
    public static int USA_CODE = 103;
    public static int RUSSIA_CODE = 105;
    public static int BELGIUM_CODE = 109;
    public static int GERMANY_CODE = 501;
    public static int AUSTRIA_CODE = 502;
    public static int ITALY_CODE = 999;
	
	
	public RoFCountry(Country country)
	{
	    this.country = country;
	}

    public RoFCountry(int countryCode)
    {
        this.country = this.countryCodeToCountry(countryCode);
    }

    @Override
    public ICountry copy()
    {
        ICountry clone = new RoFCountry(this.country);
        
        return clone;
    }

    @Override
    public Side getSide()
    {
        if (country == Country.NEUTRAL)
        {
            return Side.NEUTRAL;
        }
        
        if (country == Country.GERMANY || country == Country.AUSTRIA)
        {
            return Side.AXIS;
        }
        
        return Side.ALLIED;
    }

	public static ICountry getCountryByService (ArmedService service)
	{
	    ICountry country = new RoFCountry(Country.NEUTRAL);
	    
		// World War I
		if (service.getServiceId() == RoFServiceManager.LAVIATION_MILITAIRE)
		{
			country = new RoFCountry(Country.FRANCE);
		}
		else if (service.getServiceId() == RoFServiceManager.LAVIATION_MARINE)
		{
            country = new RoFCountry(Country.FRANCE);
		}
		else if (service.getServiceId() == RoFServiceManager.AVIATION_MILITAIRE_BELGE)
		{
            country = new RoFCountry(Country.BELGIUM);
		}
		else if (service.getServiceId() == RoFServiceManager.RFC)
		{
            country = new RoFCountry(Country.BRITAIN);
		}
		else if (service.getServiceId() == RoFServiceManager.RNAS)
		{
            country = new RoFCountry(Country.BRITAIN);
		}
		else if (service.getServiceId() == RoFServiceManager.RAF)
		{
            country = new RoFCountry(Country.BRITAIN);
		}
		else if (service.getServiceId() == RoFServiceManager.USAS)
		{
            country = new RoFCountry(Country.USA);
		}
		else if (service.getServiceId() == RoFServiceManager.DEUTSCHE_LUFTSTREITKRAFTE)
		{
            country = new RoFCountry(Country.GERMANY);
		}
        else if (service.getServiceId() == RoFServiceManager.MARINE_FLIEGER_CORP)
        {
            country = new RoFCountry(Country.GERMANY);
        }
        else if (service.getServiceId() == RoFServiceManager.RUSSIAN_AIR_SERVICE)
        {
            country = new RoFCountry(Country.RUSSIA);
        }
        else if (service.getServiceId() == RoFServiceManager.AUSTRO_HUNGARIAN_AIR_SERVICE)
        {
            country = new RoFCountry(Country.AUSTRIA);
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
        else if (country == Country.RUSSIA)
        {
            countryCode = RUSSIA_CODE;
        }
        else if (country == Country.GERMANY)
        {
            countryCode = GERMANY_CODE;
        }
        else if (country == Country.AUSTRIA)
        {
            countryCode = AUSTRIA_CODE;
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
        else if (countryCode == RUSSIA_CODE)
        {
            country = Country.RUSSIA;
        }       
        else if (countryCode == ITALY_CODE)
        {
            country = Country.ITALY;
        }       
        else if (countryCode == GERMANY_CODE)
        {
            country = Country.GERMANY;
        }
        else if (countryCode == AUSTRIA_CODE)
        {
            country = Country.AUSTRIA;
        }
        
        return country;
    }

    protected int adjustCountryCode()
    {
        if (country == Country.BELGIUM)
        {
            return countryToCountryCode(Country.FRANCE);
        }
                
        return getCountryCode();
    }
}
