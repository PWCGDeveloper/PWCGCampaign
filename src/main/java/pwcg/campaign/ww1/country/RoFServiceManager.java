package pwcg.campaign.ww1.country;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceManager;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.AmericanColorMap;
import pwcg.gui.colors.AustrianColorMap;
import pwcg.gui.colors.BelgianColorMap;
import pwcg.gui.colors.FrenchColorMap;
import pwcg.gui.colors.FrenchNavyColorMap;
import pwcg.gui.colors.GermanColorMap;
import pwcg.gui.colors.GermanNavyColorMap;
import pwcg.gui.colors.RAFColorMap;
import pwcg.gui.colors.RFCColorMap;
import pwcg.gui.colors.RNASColorMap;
import pwcg.gui.colors.RussianColorMap;

public class RoFServiceManager extends ArmedServiceManager implements IArmedServiceManager 
{
    public static int LAVIATION_MILITAIRE = 10101;
    public static int LAVIATION_MARINE = 10102;
    public static int AVIATION_MILITAIRE_BELGE = 10903;
    public static int RFC = 10201;
    public static int RNAS = 10202;
    public static int RAF = 10203;
    public static int USAS = 10301;
    public static int RUSSIAN_AIR_SERVICE = 10501;
    public static int DEUTSCHE_LUFTSTREITKRAFTE = 50101;
    public static int MARINE_FLIEGER_CORP = 50102;
    public static int AUSTRO_HUNGARIAN_AIR_SERVICE = 50201;
    
    public static String LAVIATION_MILITAIRE_NAME = "l'Aviation Militaire";
    public static String LAVIATION_MARINE_NAME = "l'Aviation Maritime";
    public static String AVIATION_MILITAIRE_BELGE_NAME = "Aviation Militaire Belge";
    public static String RFC_NAME = "Royal Flying Corp";
    public static String RNAS_NAME = "Royal Naval Air Service";
    public static String RAF_NAME = "Royal Air Force";
    public static String USAS_NAME = "United States Air Service";
    public static String DEUTSCHE_LUFTSTREITKRAFTE_NAME = "Deutsche Luftstreitkrafte";
    public static String MARINE_FLIEGER_CORP_NAME = "Marine Flieger Korps";
    public static String AUSTRO_HUNGARIAN_AIR_SERVICE_NAME ="Austro-Hungarian Luftfahrtruppe";
    public static String RUSSIAN_AIR_SERVICE_NAME ="Imperial Russian Air Service";
        	
    public static String LAVIATION_MILITAIRE_ICON = "ServiceFrenchArmy";
    public static String LAVIATION_MARINE_ICON = "ServiceFrenchNavy";
    public static String AVIATION_MILITAIRE_BELGE_ICON = "ServiceBelgianArmy";
    public static String RFC_ICON = "ServiceRFC";
    public static String RNAS_ICON = "ServiceRNAS";
    public static String RAF_ICON = "ServiceRAF";
    public static String USAS_ICON = "ServiceUSAS";
    public static String DEUTSCHE_LUFTSTREITKRAFTE_ICON = "ServiceGAS";
    public static String MARINE_FLIEGER_CORP_ICON = "ServiceGMF";
    public static String AUSTRO_HUNGARIAN_AIR_SERVICE_ICON ="ServiceLFT";
    public static String RUSSIAN_AIR_SERVICE_ICON ="ServiceIRAS";

    private static RoFServiceManager instance;
    
    public static RoFServiceManager getInstance()
    {
        if (instance == null)
        {
            instance = new RoFServiceManager();
            instance.initialize();

        }
        return instance;
    }
    
    private RoFServiceManager ()
	{
	}

