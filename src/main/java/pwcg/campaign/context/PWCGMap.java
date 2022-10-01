package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.battle.AmphibiousAssaultManager;
import pwcg.campaign.battle.BattleManager;
import pwcg.campaign.battle.NoBattlePeriod;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.io.transport.MapTransport;
import pwcg.campaign.io.transport.TransportReader;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.campaign.skirmish.SkirmishManager;
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
    protected List<Integer> armedServicesActiveForMap = new ArrayList<>();
    protected BattleManager battleManager;
    protected AmphibiousAssaultManager amphibiousAssaultManager;
    protected SkirmishManager skirmishManager;
    protected List<NoBattlePeriod> noBattlePeriods = new ArrayList<>();
    protected MapTransport mapTransportRoads = new MapTransport();
    protected MapTransport mapTransportRail = new MapTransport();
    protected boolean hasShips = false;
    protected List<GroundLimitationPeriod> groundLimitations = new ArrayList<>();

    public PWCGMap()
    {
    }

    public void configure() throws PWCGException
    {
        String mapName = getMapName();
        mapIdentifier = FrontMapIdentifier.getFrontMapIdentifierForName(mapName);

        battleManager = new BattleManager(mapIdentifier);
        battleManager.initialize();

        amphibiousAssaultManager = new AmphibiousAssaultManager(mapIdentifier);
        amphibiousAssaultManager.initialize();

        skirmishManager = new SkirmishManager(mapIdentifier);
        skirmishManager.initialize();

        frontDatesForMap = new FrontDatesForMap(mapIdentifier);
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

        airfieldManager.configure(mapIdentifier);

        targetPreferenceManager = new TargetPreferenceManager();
        targetPreferenceManager.configure(mapName);

        TransportReader transportReader = new TransportReader();
        mapTransportRail = transportReader.readTransportFile(mapName, "railroads.ini");
        mapTransportRoads = transportReader.readTransportFile(mapName, "roads.ini");
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

    public boolean isNoDynamicBattlePeriod(Date date) throws PWCGException
    {
        for (NoBattlePeriod noBattlePeriod : noBattlePeriods)
        {
            if (DateUtils.isDateInRange(date, noBattlePeriod.getStartNoBattlePeriod(), noBattlePeriod.getEndNoBattlePeriod()))
            {
                return true;
            }
        }
        return false;
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
    
    public boolean isLimited(Date date, PwcgMapGroundUnitLimitation limitation) throws PWCGException
    {
        for (GroundLimitationPeriod limitationPeriod : groundLimitations)
        {
            if (limitationPeriod.isLimited(date, limitation))
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
        return mapIdentifier.getMapName();
    }

    public Coordinate getMapCenter()
    {
        return mapArea.getCenter();
    }

    public TargetPreferenceManager getTargetPreferenceManager()
    {
        return targetPreferenceManager;
    }

    public BattleManager getBattleManager()
    {
        return battleManager;
    }

    public AmphibiousAssaultManager getAmphibiousAssaultManager()
    {
        return amphibiousAssaultManager;
    }

    public SkirmishManager getSkirmishManager()
    {
        return skirmishManager;
    }

    public MapTransport getMapTransportRoads()
    {
        return mapTransportRoads;
    }

    public MapTransport getMapTransportRail()
    {
        return mapTransportRail;
    }

    public boolean hasShips()
    {
        return hasShips;
    }
    
    
}
