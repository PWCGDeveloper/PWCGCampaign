package pwcg.product.bos.map.kuban;

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

public class KubanMap extends PWCGMap
{
    private static final Map<String, Integer> missionSpacingMyDate;
    static
    { 
        missionSpacingMyDate = new TreeMap<>(); 
        missionSpacingMyDate.put("19420601", 2); 
        missionSpacingMyDate.put("19420715", 1); 
        missionSpacingMyDate.put("19420801", 4); 
        missionSpacingMyDate.put("19430101", 1); 
        missionSpacingMyDate.put("19430218", 4); 
        missionSpacingMyDate.put("19430909", 1); 
        missionSpacingMyDate.put("19431008", 2); 
        missionSpacingMyDate.put("19431030", 4); 
    } 

    public KubanMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapIdentifier = FrontMapIdentifier.KUBAN_MAP;

        mapArea = new KubanMapArea();
        usableMapArea = new KubanMapUsableArea();

        buildArmedServicesActiveForMap();
        
        super.configure();
    }

    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(BoSServiceManager.VVS);
        armedServicesActiveForMap.add(BoSServiceManager.NORMANDIE);
        armedServicesActiveForMap.add(BoSServiceManager.LUFTWAFFE);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19420601"), DateUtils.getDateYYYYMMDD("19431231"));
        
        this.frontDatesForMap.addFrontDate("19420601");
        this.frontDatesForMap.addFrontDate("19420624");
        this.frontDatesForMap.addFrontDate("19420709");
        this.frontDatesForMap.addFrontDate("19420721");
        this.frontDatesForMap.addFrontDate("19430301");
        this.frontDatesForMap.addFrontDate("19430330");
        this.frontDatesForMap.addFrontDate("19430418");
        this.frontDatesForMap.addFrontDate("19430918");
        this.frontDatesForMap.addFrontDate("19430927");
        this.frontDatesForMap.addFrontDate("19431004");
        this.frontDatesForMap.addFrontDate("19431008");
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
        return new KubanMapClimate();
    }

    @Override
    protected IMapSeason buildMapSeason()
    {
        return new KubanMapSeason();
    }

    @Override
    public int getRainChances()
    {
        return 25;
    }
}
