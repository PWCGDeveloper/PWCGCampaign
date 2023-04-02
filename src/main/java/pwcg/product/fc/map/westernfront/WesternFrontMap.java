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
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19170601"), DateUtils.getDateYYYYMMDD("19181111"));
        this.frontDatesForMap.addFrontDate("19170601");
        this.frontDatesForMap.addFrontDate("19180329");
        this.frontDatesForMap.addFrontDate("19180404");
        this.frontDatesForMap.addFrontDate("19180429");
        this.frontDatesForMap.addFrontDate("19180531");
        this.frontDatesForMap.addFrontDate("19180604");
        this.frontDatesForMap.addFrontDate("19180612");
        this.frontDatesForMap.addFrontDate("19180918");
        this.frontDatesForMap.addFrontDate("19180925");
        this.frontDatesForMap.addFrontDate("19181004");
        this.frontDatesForMap.addFrontDate("19181015");
        this.frontDatesForMap.addFrontDate("19181101");
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
