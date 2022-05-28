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

	public List<Block> getAirfieldBlocks()
	{
		return airfieldBlocks;
	}

	public void setAirfieldBlocks(List<Block> airfieldBlocks)
	{
		this.airfieldBlocks = airfieldBlocks;
	}

	public List<NonScriptedBlock> getNonScriptedGround()
    {
        return nonScriptedGround;
    }

    public void setNonScriptedGround(List<NonScriptedBlock> nonScriptedGround)
    {
        this.nonScriptedGround = nonScriptedGround;
    }

    public void generateEntityRelationships()
	{
        List<ScriptedFixedPosition> fixedPositions = new ArrayList<>();
        fixedPositions.addAll(railroadStations);
        fixedPositions.addAll(bridges);
        fixedPositions.addAll(airfieldBlocks);
        fixedPositions.addAll(standaloneBlocks);
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
