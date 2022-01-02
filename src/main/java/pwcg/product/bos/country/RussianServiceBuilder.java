package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceType;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.colors.VVSColorMap;

public class RussianServiceBuilder
{
    private static String RUSSIAN_ARMY_NAME ="Sovetskiye Sukhoputnye Voyska";
    private static String RUSSIAN_ARMY_ICON ="ServiceSSV";

    public static List <ArmedService> createServices() throws PWCGException
    {
        List <ArmedService> russianServices = new ArrayList<ArmedService>();
        russianServices.add(createRussianArmy());
        return russianServices;
    }

    private static ArmedService createRussianArmy() throws PWCGException
    {
        ArmedService svv = new ArmedService();
        svv.setServiceId(BoSServiceManager.SVV);
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
