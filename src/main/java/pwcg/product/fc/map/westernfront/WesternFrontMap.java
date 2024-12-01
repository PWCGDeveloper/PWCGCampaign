package pwcg.product.fc.map.westernfront;

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
import pwcg.product.bos.map.IMapClimate;
import pwcg.product.bos.map.IMapSeason;
import pwcg.product.fc.country.FCServiceManager;

public class WesternFrontMap extends PWCGMap
{
    private static final Map<String, Integer> missionSpacingMyDate;
    static
    { 
        missionSpacingMyDate = new TreeMap<>(); 
        missionSpacingMyDate.put("19160101", 7); 
        missionSpacingMyDate.put("19160801", 6); 
        missionSpacingMyDate.put("19160901", 6); 
        missionSpacingMyDate.put("19161001", 6); 
        missionSpacingMyDate.put("19161101", 7); 
        missionSpacingMyDate.put("19161201", 8); 
        missionSpacingMyDate.put("19170101", 8); 
        missionSpacingMyDate.put("19170201", 7); 
        missionSpacingMyDate.put("19170301", 3); 
        missionSpacingMyDate.put("19170401", 2); 
        missionSpacingMyDate.put("19170501", 2); 
        missionSpacingMyDate.put("19170601", 2); 
        missionSpacingMyDate.put("19170701", 2); 
        missionSpacingMyDate.put("19170801", 2); 
        missionSpacingMyDate.put("19171120", 1); 
        missionSpacingMyDate.put("19171203", 4); 
        missionSpacingMyDate.put("19180101", 6); 
        missionSpacingMyDate.put("19180201", 4); 
        missionSpacingMyDate.put("19180301", 3); 
        missionSpacingMyDate.put("19180321", 1); 
        missionSpacingMyDate.put("19180429", 2); 
        missionSpacingMyDate.put("19180528", 1); 
        missionSpacingMyDate.put("19180610", 2); 
        missionSpacingMyDate.put("19180706", 3); 
        missionSpacingMyDate.put("19180815", 1); 
        missionSpacingMyDate.put("19180920", 2); 
        missionSpacingMyDate.put("19181101", 3); 
    } 


    public WesternFrontMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapIdentifier = FrontMapIdentifier.WESTERN_FRONT_MAP;
        
        mapArea = new WesternFrontMapArea();
        usableMapArea = new WesternFrontMapUsableArea();

        buildArmedServicesActiveForMap();
        
        super.configure();
    }

    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(FCServiceManager.AVIATION_MILITAIRE_BELGE);
        armedServicesActiveForMap.add(FCServiceManager.LAVIATION_MILITAIRE);
        armedServicesActiveForMap.add(FCServiceManager.RAF);
        armedServicesActiveForMap.add(FCServiceManager.RFC);
        armedServicesActiveForMap.add(FCServiceManager.RNAS);
        armedServicesActiveForMap.add(FCServiceManager.USAS);
        armedServicesActiveForMap.add(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        // This controls the available start dates for campaign generation
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19181111"));
        
        this.frontDatesForMap.addFrontDate("19160101"); // Start
        this.frontDatesForMap.addFrontDate("19180301"); // Kaiserschlacht phase 1
        this.frontDatesForMap.addFrontDate("19180401"); // Kaiserschlacht phase 1
        this.frontDatesForMap.addFrontDate("19180501"); // Kaiserschlacht phase 1
        this.frontDatesForMap.addFrontDate("19180601"); // Kaiserschlacht phase 2
        this.frontDatesForMap.addFrontDate("19180701"); // Kaiserschlacht phase 2
        this.frontDatesForMap.addFrontDate("19180901"); // 100 days
        this.frontDatesForMap.addFrontDate("19181001"); // 100 days
        this.frontDatesForMap.addFrontDate("19181101"); // 100 days
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
        return new WesternFrontMapClimate();
    }

    @Override
    protected IMapSeason buildMapSeason()
    {
        return new WesternFrontMapSeason();
    }

    @Override
    public int getRainChances()
    {
        return 15;
    }
}
