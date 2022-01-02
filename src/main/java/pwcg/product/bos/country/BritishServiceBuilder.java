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

public class BritishServiceBuilder
{

    public static String BRITISH_ARMY_NAME ="British Army";
    public static String BRITISH_ARMY_ICON ="ServiceBritishArmy";

    public static List<ArmedService> createServices() throws PWCGException
    {
        List<ArmedService> services = new ArrayList<ArmedService>();
        services.add(createBritishArmy());
        return services;
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
