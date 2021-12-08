package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceManager;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.AmericanColorMap;
import pwcg.gui.colors.FrenchColorMap;
import pwcg.gui.colors.GermanColorMap;
import pwcg.gui.colors.ItalianColorMap;
import pwcg.gui.colors.RAFColorMap;
import pwcg.gui.colors.VVSColorMap;

public class BoSServiceManager extends ArmedServiceManager implements IArmedServiceManager 
{
    
    public static int VVS = 10101;
    public static int USAAF = 10102;
    public static int RAF = 10103;
    public static int NORMANDIE = 10104;
    public static int FREE_FRENCH = 10105;
    public static int RCAF = 10106;
    public static int LUFTWAFFE = 20101;
    public static int REGIA_AERONAUTICA = 20202;
        
    public static String REGIA_AERONAUTICA_NAME ="Regia Aeronautica";
    public static String USAAF_NAME ="United States Army Air Force";
    public static String RAF_NAME ="Royal Air Force";
    public static String RCAF_NAME ="Royal Canadian Air Force";
    public static String FREE_FRENCH_NAME ="Free French";

    public static String REGIA_AERONAUTICA_ICON ="ServiceRA";
    public static String USAAF_ICON ="ServiceUSAAF";
    public static String RAF_ICON ="ServiceRAF";
    public static String RCAF_ICON ="ServiceRCAF";
    public static String FREE_FRENCH_NAME_ICON ="ServiceFreeFrench";

    public static int WEHRMACHT = 20111;
    public static int RUSSIAN_ARMY = 10112;
    public static int US_ARMY = 10113;
    public static int BRITISH_ARMY = 10114;

    public static String US_ARMY_NAME ="United States Army";
    public static String BRITISH_ARMY_NAME ="British Army";

    public static String US_ARMY_ICON = "ServiceUnitedStatesArmy";
    public static String BRITISH_ARMY_ICON ="ServiceBritishArmy";

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
            createRussianAirServices();
            createGermanAirServices();
            createItalianAirServices();
            createAmericanAirServices();
            createBritishAirServices();
            