    protected void initialize() 
    {
        try
        {
            Date startDate = DateUtils.getBeginningOfGame();     
            Date endDate = DateUtils.getEndOfWar();
            Date rafStartDate = DateUtils.getRAFDate();
    
            List <ArmedService> frenchServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(RoFCountry.FRANCE_CODE, frenchServices);
            
            // l'Aviation Militaire
            ArmedService frenchArmy = createServiceFrenchArmy(startDate, endDate);
            frenchServices.add(frenchArmy);
            
            // l'Aviation Maritime
            ArmedService frenchNavy = createServiceFrenchNavy(startDate, endDate);
            frenchServices.add(frenchNavy);
            
            // Aviation Militaire Belge
            ArmedService belgianAF = createServiceBelgianArmy(startDate, endDate);
            frenchServices.add(belgianAF);
    
            // British
            List <ArmedService> britishServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(RoFCountry.BRITAIN_CODE, britishServices);
    
            // Royal Flying Corps (Britain)
            ArmedService rfc = createServiceRFC(startDate, rafStartDate);
            britishServices.add(rfc);
            
            // Royal Naval Air Service (Britain)
            ArmedService rnas = createServiceRNAS(rafStartDate);
            britishServices.add(rnas);
    
            // Royal Air Force (Britain)
            ArmedService raf = createServiceRAF(endDate, rafStartDate);
            britishServices.add(raf);           
            
            // American
            List <ArmedService> americanServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(RoFCountry.USA_CODE, americanServices);
    
            // United States Air Service (USA)
            ArmedService usas = createServiceUSAS(endDate);
            americanServices.add(usas);
            
            // Russian
            List <ArmedService> russianServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(RoFCountry.RUSSIA_CODE, russianServices);
    
            // Imperial Russian Air Service
            ArmedService iras = new ArmedService();
            createServiceRussianArmy(startDate, iras);
            russianServices.add(iras);

            // German
            List <ArmedService> germanServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(RoFCountry.GERMANY_CODE, germanServices);
    
            // Deutsche Luftstreitkräfte (Germany)
            ArmedService gas = new ArmedService();
            createServiceGermanArmy(startDate, endDate, gas);
            germanServices.add(gas);
    
            // Marine Flieger Korps (Germany)
            ArmedService mfj = createServiceGermanNavy(startDate, endDate);
            germanServices.add(mfj);
            
            
            // Austrian
            List <ArmedService> austrianServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(RoFCountry.AUSTRIA_CODE, austrianServices);
    
            // LFT
            ArmedService lft = createServiceAustrianArmy();
            austrianServices.add(lft);

        }
        catch (Exception e)
        {
            Logger.logException(e);
        }
    }

    private ArmedService createServiceAustrianArmy() throws PWCGException
    {
        ArmedService lft = new ArmedService();
        lft.setServiceId(AUSTRO_HUNGARIAN_AIR_SERVICE);
        lft.setCountry(new RoFCountry(RoFCountry.AUSTRIA_CODE));
        lft.setName(AUSTRO_HUNGARIAN_AIR_SERVICE_NAME);
        lft.setServiceIcon(AUSTRO_HUNGARIAN_AIR_SERVICE_ICON);
        lft.setStartDate(DateUtils.getDateWithValidityCheck("01/01/1916"));
        lft.setEndDate(DateUtils.getEndOfWWIRussia());
        lft.setServiceColorMap(new AustrianColorMap());
        lft.setGeneralRankForService("Feldmarschall-leutenant");
        lft.setDailyReplacementRate(4);

        List<String> lftPics = new ArrayList<String>();
        lftPics.add("austrian");
        lft.setPicDirs(lftPics);
        
        lft.addServiceQuality(DateUtils.getBeginningOfWar(), 50);
        
        return lft;
    }

    private ArmedService createServiceGermanNavy(Date startDate, Date endDate) throws PWCGException
    {
        ArmedService mfj = new ArmedService();
        mfj.setServiceId(MARINE_FLIEGER_CORP);
        mfj.setCountry(new RoFCountry(RoFCountry.GERMANY_CODE));
        mfj.setName(MARINE_FLIEGER_CORP_NAME);
        mfj.setServiceIcon(MARINE_FLIEGER_CORP_ICON);
        mfj.setStartDate(startDate);
        mfj.setEndDate(endDate);
        mfj.setServiceColorMap(new GermanNavyColorMap());
        mfj.setGeneralRankForService("Vizeadmiral");
        mfj.setDailyReplacementRate(2);

        List<String> mfjPics = new ArrayList<String>();
        mfjPics.add("German");
        mfjPics.add("German\\MFJ");
        mfj.setPicDirs(mfjPics);
        
        mfj.addServiceQuality(DateUtils.getBeginningOfWar(), 70);
        
        return mfj;
    }

