package pwcg.campaign.context;

import java.awt.Point;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.target.DrifterManager;
import pwcg.campaign.target.ShippingLaneManager;
import pwcg.campaign.target.TargetPreferenceManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.options.MapWeather;
import pwcg.mission.options.MissionOptions;

public abstract class PWCGMap
{
    protected abstract void configureTransitionDates() throws PWCGException;
    
    public enum FrontMapIdentifier
    {
        FRANCE_MAP,
        CHANNEL_MAP,
        GALICIA_MAP,
        MOSCOW_MAP,
        STALINGRAD_MAP,
        KUBAN_MAP
    }

    public static final String FRANCE_MAP_NAME = "France";
    public static final String CHANNEL_MAP_NAME = "Channel";
    public static final String GALICIA_MAP_NAME = "Galicia";

    public static final String MOSCOW_MAP_NAME = "Moscow";
    public static final String STALINGRAD_MAP_NAME = "Stalingrad";
    public static final String KUBAN_MAP_NAME = "Kuban";

    protected FrontMapIdentifier mapIdentifier = null;
    protected Map<Date, FrontLinesForMap> frontLinesForMapByDate = new TreeMap<>();
    protected FrontDatesForMap frontDatesForMap = new FrontDatesForMap();
    protected DrifterManager drifterManager = null;
    protected AirfieldManager airfieldManager = null;
    protected GroupManager groupManager = null; 
    protected ShippingLaneManager shippingLaneManager = null;
    protected TargetPreferenceManager targetPreferenceManager = null;
    protected FrontParameters frontParameters = null;
    protected MissionOptions missionOptions = null;
    protected MapWeather mapWeather = null;
    protected String mapName = "";
    protected Point mapCenter = new Point(700, 700);

    
    public PWCGMap()
    {
    }

    public static FrontMapIdentifier getMapIdFromMapName(String mapName)
    {
        FrontMapIdentifier mapId = FrontMapIdentifier.FRANCE_MAP;
        if (mapName.equals(CHANNEL_MAP_NAME))
        {
            mapId = FrontMapIdentifier.CHANNEL_MAP;
        }
        else if (mapName.equals(GALICIA_MAP_NAME))
        {
            mapId = FrontMapIdentifier.GALICIA_MAP;
        }
        else if (mapName.equals(MOSCOW_MAP_NAME))
        {
            mapId = FrontMapIdentifier.MOSCOW_MAP;
        }
        else if (mapName.equals(STALINGRAD_MAP_NAME))
        {
            mapId = FrontMapIdentifier.STALINGRAD_MAP;
        }
        else if (mapName.equals(KUBAN_MAP_NAME))
        {
            mapId = FrontMapIdentifier.KUBAN_MAP;
        }

        return mapId;
    }

    public void configure() throws PWCGException
    {
        String mapName = getMapName();
        
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
        
        frontNameIdentifierMap.put(FRANCE_MAP_NAME, FrontMapIdentifier.FRANCE_MAP);            
        frontNameIdentifierMap.put(CHANNEL_MAP_NAME, FrontMapIdentifier.CHANNEL_MAP);
        frontNameIdentifierMap.put(GALICIA_MAP_NAME, FrontMapIdentifier.GALICIA_MAP);   
        
        frontNameIdentifierMap.put(MOSCOW_MAP_NAME, FrontMapIdentifier.MOSCOW_MAP);            
        frontNameIdentifierMap.put(STALINGRAD_MAP_NAME, FrontMapIdentifier.STALINGRAD_MAP);
        frontNameIdentifierMap.put(KUBAN_MAP_NAME, FrontMapIdentifier.KUBAN_MAP);            

        return frontNameIdentifierMap.get(name);
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

    public FrontParameters getFrontParameters()
    {
        return frontParameters;
    }
        
    public FrontDatesForMap getFrontDatesForMap()
    {
        return frontDatesForMap;
    }

    public String getMapName()
    {
        return mapName;
    }

    public MissionOptions getMissionOptions()
    {
        return missionOptions;
    }

    public MapWeather getMapWeather()
    {
        return mapWeather;
    }

    public Point getMapCenter()
    {
        return mapCenter;
    }

    public TargetPreferenceManager getTargetPreferenceManager()
    {
        return targetPreferenceManager;
    }
}