            createRussianArmyServices();
            createGermanArmyServices();
            createAmericanArmyServices();
            createBritishArmyServices();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }
    
    private void createRussianAirServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.RUSSIA_CODE, RussianServiceBuilder.createRussianAirServices());
    }
    
    private void createGermanAirServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.GERMANY_CODE, GermanServiceBuilder.createGermanAirServices());
    }
    
	private void createItalianAirServices() throws PWCGException
	{
		List <ArmedService> italianServices = new ArrayList<ArmedService>();
		armedServicesByCountry.put(BoSCountry.ITALY_CODE, italianServices);
   
		ArmedService regiaAeronautica = new ArmedService();
		regiaAeronautica.setServiceId(REGIA_AERONAUTICA);
		regiaAeronautica.setCountry(CountryFactory.makeCountryByCountry(Country.ITALY));
        regiaAeronautica.setNameCountry(CountryFactory.makeCountryByCountry(Country.ITALY));
		regiaAeronautica.setName(REGIA_AERONAUTICA_NAME);
		regiaAeronautica.setServiceIcon(REGIA_AERONAUTICA_ICON);
		regiaAeronautica.setEndDate(DateUtils.getEndOfWar());
		regiaAeronautica.setServiceColorMap(new ItalianColorMap());
		regiaAeronautica.setGeneralRankForService("Generale di Divisione Aerea");
        regiaAeronautica.setStartDate(DateUtils.getDateYYYYMMDD("19411001"));
        regiaAeronautica.setDailyPersonnelReplacementRatePerSquadron(0.8);
        regiaAeronautica.setDailyEquipmentReplacementRatePerSquadron(1.5);

		List<String> italianPics = new ArrayList<String>();
		italianPics.add("Italian");
		regiaAeronautica.setPicDirs(italianPics);
		
		regiaAeronautica.addServiceQuality(DateUtils.getDateYYYYMMDD("193900101"), 35);
        
		regiaAeronautica.setAirVictoriesForgreatAce(15);
		regiaAeronautica.setGroundVictoriesForgreatAce(80);

		italianServices.add(regiaAeronautica);
	}

    private void createAmericanAirServices() throws PWCGException
    {
        List <ArmedService> americanServices = new ArrayList<ArmedService>();
        armedServicesByCountry.put(BoSCountry.USA_CODE, americanServices);
   
        ArmedService usaaf = new ArmedService();
        usaaf.setServiceId(USAAF);
        usaaf.setCountry(CountryFactory.makeCountryByCountry(Country.USA));
        usaaf.setNameCountry(CountryFactory.makeCountryByCountry(Country.USA));
        usaaf.setName(USAAF_NAME);
        usaaf.setServiceIcon(USAAF_ICON);
        usaaf.setEndDate(DateUtils.getEndOfWar());
        usaaf.setServiceColorMap(new AmericanColorMap());
        usaaf.setGeneralRankForService("General");
        usaaf.setStartDate(DateUtils.getDateYYYYMMDD("19440901"));
        usaaf.setDailyPersonnelReplacementRatePerSquadron(3.0);
        usaaf.setDailyEquipmentReplacementRatePerSquadron(3.0);

        List<String> usaafPics = new ArrayList<String>();
        usaafPics.add("American");
        usaaf.setPicDirs(usaafPics);
        
        usaaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 50);
        usaaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19400101"), 60);
        usaaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19410101"), 60);
        usaaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 60);
        usaaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 70);
        usaaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 75);
        usaaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19450101"), 75);
        
        usaaf.setAirVictoriesForgreatAce(20);
        usaaf.setGroundVictoriesForgreatAce(100);

        americanServices.add(usaaf);
    }

    private void createBritishAirServices() throws PWCGException
    {
        List <ArmedService> britishServices = new ArrayList<ArmedService>();
        armedServicesByCountry.put(BoSCountry.BRITAIN_CODE, britishServices);
   
        createRAF(britishServices);
        createFreeFrench(britishServices);
        createRCAF(britishServices);
    }

    private void createRAF(List<ArmedService> britishServices) throws PWCGException
    {
        ArmedService raf = new ArmedService();
        raf.setServiceId(RAF);
        raf.setCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        raf.setNameCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        raf.setName(RAF_NAME);
        raf.setServiceIcon(RAF_ICON);
        raf.setEndDate(DateUtils.getEndOfWar());
        raf.setServiceColorMap(new RAFColorMap());
        raf.setGeneralRankForService("Air Vice-Marshal");
        raf.setStartDate(DateUtils.getDateYYYYMMDD("19440901"));
        raf.setDailyPersonnelReplacementRatePerSquadron(2.0);
        raf.setDailyEquipmentReplacementRatePerSquadron(2.0);

        List<String> rafPics = new ArrayList<String>();
        rafPics.add("British");
        raf.setPicDirs(rafPics);
        
        raf.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 60);
        raf.addServiceQuality(DateUtils.getDateYYYYMMDD("19400101"), 70);
        raf.addServiceQuality(DateUtils.getDateYYYYMMDD("19410101"), 70);
        raf.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 70);
        raf.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 70);
        raf.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 70);
        raf.addServiceQuality(DateUtils.getDateYYYYMMDD("19450101"), 70);
        
        raf.setAirVictoriesForgreatAce(20);
        raf.setGroundVictoriesForgreatAce(100);

        britishServices.add(raf);
    }

    private void createFreeFrench(List<ArmedService> britishServices) throws PWCGException
    {
        ArmedService freeFrench = new ArmedService();
        freeFrench.setServiceId(FREE_FRENCH);
        freeFrench.setCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        freeFrench.setNameCountry(CountryFactory.makeCountryByCountry(Country.FRANCE));
        freeFrench.setName(FREE_FRENCH_NAME);
        freeFrench.setServiceIcon(FREE_FRENCH_NAME_ICON);
        freeFrench.setEndDate(DateUtils.getEndOfWar());
        freeFrench.setServiceColorMap(new FrenchColorMap());
        freeFrench.setGeneralRankForService("Air Vice-Marshal");
        freeFrench.setStartDate(DateUtils.getDateYYYYMMDD("19440901"));
        freeFrench.setDailyPersonnelReplacementRatePerSquadron(1.0);
        freeFrench.setDailyEquipmentReplacementRatePerSquadron(1.0);

        List<String> rafPics = new ArrayList<String>();
        rafPics.add("British");
        freeFrench.setPicDirs(rafPics);
        
        freeFrench.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 60);
        freeFrench.addServiceQuality(DateUtils.getDateYYYYMMDD("19400101"), 70);
        freeFrench.addServiceQuality(DateUtils.getDateYYYYMMDD("19410101"), 70);
        freeFrench.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 70);
        freeFrench.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 70);
        freeFrench.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 70);
        freeFrench.addServiceQuality(DateUtils.getDateYYYYMMDD("19450101"), 70);
        
        freeFrench.setAirVictoriesForgreatAce(20);
        freeFrench.setGroundVictoriesForgreatAce(100);
        
        britishServices.add(freeFrench);
    }

    private void createRCAF(List<ArmedService> britishServices) throws PWCGException
    {
        ArmedService rcaf = new ArmedService();
        rcaf.setServiceId(RCAF);
        rcaf.setCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        rcaf.setNameCountry(CountryFactory.makeCountryByCountry(Country.CANADA));
        rcaf.setName(RCAF_NAME);
        rcaf.setServiceIcon(RCAF_ICON);
        rcaf.setEndDate(DateUtils.getEndOfWar());
        rcaf.setServiceColorMap(new FrenchColorMap());
        rcaf.setGeneralRankForService("Air Vice-Marshal");
        rcaf.setStartDate(DateUtils.getDateYYYYMMDD("19440901"));
        rcaf.setDailyPersonnelReplacementRatePerSquadron(1.0);
        rcaf.setDailyEquipmentReplacementRatePerSquadron(1.0);

        List<String> rafPics = new ArrayList<String>();
        rafPics.add("British");
        rcaf.setPicDirs(rafPics);
        
        rcaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 60);
        rcaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19400101"), 70);
        rcaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19410101"), 70);
        rcaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 70);
        rcaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 70);
        rcaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 70);
        rcaf.addServiceQuality(DateUtils.getDateYYYYMMDD("19450101"), 70);
        
        rcaf.setAirVictoriesForgreatAce(20);
        rcaf.setGroundVictoriesForgreatAce(100);

        britishServices.add(rcaf);
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
            return(getArmedServiceById(LUFTWAFFE, date));
        }
        else if (country == Country.RUSSIA)
        {
            return(getArmedServiceById(VVS, date));
        }
        else if (country == Country.ITALY)
        {
            return(getArmedServiceById(REGIA_AERONAUTICA, date));
        }
        else if (country == Country.USA)
        {
            return(getArmedServiceById(USAAF, date));
        }
        else if (country == Country.BRITAIN)
        {
            return(getArmedServiceById(RAF, date));
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
