package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.GroundObjectIOJson;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.DateUtils;

public class GroupManager 
{
    public static final String TOWN_LOCATION_FILE_NAME = "MapLocations";
    public static final String GROUND_STRUCTURE_FILE_NAME = "GroundStructures.json";
    public static final String GROUND_STRUCTURE_NORMANDY_FILE_NAME = "GroundStructures.NormandyBeach.json";
    public static final String GROUND_STRUCTURE_MULBERRY_FILE_NAME = "GroundStructures.Mulberry.json";
	
	private GroundStructureGroup groundStructureGroup = new GroundStructureGroup();
    private TownFinder townFinder;
    private BlockFinder blockFinder;
    private RailroadStationFinder railroadStationFinder;
    private BridgeFinder bridgeFinder;
    private List<String> groundFilesLoaded = new ArrayList<>();

	public GroupManager ()
	{
	}

	public void configure(String mapName) throws PWCGException 
	{
	    groundStructureGroup = GroundObjectIOJson.readJson(mapName, GROUND_STRUCTURE_FILE_NAME);
        buildFinders();
        groundFilesLoaded.clear();
        groundFilesLoaded.add(GROUND_STRUCTURE_FILE_NAME);

	    String pwcgInputDir = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
	    LocationSet townLocations = LocationIOJson.readJson(pwcgInputDir, TOWN_LOCATION_FILE_NAME);
        townFinder = new TownFinder(townLocations);
	}

    public void configureForDate(String mapName, Campaign campaign) throws PWCGException 
    {
        if (mapName.equals(FrontMapIdentifier.NORMANDY_MAP.getMapName()))
        {
            if (DateUtils.isDateOnOrAfter(campaign.getDate(), DateUtils.getDateYYYYMMDD("19440501")))
            {
                loadAdditionalGroundStructures(mapName, campaign, GROUND_STRUCTURE_NORMANDY_FILE_NAME);
                buildFinders();
            }
            
            if (DateUtils.isDateOnOrAfter(campaign.getDate(), DateUtils.getDateYYYYMMDD("19440615")))
            {
                loadAdditionalGroundStructures(mapName, campaign, GROUND_STRUCTURE_MULBERRY_FILE_NAME);
                buildFinders();
            }            
        }
    }

    private void loadAdditionalGroundStructures(String mapName, Campaign campaign, String filename) throws PWCGException
    {
        if (!groundFilesLoaded.contains(filename))
        {
            GroundStructureGroup additionalStructureGroup = GroundObjectIOJson.readJson(mapName, filename);
            mergeGroundStructureGroup(additionalStructureGroup);
            groundFilesLoaded.add(filename);
        }
    }

    private void mergeGroundStructureGroup(GroundStructureGroup additionalStructureGroup)
    {
        groundStructureGroup.addAirfieldBlocks(additionalStructureGroup.getAirfieldBlocks());        
        groundStructureGroup.addBridges(additionalStructureGroup.getBridges());        
        groundStructureGroup.addRailroadStations(additionalStructureGroup.getRailroadStations());        
        groundStructureGroup.addStandaloneBlocks(additionalStructureGroup.getStandaloneBlocks());        
        groundStructureGroup.addNonScriptedGround(additionalStructureGroup.getNonScriptedGround());        
    }

    private void buildFinders()
    {
        blockFinder = new BlockFinder(groundStructureGroup.getStandaloneBlocks());
        bridgeFinder = new BridgeFinder(groundStructureGroup.getBridges());
        railroadStationFinder = new RailroadStationFinder(groundStructureGroup.getRailroadStations());
    }
	
    public List<ScriptedFixedPosition> getAllFixedPosition() 
    {
        List<ScriptedFixedPosition>fixedPositions = new ArrayList<ScriptedFixedPosition>();
        fixedPositions.addAll(groundStructureGroup.getRailroadStations());
        fixedPositions.addAll(groundStructureGroup.getBridges());
        return fixedPositions;
    }

	public List<Block> getRailroadList() 
	{
		return groundStructureGroup.getRailroadStations();
	}

    public List<Block> getStandaloneBlocks() 
    {
        return groundStructureGroup.getStandaloneBlocks();
    }

    public List<Block> getAirfieldBlocks() 
    {
        return groundStructureGroup.getAirfieldBlocks();
    }

    public List<NonScriptedBlock> getNonScriptedBlocks() 
    {
        return groundStructureGroup.getNonScriptedGround();
    }

    public LocationSet getTownLocations()
    {
        return townFinder.getTownLocations();
    }

    public List<PWCGLocation> findTownsForSideWithinRadius(Side side, Date date, Coordinate referenceLocation, double radius) throws PWCGException
    {
        return townFinder.findTownsForSideWithinRadius(side, date, referenceLocation, radius);
    }

    public TownFinder getTownFinder()
    {
        return townFinder;
    }

    public BlockFinder getBlockFinder()
    {
        return blockFinder;
    }

    public RailroadStationFinder getRailroadStationFinder()
    {
        return railroadStationFinder;
    }

    public BridgeFinder getBridgeFinder()
    {
        return bridgeFinder;
    }
}
