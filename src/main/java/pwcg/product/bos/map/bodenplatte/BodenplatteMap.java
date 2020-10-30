package pwcg.product.bos.map.bodenplatte;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.BoSServiceManager;

public class BodenplatteMap extends PWCGMap
{
    private static final Map<String, Integer> missionSpacingMyDate;
    static
    { 
        missionSpacingMyDate = new TreeMap<>(); 
        missionSpacingMyDate.put("19440901", 3); 
        missionSpacingMyDate.put("19441216", 1); 
        missionSpacingMyDate.put("19450125", 2); 
        missionSpacingMyDate.put("19450220", 3); 
        missionSpacingMyDate.put("19450401", 5); 
    } 

    public BodenplatteMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = BODENPLATTE_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.BODENPLATTE_MAP;
        
        this.missionOptions = new BodenplatteMissionOptions();
        this.mapWeather = new BodenplatteMapWeather();
        
        mapArea = new BodenplatteMapArea();
        usableMapArea = new BodenplatteMapUsableArea();

        buildArmedServicesActiveForMap();
        
        super.configure();
    }
    
    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(BoSServiceManager.USAAF);
        armedServicesActiveForMap.add(BoSServiceManager.RAF);
        armedServicesActiveForMap.add(BoSServiceManager.FREE_FRENCH);
        armedServicesActiveForMap.add(BoSServiceManager.LUFTWAFFE);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19440901"), DateUtils.getDateYYYYMMDD("19450503"));

        this.frontDatesForMap.addFrontDate("19440901");
        this.frontDatesForMap.addFrontDate("19441001");
        this.frontDatesForMap.addFrontDate("19441101");
        this.frontDatesForMap.addFrontDate("19441220");
        this.frontDatesForMap.addFrontDate("19441225");
        this.frontDatesForMap.addFrontDate("19441229");
        this.frontDatesForMap.addFrontDate("19450207");
        this.frontDatesForMap.addFrontDate("19450310");
        this.frontDatesForMap.addFrontDate("19450404");
    }

    @Override
    public ICountry getGroundCountryForMapBySide(Side side)
    {
        if (side == Side.ALLIED)
        {
            return CountryFactory.makeCountryByCountry(Country.USA);
        }
        else
        {
            return CountryFactory.makeCountryByCountry(Country.GERMANY);
        }
    }

    @Override
    protected Map<String, Integer> getMissionSpacingMyDate()
    {
        return missionSpacingMyDate;
    }
}
