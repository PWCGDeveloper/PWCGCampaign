package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.Parsers;
import pwcg.mission.ground.vehicle.Vehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class VehicleIO 
{		

    public static Vehicle readBlock (BufferedReader reader) throws PWCGException 
    {
        try
        {
            VehicleDefinition vehicleDefinition = new VehicleDefinition();
            Coordinate coords = new Coordinate();
            Orientation ori = new Orientation();
            String script = "";
            String model = "";
            int index = 0;
            int link = 0;
            
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
                    vehicleDefinition.setVehicleName(Parsers.getString(line));
                }
                else if (line.contains(DevIOConstants.MODEL))
                {
                    model = Parsers.getString(line);
                }           
                else if (line.contains(DevIOConstants.SCRIPT))
                {
                    script = Parsers.getString(line);
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
                else if (line.contains(DevIOConstants.INDEX))
                {
                    index = Parsers.getInt(line);
                }
                else if (line.contains(DevIOConstants.LINK))
                {
                    link = Parsers.getInt(line);
                }
            }
            
            Vehicle vehicle = new Vehicle(vehicleDefinition);
            vehicle.setVehicleName(vehicleDefinition.getVehicleName());
            vehicle.setModel(model);
            vehicle.setScript(script);
            vehicle.setIndex(index);
            vehicle.setLinkTrId(link);

            vehicle.setPosition(coords);
            vehicle.setOrientation(ori);

            return vehicle;
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
}
