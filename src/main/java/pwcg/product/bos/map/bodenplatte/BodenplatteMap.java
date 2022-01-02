package pwcg.product.bos.map.bodenplatte;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.product.bos.map.IMapClimate;
import pwcg.product.bos.map.IMapSeason;

public class BodenplatteMap extends PWCGMap
{
    private static final Map<String, Integer> missionSpacingMyDate;
    static
    { 
        missionSpacingMyDate = new TreeMap<>(); 
        missionSpacingMyDate.put("19440901", 3); 
        missionSpacingMyDate.put("19440917", 1); 
        missionSpacingMyDate.put("19440930", 3); 
        missionSpacingMyDate.put("19441216", 1); 
        missionSpacingMyDate.put("19450125", 2); 
        missionSpacingMyDate.put("19450220", 3); 
        missionSpacingMyDate.put("19450310", 2); 
        missionSpacingMyDate.put("19450323", 1); 
        missionSpacingMyDate.put("19450324", 1); 
        missionSpacingMyDate.put("19450401", 5); 
    } 

    public BodenplatteMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapIdentifier = FrontMapIdentifier.BODENPLATTE_MAP;
        
        this.mapClimate = new BodenplatteMapClimate();
        
        mapArea = new BodenplatteMapArea();
        usableMapArea = new BodenplatteMapUsableArea();

        buildArmedServicesActiveForMap();
        
        super.configure();
    }

    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(BoSServiceManager.US_ARMY);
        armedServicesActiveForMap.add(BoSServiceManager.BRITISH_ARMY);
        armedServicesActiveForMap.add(BoSServiceManager.WEHRMACHT);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19440901"), DateUtils.getDateYYYYMMDD("19450503"));

        this.frontDatesForMap.addFrontDate("19440901");
        this.frontDatesForMap.addFrontDate("19440917");
        this.frontDatesForMap.addFrontDate("19440925");
        this.frontDatesForMap.addFrontDate("19440928");
        this.frontDatesForMap.addFrontDate("19440930");
        this.frontDatesForMap.addFrontDate("19441101");
        this.frontDatesForMap.addFrontDate("19441220");
        this.frontDatesForMap.addFrontDate("19441225");
        this.frontDatesForMap.addFrontDate("19441229");
        this.frontDatesForMap.addFrontDate("19450207");
        this.frontDatesForMap.addFrontDate("19450310");
        this.frontDatesForMap.addFrontDate("19450323");
        this.frontDatesForMap.addFrontDate("19450324");
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

    @Override
    protected IMapClimate buildMapClimate()
    {
        return new BodenplatteMapClimate();
    }

    @Override
    protected IMapSeason buildMapSeason()
    {
        return new BodenplatteMapSeason();
    }

    @Override
    public int getRainChances()
    {
        return 25;
    }
}
