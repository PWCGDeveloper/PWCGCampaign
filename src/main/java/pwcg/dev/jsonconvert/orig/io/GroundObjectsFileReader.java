package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.NonScriptedBlock;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;

public class GroundObjectsFileReader 
{
    
    private List<Block> railroadStations = new ArrayList<>();
    private List<Block> standaloneBlocks = new ArrayList<>();
    private List<Block> airfieldBlocks = new ArrayList<>();
    private List<NonScriptedBlock> groundObjects = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<Bridge>();

    public void readGroundObjectsFromFile (String fileName) throws PWCGException 
	{
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) 
			{
				line = line.trim();
				
				// Standalone blocks
				if (line.startsWith(DevIOConstants.BLOCK))
				{
                    Block block = GroupIO.readBlock(reader);
                    if (PwcgBuildingIdentifier.isAirfield(block))
                    {
                        airfieldBlocks.add(block);
                    }

					if (block.getModel().contains("rwstation") || block.getModel().contains("rail"))
					{
                        railroadStations.add(block);
					}
					else
					{
	                    standaloneBlocks.add(block);
					}
				}
                else if (line.startsWith(DevIOConstants.BRIDGE))
                {
                    Bridge bridge = GroupIO.readBridge(reader);
                    bridges.add(bridge);
                }
                else if (line.startsWith(DevIOConstants.GROUND))
                {
                    NonScriptedBlock ground = GroupIO.readGround(reader);
                    groundObjects.add(ground);
                }
			}
			
			reader.close();
		} 
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}

    public void filter(String filter)
    {
        List<Block> filteredRRBlocks = filterBlockSet(railroadStations, filter, false);
        railroadStations = filteredRRBlocks;
        
        List<Block> filteredStandaloneBlocks = filterBlockSet(standaloneBlocks, filter, false);
        standaloneBlocks = filteredStandaloneBlocks;
        
        List<Block> filteredAirfieldBlocks = filterBlockSet(airfieldBlocks, filter, false);
        airfieldBlocks = filteredAirfieldBlocks;
        
        List<NonScriptedBlock> filteredGroundObjects = filterBlockSet(groundObjects, filter, false);
        groundObjects = filteredGroundObjects;
        
        List<Bridge> filteredBridges = filterBlockSet(bridges, filter, false);
        bridges = filteredBridges;
    }
    

    public void remove(String filter)
    {
        List<Block> filteredRRBlocks = filterBlockSet(railroadStations, filter, true);
        railroadStations = filteredRRBlocks;
        
        List<Block> filteredStandaloneBlocks = filterBlockSet(standaloneBlocks, filter, true);
        standaloneBlocks = filteredStandaloneBlocks;
        
        List<Block> filteredAirfieldBlocks = filterBlockSet(airfieldBlocks, filter, true);
        airfieldBlocks = filteredAirfieldBlocks;
        
        List<NonScriptedBlock> filteredGroundObjects = filterBlockSet(groundObjects, filter, true);
        groundObjects = filteredGroundObjects;
        
        List<Bridge> filteredBridges = filterBlockSet(bridges, filter, true);
        bridges = filteredBridges;
    }

    public static <T extends FixedPosition> List<T> filterBlockSet(List<T> blocks, String filter, boolean negative)
    {
        List<T> filteredBlocks = new ArrayList<T>(); 
        for (T block : blocks)
        {
            if (negative)
            {
                if (!block.getModel().contains(filter))
                {
                    filteredBlocks.add(block);
                }
            }
            else
            {
                if (block.getModel().contains(filter))
                {
                    filteredBlocks.add(block);
                }
            }
        }
        return filteredBlocks;
    }

    public List<Block> getRailroadStations()
    {
        return railroadStations;
    }

    public List<Block> getStandaloneBlocks()
    {
        return standaloneBlocks;
    }

    public List<Bridge> getBridges()
    {
        return bridges;
    }

    public List<Block> getAirfieldBlocks()
    {
        return airfieldBlocks;
    }

    public List<NonScriptedBlock> getGroundObjects()
    {
        return groundObjects;
    }
	
	
}
