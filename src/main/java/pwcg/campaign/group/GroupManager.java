package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.AirfieldBlock;
import pwcg.campaign.io.json.GroundObjectIOJson;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;

public class GroupManager 
{
	public static final String TOWN_LOCATION_FILE_NAME = "MapLocations";
	
	private GroundStructureGroup groundStructureGroup = new GroundStructureGroup();
    private TownFinder townFinder;
    private BlockFinder blockFinder;
    private RailroadStationFinder railroadStationFinder;
    private BridgeFinder bridgeFinder;

	public GroupManager ()
	{
	}

	public void configure(String mapName, AirfieldManager airfieldManager) throws PWCGException 
	{
	    groundStructureGroup = GroundObjectIOJson.readJson(mapName);
	    groundStructureGroup.setAltitudeGroundLevel();
	    groundStructureGroup.generateEntityRelationships();
        blockFinder = new BlockFinder(groundStructureGroup.getStandaloneBlocks());
        bridgeFinder = new BridgeFinder(groundStructureGroup.getBridges());
        railroadStationFinder = new RailroadStationFinder(groundStructureGroup.getRailroadStations());

	    String pwcgInputDir = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
	    LocationSet townLocations = LocationIOJson.readJson(pwcgInputDir, TOWN_LOCATION_FILE_NAME);
        townFinder = new TownFinder(townLocations);
	}

    public List<FixedPosition> getAllFixedPosition() 
    {
        List<FixedPosition>fixedPositions = new ArrayList<FixedPosition>();
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

    public LocationSet getTownLocations()
    {
        return townFinder.getTownLocations();
    }

    public List<AirfieldBlock> getAifieldsFromBlocks()
    {
        return groundStructureGroup.getAirfieldBlocks();
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
