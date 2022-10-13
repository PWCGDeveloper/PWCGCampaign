package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.List;

import pwcg.mission.ground.building.PwcgBuildingIdentifier;

public class GroundStructureGroup
{
	private List<Block> railroadStations = new ArrayList<Block>();
	private List<Block> standaloneBlocks = new ArrayList<Block>();
	private List<Bridge> bridges = new ArrayList<Bridge>();
    private List<Block> airfieldBlocks = new ArrayList<Block>();
    private List<NonScriptedBlock> nonScriptedGround = new ArrayList<NonScriptedBlock>();

	public List<Block> getRailroadStations()
	{
		return railroadStations;
	}

	public void addRailroadStations(List<Block> railroadStations)
	{
		this.railroadStations.addAll(railroadStations);
	}

	public List<Block> getStandaloneBlocks()
	{
		return standaloneBlocks;
	}

	public void addStandaloneBlocks(List<Block> standaloneBlocks)
	{
        this.standaloneBlocks.addAll(standaloneBlocks);
	}

	public List<Bridge> getBridges()
	{
		return bridges;
	}

	public void addBridges(List<Bridge> bridges)
	{
        this.bridges.addAll(bridges);
	}

	public List<Block> getAirfieldBlocks()
	{
		return airfieldBlocks;
	}

	public void addAirfieldBlocks(List<Block> airfieldBlocks)
	{
        this.airfieldBlocks.addAll(airfieldBlocks);
	}

	public List<NonScriptedBlock> getNonScriptedGround()
    {
        return nonScriptedGround;
    }

    public void addNonScriptedGround(List<NonScriptedBlock> nonScriptedGround)
    {
        this.nonScriptedGround.addAll(nonScriptedGround);
    }	

    public void generateAirfieldRelationships()
    {
        List<Block> newStandaloneBlocks = new ArrayList<Block>();
        List<Block> newAirfieldBlocks = new ArrayList<Block>();
        for (Block block : standaloneBlocks)
        {
            if (PwcgBuildingIdentifier.isAirfield(block))
            {
                newAirfieldBlocks.add(block);
            }
            else
            {
                newStandaloneBlocks.add(block);
            }
        }

        standaloneBlocks = newStandaloneBlocks;
        airfieldBlocks = newAirfieldBlocks;
    }

}
