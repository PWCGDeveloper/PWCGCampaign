package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceType;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.colors.ItalianColorMap;

public class ItalianServiceBuilder
{
    public static String REGIA_AERONAUTICA_NAME ="Regia Aeronautica";
    public static String REGIA_AERONAUTICA_ICON ="ServiceRA";

    public static List <ArmedService> createServices() throws PWCGException
    {
        List <ArmedService> italianServices = new ArrayList<ArmedService>();
        italianServices.add(createItalianAirServices());
        return italianServices;
    }
    
    private static ArmedService createItalianAirServices() throws PWCGException
    {   
        ArmedService regiaAeronautica = new ArmedService();
        regiaAeronautica.setServiceId(BoSServiceManager.REGIA_AERONAUTICA);
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

        regiaAeronautica.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_AIR);

        return regiaAeronautica;
    }
}
