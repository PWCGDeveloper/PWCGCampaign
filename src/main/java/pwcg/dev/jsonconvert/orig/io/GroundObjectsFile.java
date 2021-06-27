package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;

public class GroundObjectsFile 
{
    
    private List<Block> railroadStations = new ArrayList<>();
    private List<Block> standaloneBlocks = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<Bridge>();
    private List<Block> airfieldBlocks = new ArrayList<>();

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
					// Standalone RR stations
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
			}
			
			reader.close();
		} 
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
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
	
	
}
