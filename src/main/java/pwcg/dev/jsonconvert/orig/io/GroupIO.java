package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.IOException;

import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Parsers;

public class GroupIO 
{		
	/**
	 * @param reader
	 * @return
	 * @throws PWCGIOException 
	 * @throws PWCGException 
	 */
	public static Block readBlock (BufferedReader reader) throws PWCGException 
	{
		try
        {
            Block block = new Block();
            Coordinate coords = new Coordinate();
            Orientation ori = new Orientation();
            
            boolean stop = false;

            while (!stop)
            {
            	String line = reader.readLine();
            	if (line == null)
            	{
            		throw new PWCGIOException ("Bad group at readBlock");
            	}
            	
            	line = line.trim();
            	
            	if (line.contains("}"))
            	{
            		stop = true;
            	}
            	else if (line.contains(DevIOConstants.NAME))
            	{
            		block.setName(Parsers.getString(line));
            	}
            	else if (line.contains(DevIOConstants.LINK))
            	{
            		block.setLinkTrId(Parsers.getInt(line));
            	}
            	else if (line.contains(DevIOConstants.XPOS))
            	{
            		coords.setXPos(Parsers.getDouble(line));
            	}
            	else if (line.contains(DevIOConstants.YPOS))
            	{
            		coords.setYPos(Parsers.getDouble(line));
            	}
            	else if (line.contains(DevIOConstants.ZPOS))
            	{
            		coords.setZPos(Parsers.getDouble(line));
            	}
            	else if (line.contains(DevIOConstants.YORI))
            	{
            		ori.setyOri(Parsers.getDouble(line));
            	}
            	else if (line.contains(DevIOConstants.MODEL))
            	{
            		block.setModel(Parsers.getString(line));
            	}			
            	else if (line.contains(DevIOConstants.SCRIPT))
            	{
            		block.setScript(Parsers.getString(line));
            	}
            	else if (line.contains(DevIOConstants.DESC))
            	{
            		block.setDesc(Parsers.getString(line));
            	}
            	else if (line.contains(DevIOConstants.DURABILITY))
            	{
            		block.setDurability(Parsers.getInt(line));
            	}
            	else if (line.contains(DevIOConstants.DAMAGEREP))
            	{
            		block.setDamageReport(Parsers.getInt(line));
            	}
            	else if (line.contains(DevIOConstants.DAMAGETHRESH))
            	{
            		block.setDamageThreshold(Parsers.getInt(line));
            	}
            	else if (line.contains(DevIOConstants.COUNTRY))
            	{
            	}
            	else if (line.contains(DevIOConstants.DAMAGED))
            	{
            		readDamaged (reader, block);
            	}
            }
            block.setPosition(coords);
            block.setOrientation(ori);

            return block;
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	
	public static Block readDamaged (BufferedReader reader, Block block) throws PWCGIOException 
	{
		try
        {
            boolean stop = false;

            while (!stop)
            {
            	String line = reader.readLine().trim();
            	if (line == null)
            	{
            		throw new PWCGIOException ("Bad data at readDamaged");
            	}
            	if (line.contains("}"))
            	{
            		stop = true;
            	}
            }

            return block;
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

	
	public static Bridge readBridge (BufferedReader reader) throws PWCGException 
	{
		try
        {
            Bridge bridge = new Bridge();
            Coordinate coords = new Coordinate();
            Orientation ori = new Orientation();
            
            boolean stop = false;

            while (!stop)
            {
            	String line = reader.readLine().trim();
            	if (line == null)
            	{
            		throw new PWCGIOException ("Bad group at readBridge");
            	}
            	if (line.contains("}"))
            	{
            		stop = true;
            	}
            	else if (line.contains(DevIOConstants.NAME))
            	{
            		bridge.setName(Parsers.getString(line));
            	}
            	else if (line.contains(DevIOConstants.LINK))
            	{
            		bridge.setLinkTrId(Parsers.getInt(line));
            	}
            	else if (line.contains(DevIOConstants.XPOS))
            	{
            		coords.setXPos(Parsers.getDouble(line));
            	}
            	else if (line.contains(DevIOConstants.YPOS))
            	{
            		coords.setYPos(Parsers.getDouble(line));
            	}
            	else if (line.contains(DevIOConstants.ZPOS))
            	{
            		coords.setZPos(Parsers.getDouble(line));
            	}
            	else if (line.contains(DevIOConstants.YORI))
            	{
            		ori.setyOri(Parsers.getDouble(line));
            	}
            	else if (line.contains(DevIOConstants.MODEL))
            	{
            		bridge.setModel(Parsers.getString(line));
            	}			
            	else if (line.contains(DevIOConstants.SCRIPT))
            	{
            		bridge.setScript(Parsers.getString(line));
            	}
            	else if (line.contains(DevIOConstants.DESC))
            	{
            		bridge.setDesc(Parsers.getString(line));
            	}
            	else if (line.contains(DevIOConstants.DURABILITY))
            	{
            		bridge.setDurability(Parsers.getInt(line));
            	}
            	else if (line.contains(DevIOConstants.DAMAGEREP))
            	{
            		bridge.setDamageReport(Parsers.getInt(line));
            	}
            	else if (line.contains(DevIOConstants.DAMAGETHRESH))
            	{
            		bridge.setDamageThreshold(Parsers.getInt(line));
            	}
            	else if (line.contains(DevIOConstants.DELAFTERDEATH))
            	{
            		bridge.setDeleteAfterDeath(Parsers.getInt(line));
            	}
            	else if (line.contains(DevIOConstants.COUNTRY))
            	{
            	}
            }
            bridge.setPosition(coords);
            bridge.setOrientation(ori);

            return bridge;
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
}
