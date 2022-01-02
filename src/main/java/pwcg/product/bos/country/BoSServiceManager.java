package pwcg.product.bos.country;

import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceManager;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class BoSServiceManager extends ArmedServiceManager implements IArmedServiceManager 
{
    public static int WEHRMACHT = 20111;
    public static int SVV = 10112;
    public static int US_ARMY = 10113;
    public static int BRITISH_ARMY = 10114;


    private static BoSServiceManager instance;
    
    public static BoSServiceManager getInstance()
    {
        if (instance == null)
        {
            instance = new BoSServiceManager();
            instance.initialize();

        }
        return instance;
    }
    
    private BoSServiceManager ()
    {
    }

    protected void initialize() 
    {
        try
        {    
            createRussianServices();
            createGermanServices();
            createAmericanServices();
            createBritishServices();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }
    
    private void createRussianServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.RUSSIA_CODE, RussianServiceBuilder.createServices());
    }
    
    private void createGermanServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.GERMANY_CODE, GermanServiceBuilder.createServices());
    }

    private void createAmericanServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.USA_CODE, AmericanServiceBuilder.createServices());
    }

    private void createBritishServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.BRITAIN_CODE, BritishServiceBuilder.createServices());
    }

	@Override
    public ArmedService getArmedServiceById(int serviceId, Date campaignDate) throws PWCGException 
	{
		for(List <ArmedService> serviceList : armedServicesByCountry.values())
		{
			for (ArmedService service : serviceList)
			{
				if (service.getServiceId() == serviceId)
				{
					return service;
				}
			}
		}
		
		throw new PWCGException ("No service found for id = " + serviceId);
	}

	@Override
    public ArmedService getArmedServiceByName(String serviceName, Date campaignDate) throws PWCGException 
	{
		for(List <ArmedService> serviceList : armedServicesByCountry.values())
		{
			for (ArmedService service : serviceList)
			{
				if (service.getName().equals(serviceName))
				{
					return service;
				}
			}
		}
		
        throw new PWCGException ("No service found for name = " + serviceName);
	}

    @Override
    public ArmedService getPrimaryServiceForNation(Country country, Date date) throws PWCGException
    {
        if (country == Country.GERMANY)
        {
            return(getArmedServiceById(WEHRMACHT, date));
        }
        else if (country == Country.RUSSIA)
        {
            return(getArmedServiceById(SVV, date));
        }
        else if (country == Country.USA)
        {
            return(getArmedServiceById(US_ARMY, date));
        }
        else if (country == Country.BRITAIN)
        {
            return(getArmedServiceById(BRITISH_ARMY, date));
        }
        
        throw new PWCGException("Unexpected country for getPrimaryServiceForNation " + country);
    }

    @Override
    public ArmedService determineServiceByParsingSquadronId(int squadronId, Date date) throws PWCGException
    {
        String squadronIdString = "" + squadronId;
        if (squadronIdString.length() >= 3)
        {
            String countryCodeString = squadronIdString.substring(0,3);
            Integer countryCode = Integer.valueOf(countryCodeString);
            ICountry country = CountryFactory.makeCountryByCode(countryCode);
    
            return getPrimaryServiceForNation(country.getCountry(), date);
        }
        else
        {
            throw new PWCGException("");
        }
    }    
}
