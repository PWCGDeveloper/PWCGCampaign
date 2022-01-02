package pwcg.product.bos.map.east1945;

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

public class East1945Map extends PWCGMap
{
    private static final Map<String, Integer> missionSpacingMyDate;
    static
    {
        missionSpacingMyDate = new TreeMap<>();
        missionSpacingMyDate.put("19450101", 3);
        missionSpacingMyDate.put("19450201", 2);
        missionSpacingMyDate.put("19450301", 1);
    }

    public East1945Map()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapIdentifier = FrontMapIdentifier.EAST1945_MAP;

        mapArea = new East1945MapArea();
        usableMapArea = new East1945MapUsableArea();

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
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19450101"), DateUtils.getDateYYYYMMDD("19450503"));

        this.frontDatesForMap.addFrontDate("19450101");
        this.frontDatesForMap.addFrontDate("19450201");
        this.frontDatesForMap.addFrontDate("19450301");
        this.frontDatesForMap.addFrontDate("19450401");
        this.frontDatesForMap.addFrontDate("19450501");
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
        return new East1945MapClimate();
    }

    @Override
    protected IMapSeason buildMapSeason()
    {
        return new East1945MapSeason();
    }

    @Override
    public int getRainChances()
    {
        return 25;
    }
}
