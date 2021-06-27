package pwcg.campaign.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationSet;
import pwcg.core.location.McuIconToLocationConverter;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.Parsers;
import pwcg.mission.mcu.McuIcon;

public class McuIconIO
{
    public static LocationSet convertIconsToLocations (String locationSetName, List<McuIcon> icons) throws PWCGException
    {
        List<PWCGLocation> locations = new ArrayList<>();
        for (McuIcon icon : icons)
        {
        	PWCGLocation location = McuIconToLocationConverter.iconToLocation(icon);
        	locations.add(location);
        }
        
        LocationSet locationSet = new LocationSet(locationSetName);
        locationSet.setLocations(locations);

        return locationSet;
    }
    
    public static List<McuIcon> readIcons (String fileLocation) throws PWCGException
    {
       List<McuIcon> icons = new ArrayList<McuIcon>();

        try 
        {
            BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                if (line.contains(IOConstants.MCU_ICON))
                {
                    McuIcon icon = McuIconIO.readIcon(reader);
                    icons.add(icon);
                }
            }
        } 
        catch (IOException e) 
        {
            PWCGLogger.logException(e);
        }
        
        return icons;
    }

    private static McuIcon readIcon(BufferedReader reader) throws PWCGException 
    {
        McuIcon icon = null;
        
        try
        {
            Coordinate coord = new Coordinate();
            Orientation orient = new Orientation();

            coord.setYPos(0.0);
            int index = -1;
            int lcName = 0;

            double angle = 90.0;

            String name = "";

            while (!reader.readLine().contains("{"));

            boolean stop = false;
            while (!stop)
            {
                String line = reader.readLine();
                if (line == null)
                {
                    throw new PWCGException ("Bad frontlines file");
                }

                line = line.trim();

                if (line.contains("}"))
                {
                    stop = true;
                }
                else if (line.contains(IOConstants.XPOS))
                {
                    coord.setXPos(Parsers.getDouble(line));
                }
                else if (line.contains(IOConstants.LCNAME))
                {
                    lcName = Parsers.getInt(line);
                }
                else if (line.contains(IOConstants.ZPOS))
                {
                    coord.setZPos(Parsers.getDouble(line));
                }
                else if (line.contains(IOConstants.YORI))
                {
                    angle = Parsers.getDouble(line);
                    orient.setyOri(angle);
                }
                else if (line.contains(IOConstants.NAME) && !line.contains(IOConstants.LCNAME))
                {
                    name =  Parsers.getString(line);
                }
            }

            icon = new McuIcon(name, name);

            icon.setIndex(index);
            icon.setPosition(coord);
            icon.setOrientation(orient);
            icon.setName(name);
            icon.setlCName(lcName);
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        
        return icon;
    }

}
