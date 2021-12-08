package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceType;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.colors.GermanColorMap;

public class GermanServiceBuilder
{
    public static String LUFTWAFFE_NAME = "Luftwaffe";
    public static String LUFTWAFFE_ICON ="ServiceLuftwaffe";

    public static String WEHRMACHT_NAME = "Wehrmacht";
    public static String WEHRMACHT_ICON = "ServiceWehrmacht";

    public static List <ArmedService> createGermanAirServices() throws PWCGException
    {
        List <ArmedService> germanServices = new ArrayList<ArmedService>();
        germanServices.add(createLuftwaffe());
        germanServices.add(createGermanArmy());
        return germanServices;
    }


    private static ArmedService createLuftwaffe() throws PWCGException
    {
        ArmedService luftwaffe = new ArmedService();
        luftwaffe.setServiceId(BoSServiceManager.LUFTWAFFE);
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

        luftwaffe.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_AIR);

        return luftwaffe;
    }

    private static ArmedService createGermanArmy() throws PWCGException
    {
        ArmedService wehrmacht = new ArmedService();
        wehrmacht.setServiceId(BoSServiceManager.WEHRMACHT);
        wehrmacht.setCountry(CountryFactory.makeCountryByCountry(Country.GERMANY));
        wehrmacht.setNameCountry(CountryFactory.makeCountryByCountry(Country.GERMANY));
        wehrmacht.setName(WEHRMACHT_NAME);
        wehrmacht.setServiceIcon(WEHRMACHT_ICON);
        wehrmacht.setStartDate(DateUtils.getBeginningOfGame());
        wehrmacht.setEndDate(DateUtils.getEndOfWar());
        wehrmacht.setServiceColorMap(new GermanColorMap());
        wehrmacht.setGeneralRankForService("Generalleutnant");

        List<String> irasPics = new ArrayList<String>();
        irasPics.add("German");
        wehrmacht.setPicDirs(irasPics);
        
        wehrmacht.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 80);
        wehrmacht.addServiceQuality(DateUtils.getDateYYYYMMDD("19400101"), 90);
        wehrmacht.addServiceQuality(DateUtils.getDateYYYYMMDD("19410101"), 90);
        wehrmacht.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 90);
        wehrmacht.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 70);
        wehrmacht.addServiceQuality(DateUtils.getDateYYYYMMDD("19440601"), 70);

        wehrmacht.setDailyPersonnelReplacementRatePerSquadron(1.3);
        wehrmacht.setDailyEquipmentReplacementRatePerSquadron(1.0);
        
        wehrmacht.setAirVictoriesForgreatAce(20);
        wehrmacht.setGroundVictoriesForgreatAce(50);

        wehrmacht.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_GROUND);

        return wehrmacht;
    }
}
