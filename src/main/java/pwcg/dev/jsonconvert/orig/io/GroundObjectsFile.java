package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.airfield.AirfieldBlock;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

public class GroundObjectsFile 
{
    
    private List<Block> railroadStations = new ArrayList<Block>();
    private List<Block> standaloneBlocks = new ArrayList<Block>();
    private List<Bridge> bridges = new ArrayList<Bridge>();
    private List<AirfieldBlock> airfieldBlocks = new ArrayList<AirfieldBlock>();
    
    public void readGroundObjects (String mapName) throws PWCGException 
    {
        String filename = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\GroundObjects.config";     
        readGroundObjectsFromFile(filename);

    }

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
                else if (line.startsWith(DevIOConstants.AIRFIELD))
                {
                    AirfieldBlock airfieldBlock = AirfieldBlockIO.readAirfield(reader);
                    airfieldBlocks.add(airfieldBlock);
                }
			}
			
			reader.close();
		} 
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

    /**
     * @return the railroadStations
     */
    public List<Block> getRailroadStations()
    {
        return railroadStations;
    }

    /**
     * @return the standaloneBlocks
     */
    public List<Block> getStandaloneBlocks()
    {
        return standaloneBlocks;
    }

    /**
     * @return the bridges
     */
    public List<Bridge> getBridges()
    {
        return bridges;
    }

    /**
     * @return the airfieldBlocks
     */
    public List<AirfieldBlock> getAirfieldBlocks()
    {
        return airfieldBlocks;
    }
	
	
}
