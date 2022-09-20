package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.Parsers;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointIO 
{		
    public static McuWaypoint readBlock (BufferedReader reader) throws PWCGException 
    {
        try
        {
            McuWaypoint waypoint = new McuWaypoint(WaypointType.MOVE_TO_WAYPOINT);
            Coordinate coords = new Coordinate();
            Orientation ori = new Orientation();
            
            boolean stop = false;

            while (!stop)
            {
                String line = reader.readLine();
                if (line == null)
                {
                    throw new PWCGException ("Bad waypoint at readBlock");
                }
                
                line = line.trim();
                
                if (line.contains("}"))
                {
                    stop = true;
                }
                else if (line.contains(DevIOConstants.NAME))
                {
                    waypoint.setName(Parsers.getString(line));
                }
                else if (line.contains(DevIOConstants.OBJECTS))
                {
                    String objects = Parsers.getString(line);
                    int startIndex = objects.indexOf("[");
                    int endIndex = objects.indexOf("]");
                    String objectValueString = "";
                    if (endIndex > 0)
                    {
                        objectValueString = objects.substring(startIndex, endIndex);
                    }
                    else
                    {
                        objectValueString = objects.substring(startIndex+1);
                    }
                    int objectValue = Integer.valueOf(objectValueString);
                    waypoint.setObject(objectValue);
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
            

            waypoint.setPosition(coords);
            waypoint.setOrientation(ori);

            return waypoint;
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
}
