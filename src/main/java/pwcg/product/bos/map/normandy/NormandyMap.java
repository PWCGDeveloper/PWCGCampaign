package pwcg.product.bos.map.normandy;

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

public class NormandyMap extends PWCGMap
{
    private static final Map<String, Integer> missionSpacingMyDate;
    static
    { 
        missionSpacingMyDate = new TreeMap<>(); 
        missionSpacingMyDate.put("19410601", 1); 
        missionSpacingMyDate.put("19410610", 6); 
        missionSpacingMyDate.put("19410717", 3); 
        missionSpacingMyDate.put("19410813", 2); 
        missionSpacingMyDate.put("19410902", 4); 
        missionSpacingMyDate.put("19411101", 6); 
        missionSpacingMyDate.put("19420818", 1); 
        missionSpacingMyDate.put("19420821", 7); 
        missionSpacingMyDate.put("19440501", 2); 
        missionSpacingMyDate.put("19440601", 1); 
        missionSpacingMyDate.put("19440801", 2); 
    } 

    public NormandyMap()
    {
        super();
        hasShips = true;
    }

    public void configure() throws PWCGException
    {
        this.mapIdentifier = FrontMapIdentifier.NORMANDY_MAP;
        
        this.mapClimate = new NormandyMapClimate();
        
        mapArea = new NormandyMapArea();
        usableMapArea = new NormandyMapUsableArea();

        buildArmedServicesActiveForMap();
        
        super.configure();
    }

    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(BoSServiceManager.USAAF);
        armedServicesActiveForMap.add(BoSServiceManager.RAF);
        armedServicesActiveForMap.add(BoSServiceManager.RCAF);
        armedServicesActiveForMap.add(BoSServiceManager.FREE_FRENCH);
        armedServicesActiveForMap.add(BoSServiceManager.LUFTWAFFE);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19410601"), DateUtils.getDateYYYYMMDD("19440901"));

        this.frontDatesForMap.addFrontDate("19410601"); // Dunkirk
        this.frontDatesForMap.addFrontDate("19410610"); // BoB shipping phase
        this.frontDatesForMap.addFrontDate("19410717"); // BoB Aiirfield + Shipping phase
        this.frontDatesForMap.addFrontDate("19410813"); // BoB Airfield Phase
        this.frontDatesForMap.addFrontDate("19410902"); // BoB City Phase
        this.frontDatesForMap.addFrontDate("19411101"); // End of BoB
        this.frontDatesForMap.addFrontDate("19420819"); // Dieppe
        this.frontDatesForMap.addFrontDate("19420820"); // Day after Dieppe
        this.frontDatesForMap.addFrontDate("19430601"); // US in Britain
        this.frontDatesForMap.addFrontDate("19440501"); // D-Day Prep
        this.frontDatesForMap.addFrontDate("19440606"); // D-Day + 2
        this.frontDatesForMap.addFrontDate("19440609"); // Initial bulge
        this.frontDatesForMap.addFrontDate("19440620"); // Cherbourg cut off
        this.frontDatesForMap.addFrontDate("19440627"); // Cherbourg taken
        this.frontDatesForMap.addFrontDate("19440721"); // Begin St Lo and Caen taken
        this.frontDatesForMap.addFrontDate("19440731"); // Complete St Lo to Avranches
        this.frontDatesForMap.addFrontDate("19440808"); // Mortain
        this.frontDatesForMap.addFrontDate("19440813"); // Falaise
        this.frontDatesForMap.addFrontDate("19440821"); // End Falaise
        this.frontDatesForMap.addFrontDate("19440901"); // Done
   }

    @Override
    public ICountry getGroundCountryForMapBySide(Side side)
    {
        if (side == Side.ALLIED)
        {
            return CountryFactory.makeCountryByCountry(Country.BRITAIN);
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
        return new NormandyMapClimate();
    }

    @Override
    protected IMapSeason buildMapSeason()
    {
        return new NormandyMapSeason();
    }

    @Override
    public int getRainChances()
    {
        return 25;
    }
}