    private ArmedService createServiceGermanArmy(Date startDate, Date endDate, ArmedService gas) throws PWCGException
    {
        gas.setServiceId(DEUTSCHE_LUFTSTREITKRAFTE);
        gas.setCountry(new RoFCountry(RoFCountry.GERMANY_CODE));
        gas.setName(DEUTSCHE_LUFTSTREITKRAFTE_NAME);
        gas.setServiceIcon(DEUTSCHE_LUFTSTREITKRAFTE_ICON);
        gas.setStartDate(startDate);
        gas.setEndDate(endDate);
        gas.setServiceColorMap(new GermanColorMap());
        gas.setGeneralRankForService("Generalleutnant");

        List<String> gasPics = new ArrayList<String>();
        gasPics.add("German");
        gasPics.add("German\\GAS");
        gas.setPicDirs(gasPics);
        
        gas.addServiceQuality(DateUtils.getBeginningOfWar(), 75);
        
        return gas;
    }

    private ArmedService createServiceRussianArmy(Date startDate, ArmedService iras) throws PWCGException
    {
        iras.setServiceId(RUSSIAN_AIR_SERVICE);
        iras.setCountry(new RoFCountry(RoFCountry.RUSSIA_CODE));
        iras.setName(RUSSIAN_AIR_SERVICE_NAME);
        iras.setServiceIcon(RUSSIAN_AIR_SERVICE_ICON);
        iras.setStartDate(startDate);
        iras.setEndDate(DateUtils.getEndOfWWIRussia());
        iras.setServiceColorMap(new RussianColorMap());
        iras.setGeneralRankForService("Lieutenant General");
        iras.setDailyReplacementRate(5);

        List<String> irasPics = new ArrayList<String>();
        irasPics.add("russian");
        iras.setPicDirs(irasPics);
        
        iras.addServiceQuality(DateUtils.getBeginningOfWar(), 10);
        
        return iras;
    }

    private ArmedService createServiceUSAS(Date endDate) throws PWCGException
    {
        ArmedService usas = new ArmedService();
        usas.setServiceId(USAS);
        usas.setCountry(new RoFCountry(RoFCountry.USA_CODE));
        usas.setName(USAS_NAME);
        usas.setServiceIcon(USAS_ICON);
        usas.setStartDate(DateUtils.getDateWithValidityCheck("01/05/1918"));
        usas.setEndDate(endDate);
        usas.setServiceColorMap(new AmericanColorMap());
        usas.setGeneralRankForService("Major-General");
        usas.setDailyReplacementRate(15);

        List<String> usasPics = new ArrayList<String>();
        usasPics.add("American");
        usas.setPicDirs(usasPics);

        usas.addServiceQuality(DateUtils.getBeginningOfWar(), 40);
        
        return usas;
    }

    private ArmedService createServiceRAF(Date endDate, Date rafStartDate) throws PWCGException
    {
        ArmedService raf = new ArmedService();
        raf.setServiceId(RAF);
        raf.setCountry(new RoFCountry(RoFCountry.BRITAIN_CODE));
        raf.setName(RAF_NAME);
        raf.setServiceIcon(RAF_ICON);
        raf.setStartDate(rafStartDate);
        raf.setEndDate(endDate);
        raf.setServiceColorMap(new RAFColorMap());
        raf.setGeneralRankForService("Major General");

        List<String> rafPics = new ArrayList<String>();
        rafPics.add("British");
        rafPics.add("British\\RFC");
        raf.setPicDirs(rafPics);

        raf.addServiceQuality(DateUtils.getBeginningOfWar(), 50);
        
        return raf;
    }

