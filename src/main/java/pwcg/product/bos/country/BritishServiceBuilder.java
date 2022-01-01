package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceType;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.colors.AmericanColorMap;
import pwcg.gui.colors.FrenchColorMap;
import pwcg.gui.colors.RAFColorMap;

public class BritishServiceBuilder
{

    public static String RAF_NAME ="Royal Air Force";
    public static String RAF_ICON ="ServiceRAF";

    public static String RCAF_NAME ="Royal Canadian Air Force";
    public static String RCAF_ICON ="ServiceRCAF";

    public static String FREE_FRENCH_NAME ="Free French";
    public static String FREE_FRENCH_NAME_ICON ="ServiceFreeFrench";

    public static String BRITISH_ARMY_NAME ="British Army";
    public static String BRITISH_ARMY_ICON ="ServiceBritishArmy";

    public static List<ArmedService> createServices() throws PWCGException
    {
        List<ArmedService> services = new ArrayList<ArmedService>();
        services.add(createRAF());
        services.add(createFreeFrench());
        services.add(createRCAF());
        services.add(createBritishArmy());
        return services;
    }

    private static ArmedService createRAF() throws PWCGException
    {
        ArmedService raf = new ArmedService();
        raf.setServiceId(BoSServiceManager.RAF);
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

        raf.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_AIR);

        return raf;
    }

    private static ArmedService createFreeFrench() throws PWCGException
    {
        ArmedService freeFrench = new ArmedService();
        freeFrench.setServiceId(BoSServiceManager.FREE_FRENCH);
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
        
        freeFrench.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_AIR);

        return freeFrench;
    }

    private static ArmedService createRCAF() throws PWCGException
    {
        ArmedService rcaf = new ArmedService();
        rcaf.setServiceId(BoSServiceManager.RCAF);
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

        rcaf.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_AIR);

        return rcaf;
    }

    private static ArmedService createBritishArmy() throws PWCGException
    {
        ArmedService usArmy = new ArmedService();
        usArmy.setServiceId(BoSServiceManager.BRITISH_ARMY);
        usArmy.setCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        usArmy.setNameCountry(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        usArmy.setName(BRITISH_ARMY_NAME);
        usArmy.setServiceIcon(BRITISH_ARMY_ICON);
        usArmy.setEndDate(DateUtils.getEndOfWar());
        usArmy.setServiceColorMap(new AmericanColorMap());
        usArmy.setGeneralRankForService("General");
        usArmy.setStartDate(DateUtils.getDateYYYYMMDD("19440901"));
        usArmy.setDailyPersonnelReplacementRatePerSquadron(3.0);
        usArmy.setDailyEquipmentReplacementRatePerSquadron(3.0);

        List<String> usArmyPics = new ArrayList<String>();
        usArmyPics.add("British");
        usArmy.setPicDirs(usArmyPics);

        usArmy.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 50);
        usArmy.addServiceQuality(DateUtils.getDateYYYYMMDD("19400101"), 60);
        usArmy.addServiceQuality(DateUtils.getDateYYYYMMDD("19410101"), 60);
        usArmy.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 60);
        usArmy.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 70);
        usArmy.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 75);
        usArmy.addServiceQuality(DateUtils.getDateYYYYMMDD("19450101"), 75);

        usArmy.setAirVictoriesForgreatAce(20);
        usArmy.setGroundVictoriesForgreatAce(50);

        usArmy.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_GROUND);

        return usArmy;
    }
}
