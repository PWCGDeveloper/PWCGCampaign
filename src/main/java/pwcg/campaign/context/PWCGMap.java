package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.campaign.target.preference.TargetPreferenceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.map.IMapClimate;
import pwcg.product.bos.map.IMapSeason;

public abstract class PWCGMap
{
    public abstract ICountry getGroundCountryForMapBySide(Side side);
    public abstract int getRainChances();
    protected abstract void configureTransitionDates() throws PWCGException;
    protected abstract Map<String, Integer> getMissionSpacingMyDate();
    protected abstract IMapClimate buildMapClimate();
    protected abstract IMapSeason buildMapSeason();
    
    public enum FrontMapIdentifier
    {
        MOSCOW_MAP,
        STALINGRAD_MAP,
        KUBAN_MAP,
        EAST1944_MAP,
        EAST1945_MAP,
        BODENPLATTE_MAP,
        ARRAS_MAP
    }

    public static final String MOSCOW_MAP_NAME = "Moscow";
    public static final String STALINGRAD_MAP_NAME = "Stalingrad";
    public static final String EAST1944_MAP_NAME = "East1944";
    public static final String EAST1945_MAP_NAME = "East1945";
    public static final String KUBAN_MAP_NAME = "Kuban";
    public static final String BODENPLATTE_MAP_NAME = "Bodenplatte";

    public static final String ARRAS_MAP_NAME = "Arras";

    protected FrontMapIdentifier mapIdentifier = null;
    protected IMapClimate mapClimate;
    protected IMapSeason mapSeason;
    protected Map<Date, FrontLinesForMap> frontLinesForMapByDate = new TreeMap<>();
    protected FrontDatesForMap frontDatesForMap;
    protected DrifterManager drifterManager = null;
    protected AirfieldManager airfieldManager = null;
    protected GroupManager groupManager = null; 
    protected ShippingLaneManager shippingLaneManager = null;
    protected TargetPreferenceManager targetPreferenceManager = null;
    protected MapArea mapArea = null;
    protected MapArea usableMapArea = null;
    protected String mapName = "";
    protected List<Integer> armedServicesActiveForMap = new ArrayList<>();
    
    public PWCGMap()
    {
    }

    public void configure() throws PWCGException
    {
        String mapName = getMapName();
        
        frontDatesForMap = new FrontDatesForMap(PWCGMap.getFrontMapIdentifierForName(mapName));
        configureTransitionDates();
    	frontDatesForMap.cleanUnwantedDateDirectories(mapName);
    	for (Date frontDate : frontDatesForMap.getFrontDates())
    	{
    		FrontLinesForMap frontLinesForMap = new FrontLinesForMap(mapName);
            frontLinesForMap.configureForDate(frontDate);
            frontLinesForMapByDate.put(frontDate, frontLinesForMap);
    	}
        
        drifterManager = new DrifterManager();
        drifterManager.configure(mapName);
        
        groupManager = new GroupManager();
        groupManager.configure(mapName, airfieldManager);
        
        shippingLaneManager = new ShippingLaneManager();
        shippingLaneManager.configure(mapName);

        airfieldManager = new AirfieldManager();
        
        airfieldManager.configure(mapName);
        
        targetPreferenceManager = new TargetPreferenceManager();
        targetPreferenceManager.configure(mapName);
    }

    public static FrontMapIdentifier getFrontMapIdentifierForName(String name)
    {
        Map<String, FrontMapIdentifier> frontNameIdentifierMap = new HashMap<String, FrontMapIdentifier>();
        
        frontNameIdentifierMap.put(MOSCOW_MAP_NAME, FrontMapIdentifier.MOSCOW_MAP);            
        frontNameIdentifierMap.put(STALINGRAD_MAP_NAME, FrontMapIdentifier.STALINGRAD_MAP);
        frontNameIdentifierMap.put(KUBAN_MAP_NAME, FrontMapIdentifier.KUBAN_MAP);            
        frontNameIdentifierMap.put(EAST1944_MAP_NAME, FrontMapIdentifier.EAST1944_MAP);
        frontNameIdentifierMap.put(EAST1945_MAP_NAME, FrontMapIdentifier.EAST1945_MAP);
        frontNameIdentifierMap.put(BODENPLATTE_MAP_NAME, FrontMapIdentifier.BODENPLATTE_MAP);            

        frontNameIdentifierMap.put(ARRAS_MAP_NAME, FrontMapIdentifier.ARRAS_MAP);            

        return frontNameIdentifierMap.get(name);
    }
    
    public int getDaysBetweenMissionForDate(Date date) throws PWCGException
    {
        int numDaysSpacing = 2;
        
        Map<String, Integer> missionSpacingMyDate = getMissionSpacingMyDate();
        for (String mapDate : missionSpacingMyDate.keySet())
        {
            Date spacingDate = DateUtils.getDateYYYYMMDD(mapDate);
            if (spacingDate.after(date))
            {
                break;
            }
            
            numDaysSpacing = missionSpacingMyDate.get(mapDate);
        }
        return numDaysSpacing;
    }

    public boolean isMapHasService(int serviceId)
    {
        for (int mapServiceId : armedServicesActiveForMap)
        {
            if (mapServiceId == serviceId)
            {
                return true;
            }
        }
        return false;
    }

    public IMapClimate getMapClimate()
    {
        if (mapClimate == null)
        {
            mapClimate = buildMapClimate();
        }
        return mapClimate;
    }

    public IMapSeason getMapSeason() throws PWCGException
    {
        if (mapSeason == null)
        {
            mapSeason = buildMapSeason();
        }
        return mapSeason;
    }

    public FrontMapIdentifier getMapIdentifier()
    {
        return mapIdentifier;
    }

    public void setMapIdentifier(FrontMapIdentifier mapIdentifier)
    {
        this.mapIdentifier = mapIdentifier;
    }

    public FrontLinesForMap getFrontLinesForMap(Date date) throws PWCGException
    {
    	Date frontDate = frontDatesForMap.getFrontDateForDate(date);
        return frontLinesForMapByDate.get(frontDate);
    }

    public AirfieldManager getAirfieldManager()
    {
        return airfieldManager;
    }

    public void setAirfieldManager(AirfieldManager airfieldManager)
    {
        this.airfieldManager = airfieldManager;
    }

    public GroupManager getGroupManager()
    {
        return groupManager;
    }
    
    public DrifterManager getDrifterManager()
    {
        return drifterManager;
    }
    
    public ShippingLaneManager getShippingLaneManager()
    {
        return shippingLaneManager;
    }

    public void setGroupManager(GroupManager groupManager)
    {
        this.groupManager = groupManager;
    }

    public MapArea getMapArea()
    {
        return mapArea;
    }
        
    public MapArea getUsableMapArea()
    {
        return usableMapArea;
    }
        
    public FrontDatesForMap getFrontDatesForMap()
    {
        return frontDatesForMap;
    }

    public String getMapName()
    {
        return mapName;
    }

    public Coordinate getMapCenter()
    {
        return mapArea.getCenter();
    }

    public TargetPreferenceManager getTargetPreferenceManager()
    {
        return targetPreferenceManager;
    }
}
