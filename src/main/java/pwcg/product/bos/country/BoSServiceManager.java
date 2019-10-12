package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceManager;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.AmericanColorMap;
import pwcg.gui.colors.GermanColorMap;
import pwcg.gui.colors.ItalianColorMap;
import pwcg.gui.colors.RAFColorMap;
import pwcg.gui.colors.VVSColorMap;

public class BoSServiceManager extends ArmedServiceManager implements IArmedServiceManager 
{
    public static int VVS = 10101;
    public static int LUFTWAFFE = 20101;
    public static int REGIA_AERONAUTICA = 20202;
    public static int USAAF = 10102;
    public static int RAF = 10103;
    
    private static String LUFTWAFFE_NAME = "Luftwaffe";
    private static String REGIA_AERONAUTICA_NAME ="Regia Aeronautica";
    private static String VVS_NAME ="Voyenno-Vozdushnye Sily";
    private static String USAAF_NAME ="United States Army Air Force";
    private static String RAF_NAME ="Royal Air Force";
    
    private static String LUFTWAFFE_ICON ="ServiceLuftwaffe";
    private static String REGIA_AERONAUTICA_ICON ="ServiceRA";
    private static String VVS_ICON ="ServiceVVS";
    private static String USAAF_ICON ="ServiceUSAAF";
    private static String RAF_ICON ="ServiceRAF";

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
            Logger.logException(e);
        }
    }

	private void createRussianAirServices() throws PWCGException
	{
		List <ArmedService> russianServices = new ArrayList<ArmedService>();
		armedServicesByCountry.put(BoSCountry.RUSSIA_CODE, russianServices);
   
		ArmedService vvs = new ArmedService();
		vvs.setServiceId(VVS);
		vvs.setCountry(new BoSCountry(BoSCountry.RUSSIA_CODE));
		vvs.setName(VVS_NAME);
		vvs.setServiceIcon(VVS_ICON);
		vvs.setStartDate(DateUtils.getBeginningOfGame());
		vvs.setEndDate(DateUtils.getEndOfWar());
		vvs.setServiceColorMap(new VVSColorMap());
		vvs.setGeneralRankForService("General-lieutenant");
        vvs.setDailyPersonnelReplacementRate(40);
        vvs.setDailyEquipmentReplacementRate(70);

		List<String> irasPics = new ArrayList<String>();
		irasPics.add("Russian");
		vvs.setPicDirs(irasPics);
		
		vvs.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 10);
		vvs.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 20);
		vvs.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 40);

		russianServices.add(vvs);
	}

	private void createGermanAirServices() throws PWCGException
	{
		List <ArmedService> germanServices = new ArrayList<ArmedService>();
		armedServicesByCountry.put(BoSCountry.GERMANY_CODE, germanServices);
   
		ArmedService luftwaffe = new ArmedService();
		luftwaffe.setServiceId(LUFTWAFFE);
		luftwaffe.setCountry(new BoSCountry(BoSCountry.GERMANY_CODE));
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
		luftwaffe.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 100);
		luftwaffe.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 70);
		luftwaffe.addServiceQuality(DateUtils.getDateYYYYMMDD("19450101"), 50);
		luftwaffe.setDailyPersonnelReplacementRate(15);
		luftwaffe.setDailyEquipmentReplacementRate(30);

		germanServices.add(luftwaffe);
	}

	private void createItalianAirServices() throws PWCGException
	{
		List <ArmedService> italianServices = new ArrayList<ArmedService>();
		armedServicesByCountry.put(BoSCountry.ITALY_CODE, italianServices);
   
		ArmedService regiaAeronautica = new ArmedService();
		regiaAeronautica.setServiceId(REGIA_AERONAUTICA);
		regiaAeronautica.setCountry(new BoSCountry(BoSCountry.ITALY_CODE));
		regiaAeronautica.setName(REGIA_AERONAUTICA_NAME);
		regiaAeronautica.setServiceIcon(REGIA_AERONAUTICA_ICON);
		regiaAeronautica.setStartDate(DateUtils.getStartofWWIIItaly());
		regiaAeronautica.setEndDate(DateUtils.getEndOfWar());
		regiaAeronautica.setServiceColorMap(new ItalianColorMap());
		regiaAeronautica.setGeneralRankForService("Generale di Divisione Aerea");
		regiaAeronautica.setDailyPersonnelReplacementRate(1);
		regiaAeronautica.setDailyEquipmentReplacementRate(3);
		regiaAeronautica.setStartDate(DateUtils.getDateYYYYMMDD("19411001"));

		List<String> lftPics = new ArrayList<String>();
		lftPics.add("Italian");
		regiaAeronautica.setPicDirs(lftPics);
		
		regiaAeronautica.addServiceQuality(DateUtils.getDateYYYYMMDD("193900101"), 35);

		italianServices.add(regiaAeronautica);
	}

    private void createAmericanAirServices() throws PWCGException
    {
        List <ArmedService> americanServices = new ArrayList<ArmedService>();
        armedServicesByCountry.put(BoSCountry.USA_CODE, americanServices);
   
        ArmedService usaaf = new ArmedService();
        usaaf.setServiceId(USAAF);
        usaaf.setCountry(new BoSCountry(BoSCountry.USA_CODE));
        usaaf.setName(USAAF_NAME);
        usaaf.setServiceIcon(USAAF_ICON);
        usaaf.setStartDate(DateUtils.getStartofWWIIUSA());
        usaaf.setEndDate(DateUtils.getEndOfWar());
        usaaf.setServiceColorMap(new AmericanColorMap());
        usaaf.setGeneralRankForService("General");
        usaaf.setDailyPersonnelReplacementRate(15);
        usaaf.setDailyEquipmentReplacementRate(30);
        usaaf.setStartDate(DateUtils.getDateYYYYMMDD("19440901"));

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

        americanServices.add(usaaf);
    }

    private void createBritishAirServices() throws PWCGException
    {
        List <ArmedService> britishServices = new ArrayList<ArmedService>();
        armedServicesByCountry.put(BoSCountry.BRITAIN_CODE, britishServices);
   
        ArmedService raf = new ArmedService();
        raf.setServiceId(RAF);
        raf.setCountry(new BoSCountry(BoSCountry.BRITAIN_CODE));
        raf.setName(RAF_NAME);
        raf.setServiceIcon(RAF_ICON);
        raf.setStartDate(DateUtils.getStartofWWIIUSA());
        raf.setEndDate(DateUtils.getEndOfWar());
        raf.setServiceColorMap(new RAFColorMap());
        raf.setGeneralRankForService("General");
        raf.setDailyPersonnelReplacementRate(15);
        raf.setDailyEquipmentReplacementRate(30);
        raf.setStartDate(DateUtils.getDateYYYYMMDD("19440901"));

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
        
        britishServices.add(raf);
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
        
        throw new PWCGException("Unexpected country for getPrimaryServiceForNation " + country);
    }
}
