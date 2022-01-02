package pwcg.product.bos.map.moscow;

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

public class MoscowMap extends PWCGMap
{
    private static final Map<String, Integer> missionSpacingMyDate;
    static
    { 
        missionSpacingMyDate = new TreeMap<>(); 
        missionSpacingMyDate.put("19411001", 2); 
        missionSpacingMyDate.put("19411003", 1); 
        missionSpacingMyDate.put("19411012", 2); 
        missionSpacingMyDate.put("19411016", 1); 
        missionSpacingMyDate.put("19411024", 2); 
        missionSpacingMyDate.put("19421205", 2); 
        missionSpacingMyDate.put("19420101", 3); 
        missionSpacingMyDate.put("19420110", 4); 
        missionSpacingMyDate.put("19420201", 5); 
    } 

    public MoscowMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapIdentifier = FrontMapIdentifier.MOSCOW_MAP;

        mapArea = new MoscowMapArea();
        usableMapArea = new MoscowMapUsableArea();
        buildArmedServicesActiveForMap();
        super.configure();
    }

    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(BoSServiceManager.SVV);
        armedServicesActiveForMap.add(BoSServiceManager.WEHRMACHT);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19411001"), DateUtils.getDateYYYYMMDD("19420301"));

        this.frontDatesForMap.addFrontDate("19411001");
        this.frontDatesForMap.addFrontDate("19411005");
        this.frontDatesForMap.addFrontDate("19411011");
        this.frontDatesForMap.addFrontDate("19411016");
        this.frontDatesForMap.addFrontDate("19411020");
        this.frontDatesForMap.addFrontDate("19411110");
        this.frontDatesForMap.addFrontDate("19411120");
        this.frontDatesForMap.addFrontDate("19411215");
        this.frontDatesForMap.addFrontDate("19420110");
    }

    @Override
    public ICountry getGroundCountryForMapBySide(Side side)
    {
        if (side == Side.ALLIED)
        {
            return CountryFactory.makeCountryByCountry(Country.RUSSIA);
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
        return new MoscowMapClimate();
    }

    @Override
    protected IMapSeason buildMapSeason()
    {
        return new MoscowMapSeason();
    }

    @Override
    public int getRainChances()
    {
        return 15;
    }
}
