package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import pwcg.campaign.group.airfield.AirfieldBlock;
import pwcg.campaign.io.McuIconIO;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.Parsers;
import pwcg.mission.mcu.McuIcon;

public class AirfieldBlockIO
{

	public static List<McuIcon> readAirfieldIcons (String fileLocation) throws PWCGException
    {
       List<McuIcon> icons = McuIconIO.readIcons(fileLocation);
        
        return icons;
    }

	public static AirfieldBlock readAirfield (BufferedReader reader) throws PWCGException
	{
		try
        {
		    AirfieldBlock field = new AirfieldBlock();
            Coordinate coords = new Coordinate();
            Orientation ori = new Orientation();
            
            boolean stop = false;

            while (!stop)
            {
            	String line = reader.readLine();
            	if (line == null)
            	{
            		stop = true;
            	}
            	if (line.contains("}"))
            	{
            		stop = true;
            	}
            	else if (line.contains(DevIOConstants.DAMAGEREP))
            	{
            		field.setDamageReport(Parsers.getInt(line));
            	}
            	
            	else if (line.contains(DevIOConstants.DAMAGETHRESH))
            	{
            		field.setDamageThreshold(Parsers.getInt(line));
            	}
            	
            	else if (line.contains(DevIOConstants.DESC))
            	{
            		field.setDesc(Parsers.getString(line));
            	}
            	
            	else if (line.contains(DevIOConstants.DURABILITY))
            	{
            		field.setDurability(Parsers.getInt(line));
            	}
            	
            	else if (line.contains(DevIOConstants.LINK))
            	{
            		field.setLinkTrId(Parsers.getInt(line));
            	}
            	
            	else if (line.contains(DevIOConstants.MODEL))
            	{
            		field.setModel(Parsers.getString(line));
            	}
            	
            	else if (line.contains(DevIOConstants.NAME))
            	{
            		field.setName(Parsers.getString(line));
            	}
            	else if (line.contains(DevIOConstants.SCRIPT))
            	{
            		field.setScript(Parsers.getString(line));
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
            }
            field.setPosition(coords);
            field.setOrientation(ori);
            
            return field;
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
}
