package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.group.airfield.AirfieldBlock;

public class GroundStructureGroup
{
	private List<Block> railroadStations = new ArrayList<Block>();
	private List<Block> standaloneBlocks = new ArrayList<Block>();
	private List<Bridge> bridges = new ArrayList<Bridge>();
	private List<AirfieldBlock> airfieldBlocks = new ArrayList<AirfieldBlock>();

	public List<Block> getRailroadStations()
	{
		return railroadStations;
	}

	public void setRailroadStations(List<Block> railroadStations)
	{
		this.railroadStations = railroadStations;
	}

	public List<Block> getStandaloneBlocks()
	{
		return standaloneBlocks;
	}

	public void setStandaloneBlocks(List<Block> standaloneBlocks)
	{
		this.standaloneBlocks = standaloneBlocks;
	}

	public List<Bridge> getBridges()
	{
		return bridges;
	}

	public void setBridges(List<Bridge> bridges)
	{
		this.bridges = bridges;
	}

	public List<AirfieldBlock> getAirfieldBlocks()
	{
		return airfieldBlocks;
	}

	public void setAirfieldBlocks(List<AirfieldBlock> airfieldBlocks)
	{
		this.airfieldBlocks = airfieldBlocks;
	}

	public void generateEntityRelationships()
	{
        List<FixedPosition> fixedPositions = new ArrayList<>();
        fixedPositions.addAll(railroadStations);
        fixedPositions.addAll(standaloneBlocks);
        fixedPositions.addAll(bridges);
        fixedPositions.addAll(airfieldBlocks);
	}
}
