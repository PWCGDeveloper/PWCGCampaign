package pwcg.product.bos.map.stalingrad;

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

public class StalingradMap extends PWCGMap
{
    private static final Map<String, Integer> missionSpacingMyDate;
    static
    { 
        missionSpacingMyDate = new TreeMap<>(); 
        missionSpacingMyDate.put("19420801", 3); 
        missionSpacingMyDate.put("19420823", 2); 
        missionSpacingMyDate.put("19420905", 1); 
        missionSpacingMyDate.put("19420927", 2); 
        missionSpacingMyDate.put("19421014", 1); 
        missionSpacingMyDate.put("19421119", 1); 
        missionSpacingMyDate.put("19421225", 2); 
        missionSpacingMyDate.put("19430120", 3); 
        missionSpacingMyDate.put("19430202", 4); 
    } 

    public StalingradMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapIdentifier = FrontMapIdentifier.STALINGRAD_MAP;

        mapArea = new StalingradMapArea();
        usableMapArea = new StalingradMapUsableArea();

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
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19420301"), DateUtils.getDateYYYYMMDD("19430202"));

        this.frontDatesForMap.addFrontDate("19420301");
        this.frontDatesForMap.addFrontDate("19420801");
        this.frontDatesForMap.addFrontDate("19420906");
        this.frontDatesForMap.addFrontDate("19421011");
        this.frontDatesForMap.addFrontDate("19421123");
        this.frontDatesForMap.addFrontDate("19421223");
        this.frontDatesForMap.addFrontDate("19430120");
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
        return new StalingradMapClimate();
    }

    @Override
    protected IMapSeason buildMapSeason()
    {
        return new StalingradMapSeason();
    }

    @Override
    public int getRainChances()
    {
        return 15;
    }
}
