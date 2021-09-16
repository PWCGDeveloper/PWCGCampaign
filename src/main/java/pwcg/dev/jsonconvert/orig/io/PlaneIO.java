package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.Parsers;
import pwcg.mission.flight.plane.PlaneMcu;

public class PlaneIO 
{		
	public static PlaneMcu readBlock (BufferedReader reader) throws PWCGException 
	{
		try
        {
		    PlaneMcu plane = new PlaneMcu();
            Coordinate coords = new Coordinate();
            Orientation ori = new Orientation();
            
            boolean stop = false;

            while (!stop)
            {
            	String line = reader.readLine();
            	if (line == null)
            	{
            		throw new PWCGException ("Bad group at readBlock");
            	}
            	
            	line = line.trim();
            	
            	if (line.contains("}"))
            	{
            		stop = true;
            	}
            	else if (line.contains(DevIOConstants.NAME))
            	{
            		plane.setName(Parsers.getString(line));
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
            		plane.setModel(Parsers.getString(line));
            	}			
            	else if (line.contains(DevIOConstants.SCRIPT))
            	{
            		plane.setScript(Parsers.getString(line));
            	}
            }
            plane.setPosition(coords);
            plane.setOrientation(ori);

            return plane;
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}
}