    private ArmedService createServiceRNAS(Date rafStartDate) throws PWCGException
    {
        ArmedService rnas = new ArmedService();
        rnas.setServiceId(RNAS);
        rnas.setCountry(new RoFCountry(RoFCountry.BRITAIN_CODE));
        rnas.setName(RNAS_NAME);
        rnas.setServiceIcon(RNAS_ICON);
        rnas.setStartDate(DateUtils.getDateWithValidityCheck("01/03/1916"));
        rnas.setEndDate(rafStartDate);  // RAF start date is the RNAS end date
        rnas.setServiceColorMap(new RNASColorMap());
        rnas.setGeneralRankForService("Rear Admiral");

        List<String> rnasPics = new ArrayList<String>();
        rnasPics.add("British");
        rnasPics.add("British\\RNAS");
        rnas.setPicDirs(rnasPics);

        rnas.addServiceQuality(DateUtils.getBeginningOfWar(), 60);
        
        return rnas;
    }

    private ArmedService createServiceRFC(Date startDate, Date rafStartDate) throws PWCGException
    {
        ArmedService rfc = new ArmedService();
        rfc.setServiceId(RFC);
        rfc.setCountry(new RoFCountry(RoFCountry.BRITAIN_CODE));
        rfc.setName(RFC_NAME);
        rfc.setServiceIcon(RFC_ICON);
        rfc.setStartDate(startDate);
        rfc.setEndDate(rafStartDate); // RAF start date is the RFC end date
        rfc.setServiceColorMap(new RFCColorMap());
        rfc.setGeneralRankForService("Major General");

        List<String> rfcPics = new ArrayList<String>();
        rfcPics.add("British");
        rfcPics.add("British\\RFC");
        rfc.setPicDirs(rfcPics);

        rfc.addServiceQuality(DateUtils.getBeginningOfWar(), 50);
        
        return rfc;
    }

    private ArmedService createServiceBelgianArmy(Date startDate, Date endDate) throws PWCGException
    {
        ArmedService belgianAF = new ArmedService();
        belgianAF.setServiceId(AVIATION_MILITAIRE_BELGE);
        belgianAF.setCountry(new RoFCountry(RoFCountry.BELGIUM_CODE));
        belgianAF.setName(AVIATION_MILITAIRE_BELGE_NAME);
        belgianAF.setServiceIcon(AVIATION_MILITAIRE_BELGE_ICON);
        belgianAF.setStartDate(startDate);
        belgianAF.setEndDate(endDate);
        belgianAF.setServiceColorMap(new BelgianColorMap());
        belgianAF.setGeneralRankForService("General de Brigade");
        belgianAF.setDailyReplacementRate(1);

        List<String> belgianPics = new ArrayList<String>();
        belgianPics.add("Belgian");
        belgianAF.setPicDirs(belgianPics);

        belgianAF.addServiceQuality(DateUtils.getBeginningOfWar(), 40);
        
        return belgianAF;
    }

    private ArmedService createServiceFrenchNavy(Date startDate, Date endDate) throws PWCGException
    {
        ArmedService frenchNavy = new ArmedService();
        frenchNavy.setServiceId(LAVIATION_MARINE);
        frenchNavy.setCountry(new RoFCountry(RoFCountry.FRANCE_CODE));
        frenchNavy.setName(LAVIATION_MARINE_NAME);
        frenchNavy.setServiceIcon(LAVIATION_MARINE_ICON);
        frenchNavy.setStartDate(startDate);
        frenchNavy.setEndDate(endDate);
        frenchNavy.setServiceColorMap(new FrenchNavyColorMap());
        frenchNavy.setGeneralRankForService("Contre-Amiral");

        List<String> frenchNavyPics = new ArrayList<String>();
        frenchNavyPics.add("French");
        frenchNavy.setPicDirs(frenchNavyPics);

        frenchNavy.addServiceQuality(DateUtils.getBeginningOfWar(), 35);
        
        return frenchNavy;
    }

