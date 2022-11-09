package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceManager;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGProduct;
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
        
    private static String LUFTWAFFE_NAME = "Luftwaffe";
    private static String REGIA_AERONAUTICA_NAME ="Regia Aeronautica";
    private static String VVS_NAME ="Voyenno-Vozdushnye Sily";
    private static String USAAF_NAME ="United States Army Air Force";
    private static String RAF_NAME ="Royal Air Force";
    private static String RCAF_NAME ="Royal Canadian Air Force";
    private static String NORMANDIE_NAME = "Normandie";
    private static String FREE_FRENCH_NAME ="Free French";
    
    private static String LUFTWAFFE_ICON ="ServiceLuftwaffe";
    private static String REGIA_AERONAUTICA_ICON ="ServiceRA";
    private static String VVS_ICON ="ServiceVVS";
    private static String USAAF_ICON ="ServiceUSAAF";
    private static String RAF_ICON ="ServiceRAF";
    private static String RCAF_ICON ="ServiceRCAF";
    private static String NORMANDIE_ICON ="ServiceNormandie";
    private static String FREE_FRENCH_NAME_ICON ="ServiceFreeFrench";
    
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
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

	private void createRussianAirServices() throws PWCGException
	{
		List <ArmedService> russianServices = new ArrayList<ArmedService>();
		armedServicesByCountry.put(BoSCountry.RUSSIA_CODE, russianServices);
   
        createVVS(russianServices);
        createNormandie(russianServices);
	}

    private void createVVS(List<ArmedService> russianServices) throws PWCGException
    {
        ArmedService vvs = new ArmedService(PWCGProduct.BOS);
		vvs.setServiceId(VVS);
        vvs.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        vvs.setNameCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
		vvs.setName(VVS_NAME);
		vvs.setServiceIcon(VVS_ICON);
		vvs.setStartDate(DateUtils.getBeginningOfGame());
		vvs.setEndDate(DateUtils.getEndOfWar());
		vvs.setServiceColorMap(new VVSColorMap());
		vvs.setGeneralRankForService("General-lieutenant");
        vvs.setDailyPersonnelReplacementRatePerSquadron(2.2);
        vvs.setDailyEquipmentReplacementRatePerSquadron(3.0);

		List<String> irasPics = new ArrayList<String>();
		irasPics.add("Russian");
		vvs.setPicDirs(irasPics);
		
		vvs.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 10);
		vvs.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 20);
        vvs.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 40);
        vvs.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 50);
        
        vvs.setAirVictoriesForgreatAce(20);
        vvs.setGroundVictoriesForgreatAce(100);

		russianServices.add(vvs);
    }

    private void createNormandie(List<ArmedService> russianServices) throws PWCGException
    {
        ArmedService normandie = new ArmedService(PWCGProduct.BOS);
        normandie.setServiceId(NORMANDIE);
        normandie.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        normandie.setNameCountry(CountryFactory.makeCountryByCountry(Country.FRANCE));
        normandie.setName(NORMANDIE_NAME);
        normandie.setServiceIcon(NORMANDIE_ICON);
        normandie.setStartDate(DateUtils.getDateYYYYMMDD("19420801"));
        normandie.setEndDate(DateUtils.getEndOfWar());
        normandie.setServiceColorMap(new FrenchColorMap());
        normandie.setGeneralRankForService("General-lieutenant");
        normandie.setDailyPersonnelReplacementRatePerSquadron(1.0);
        normandie.setDailyEquipmentReplacementRatePerSquadron(2.5);

        List<String> irasPics = new ArrayList<String>();
        irasPics.add("Russian");
        normandie.setPicDirs(irasPics);
        
        normandie.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 20);
        normandie.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 30);
        normandie.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 50);
        normandie.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 50);
        
        normandie.setAirVictoriesForgreatAce(20);
        normandie.setGroundVictoriesForgreatAce(100);

        russianServices.add(normandie);
    }

	private void createGermanAirServices() throws PWCGException
	{
		List <ArmedService> germanServices = new ArrayList<ArmedService>();
		armedServicesByCountry.put(BoSCountry.GERMANY_CODE, germanServices);
   
		ArmedService luftwaffe = new ArmedService(PWCGProduct.BOS);
		luftwaffe.setServiceId(LUFTWAFFE);
		luftwaffe.setCountry(CountryFactory.makeCountryByCountry(Country.GERMANY));
		luftwaffe.setNameCountry(CountryFactory.makeCountryByCountry(Country.GERMANY));
		luftwaffe.setName(LUFTWAFFE_NAME);
		luftwaffe.setServiceIcon(LUFTWAFFE_ICON);

		luftwaffe.setStartDate(DateUtils.getBeginningOfGame());
		luftwaffe.setEndDate(DateUtils.getEndOfWar());
		luftwaffe.setServiceColorMap(new GermanColorMap());
		luftwaffe.setGeneralRankForService("Generalleutnant");

		List<String> luftwaffePics = new ArrayList<String>();
		luftwaffePics.add("German");
		luftwaffe.setPicDirs(luftwaffePics);
		
		luftwaffe.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 50);
		luftwaffe.addServiceQuality(DateUtils.getDateYYYYMMDD("19400101"), 60);
		luftwaffe.addServiceQuality(DateUtils.getDateYYYYMMDD("19410101"), 70);
		luftwaffe.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 90);
        luftwaffe.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 70);
        luftwaffe.addServiceQuality(DateUtils.getDateYYYYMMDD("19440601"), 50);
		luftwaffe.setDailyPersonnelReplacementRatePerSquadron(1.3);
		luftwaffe.setDailyEquipmentReplacementRatePerSquadron(1.0);
        
		luftwaffe.setAirVictoriesForgreatAce(50);
		luftwaffe.setGroundVictoriesForgreatAce(150);

		germanServices.add(luftwaffe);
	}

	private void createItalianAirServices() throws PWCGException
	{
		List <ArmedService> italianServices = new ArrayList<ArmedService>();
		armedServicesByCountry.put(BoSCountry.ITALY_CODE, italianServices);
   
		ArmedService regiaAeronautica = new ArmedService(PWCGProduct.BOS);
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
   
        ArmedService usaaf = new ArmedService(PWCGProduct.BOS);
        usaaf.setServiceId(USAAF);
        usaaf.setCountry(CountryFactory.makeCountryByCountry(Country.USA));
        usaaf.setNameCountry(CountryFactory.makeCountryByCountry(Country.USA));
        usaaf.setName(USAAF_NAME);
        usaaf.setServiceIcon(USAAF_ICON);
        usaaf.setEndDate(DateUtils.getEndOfWar());
        usaaf.setServiceColorMap(new AmericanColorMap());
        usaaf.setGeneralRankForService("General");
        usaaf.setStartDate(DateUtils.getDateYYYYMMDD("19430601"));
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
        ArmedService raf = new ArmedService(PWCGProduct.BOS);
        raf.setServiceId(RAF);
        raf.setCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        raf.setNameCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        raf.setName(RAF_NAME);
        raf.setServiceIcon(RAF_ICON);
        raf.setEndDate(DateUtils.getEndOfWar());
        raf.setServiceColorMap(new RAFColorMap());
        raf.setGeneralRankForService("Air Vice-Marshal");
        raf.setStartDate(DateUtils.getDateYYYYMMDD("19410601"));
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
        ArmedService freeFrench = new ArmedService(PWCGProduct.BOS);
        freeFrench.setServiceId(FREE_FRENCH);
        freeFrench.setCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        freeFrench.setNameCountry(CountryFactory.makeCountryByCountry(Country.FRANCE));
        freeFrench.setName(FREE_FRENCH_NAME);
        freeFrench.setServiceIcon(FREE_FRENCH_NAME_ICON);
        freeFrench.setEndDate(DateUtils.getEndOfWar());
        freeFrench.setServiceColorMap(new FrenchColorMap());
        freeFrench.setGeneralRankForService("Air Vice-Marshal");
        freeFrench.setStartDate(DateUtils.getDateYYYYMMDD("19430301"));
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
        ArmedService rcaf = new ArmedService(PWCGProduct.BOS);
        rcaf.setServiceId(RCAF);
        rcaf.setCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        rcaf.setNameCountry(CountryFactory.makeCountryByCountry(Country.CANADA));
        rcaf.setName(RCAF_NAME);
        rcaf.setServiceIcon(RCAF_ICON);
        rcaf.setEndDate(DateUtils.getEndOfWar());
        rcaf.setServiceColorMap(new FrenchColorMap());
        rcaf.setGeneralRankForService("Air Vice-Marshal");
        rcaf.setStartDate(DateUtils.getDateYYYYMMDD("19410601"));
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

    @Override
    public PWCGProduct getProduct()
    {
        return PWCGProduct.BOS;
    }    
}
