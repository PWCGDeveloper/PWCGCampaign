package pwcg.campaign.ww2.country;

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
import pwcg.gui.colors.GermanColorMap;
import pwcg.gui.colors.ItalianColorMap;
import pwcg.gui.colors.VVSColorMap;

public class BoSServiceManager extends ArmedServiceManager implements IArmedServiceManager 
{
    public static int VVS = 10101;
    public static int LUFTWAFFE = 20101;
    public static int REGIA_AERONAUTICA = 20202;
    
    private static String LUFTWAFFE_NAME = "Luftwaffe";
    private static String REGIA_AERONAUTICA_NAME ="Regia Aeronautica";
    private static String VVS_NAME ="Voyenno-Vozdushnye Sily";
    
    private static String LUFTWAFFE_ICON ="ServiceLuftwaffe";
    private static String REGIA_AERONAUTICA_ICON ="ServiceRA";
    private static String VVS_ICON ="ServiceVVS";

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
		regiaAeronautica.setStartDate(DateUtils.getDateYYYYMMDD("19420801"));

		List<String> lftPics = new ArrayList<String>();
		lftPics.add("Italian");
		regiaAeronautica.setPicDirs(lftPics);
		
		regiaAeronautica.addServiceQuality(DateUtils.getDateYYYYMMDD("193900101"), 35);

		italianServices.add(regiaAeronautica);
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