    private ArmedService createServiceFrenchArmy(Date startDate, Date endDate) throws PWCGException
    {
        ArmedService frenchArmy = new ArmedService();
        frenchArmy.setServiceId(LAVIATION_MILITAIRE);
        frenchArmy.setCountry(new RoFCountry(RoFCountry.FRANCE_CODE));
        frenchArmy.setName(LAVIATION_MILITAIRE_NAME);
        frenchArmy.setServiceIcon(LAVIATION_MILITAIRE_ICON);
        frenchArmy.setStartDate(startDate);
        frenchArmy.setEndDate(endDate);
        frenchArmy.setServiceColorMap(new FrenchColorMap());
        frenchArmy.setGeneralRankForService("General de Brigade");

        List<String> frenchArmyPics = new ArrayList<String>();
        frenchArmyPics.add("French");
        frenchArmy.setPicDirs(frenchArmyPics);

        frenchArmy.addServiceQuality(DateUtils.getBeginningOfWar(), 40);
        
        return frenchArmy;
    }

	@Override
    public ArmedService getArmedServiceById(int serviceId, Date campaignDate) throws PWCGException 
	{
		if (serviceId == RFC || serviceId == RNAS)
		{
			if (campaignDate.before(DateUtils.getRAFDate()))
			{
			}
			else
			{
				serviceId = RAF;
			}
		}
			
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
		if (serviceName.equals(RFC_NAME) || serviceName.equals(RNAS_NAME))
		{
			if (campaignDate.before(DateUtils.getRAFDate()))
			{
			}
			else
			{
				serviceName = RAF_NAME;
			}
		}
			
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
    public List<ArmedService> getAlliedServices(Date date) throws PWCGException
    {
        List<ArmedService> alliedServices = new ArrayList<ArmedService>();
        
        for (int countryCode : armedServicesByCountry.keySet())
        {
            if (CountryFactory.makeCountryByCode(countryCode).getSide() == Side.ALLIED)
            {
                List<ArmedService> servicesForCountry = armedServicesByCountry.get(countryCode);
                
                for (ArmedService service: servicesForCountry)
                {
                    // RFC and RNAS before transition
                    if (service.getName().equals(RoFServiceManager.RFC) || service.getName().equals(RoFServiceManager.RNAS))
                    {
                        if (date.before(DateUtils.getRAFDate()))
                        {
                            alliedServices.add(service);
                        }
                    }
                    // RAF before transition
                    else if (service.getName().equals(RoFServiceManager.RAF))
                    {
                        if (!date.before(DateUtils.getRAFDate()))
                        {
                            alliedServices.add(service);
                        }
                    }
                    // All other allied just add
                    else
                    {
                        alliedServices.add(service);
                    }
                }
            }
        }
        
        return alliedServices;
    }

    @Override
    public ArmedService getArmedService(int serviceId) throws PWCGException
    {
        return super.getArmedService(serviceId);
    }

    @Override
    public List<ArmedService> getAllArmedServices() throws PWCGException
    {
        return super.getAllArmedServices();
    }

    @Override
    public List<ArmedService> getAxisServices(Date date) throws PWCGException
    {
        return super.getAxisServices(date);
    }

    @Override
    public ArmedService getPrimaryServiceForNation(Country country, Date date) throws PWCGException
    {
        if (country == Country.AUSTRIA)
        {
            return(getArmedServiceById(AUSTRO_HUNGARIAN_AIR_SERVICE, date));
        }
        else if (country == Country.BELGIUM)
        {
            return(getArmedServiceById(AVIATION_MILITAIRE_BELGE, date));
        }
        else if (country == Country.GERMANY)
        {
            return(getArmedServiceById(DEUTSCHE_LUFTSTREITKRAFTE, date));
        }
        else if (country == Country.FRANCE)
        {
            return(getArmedServiceById(LAVIATION_MILITAIRE, date));
        }
        else if (country == Country.RUSSIA)
        {
            return(getArmedServiceById(RUSSIAN_AIR_SERVICE, date));
        }
        else if (country == Country.USA)
        {
            return(getArmedServiceById(USAS, date));
        }
        else if (country == Country.BRITAIN)
        {
            if (!date.before(DateUtils.getRAFDate()))
            {
                return(getArmedServiceById(RFC, date));
            }
            else
            {
                return(getArmedServiceById(RAF, date));
            }
        }
        
        throw new PWCGException("Unexpected country for getPrimaryServiceForNation " + country);
    }

}
