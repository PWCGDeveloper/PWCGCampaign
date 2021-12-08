package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceType;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.colors.FrenchColorMap;
import pwcg.gui.colors.VVSColorMap;

public class RussianServiceBuilder
{
    private static String VVS_NAME ="Voyenno-Vozdushnye Sily";
    private static String NORMANDIE_NAME = "Normandie";
    private static String RUSSIAN_ARMY_NAME ="Sovetskiye Sukhoputnye Voyska";
    
    private static String VVS_ICON ="ServiceVVS";
    private static String NORMANDIE_ICON ="ServiceNormandie";
    private static String RUSSIAN_ARMY_ICON ="ServiceSSV";

    public static List <ArmedService> createRussianAirServices() throws PWCGException
    {
        List <ArmedService> russianServices = new ArrayList<ArmedService>();
        russianServices.add(createVVS());
        russianServices.add(createNormandie());
        russianServices.add(createRussianArmy());
        return russianServices;
    }

    private static ArmedService createVVS() throws PWCGException
    {
        ArmedService vvs = new ArmedService();
        vvs.setServiceId(BoSServiceManager.VVS);
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
        
        vvs.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_AIR);

        return vvs;
    }

    private static ArmedService createNormandie() throws PWCGException
    {
        ArmedService normandie = new ArmedService();
        normandie.setServiceId(BoSServiceManager.NORMANDIE);
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

        normandie.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_AIR);

        return normandie;
    }

    private static ArmedService createRussianArmy() throws PWCGException
    {
        ArmedService svv = new ArmedService();
        svv.setServiceId(BoSServiceManager.RUSSIAN_ARMY);
        svv.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        svv.setNameCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        svv.setName(RUSSIAN_ARMY_NAME);
        svv.setServiceIcon(RUSSIAN_ARMY_ICON);
        svv.setStartDate(DateUtils.getBeginningOfGame());
        svv.setEndDate(DateUtils.getEndOfWar());
        svv.setServiceColorMap(new VVSColorMap());
        svv.setGeneralRankForService("General-lieutenant");
        svv.setDailyPersonnelReplacementRatePerSquadron(2.2);
        svv.setDailyEquipmentReplacementRatePerSquadron(3.0);

        List<String> irasPics = new ArrayList<String>();
        irasPics.add("Russian");
        svv.setPicDirs(irasPics);
        
        svv.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 10);
        svv.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 20);
        svv.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 40);
        svv.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 50);
        
        svv.setAirVictoriesForgreatAce(20);
        svv.setGroundVictoriesForgreatAce(100);

        svv.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_GROUND);

        return svv;
    }
}
