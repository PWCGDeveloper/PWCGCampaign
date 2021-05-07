package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.GroundObjectIOJson;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;

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
