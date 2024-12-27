package pwcg.dev.jsonconvert;

import java.io.BufferedReader;
import java.io.FileReader;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;
import pwcg.dev.jsonconvert.orig.io.DevIOConstants;
import pwcg.dev.jsonconvert.orig.io.VehicleIO;
import pwcg.mission.ground.vehicle.Vehicle;

public class BargeBuilder 
{    
    LocationSet bargeLocations = new LocationSet("Barges");

    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        BargeBuilder jsonConverter = new BargeBuilder();
        jsonConverter.getShippingLanes("Normandy");
    }

    private void getShippingLanes (String mapName) throws Exception 
    {
        String filename = "C:\\PWCG\\NormandyData\\NormandyPWCGData.Mission";     
        readShippingLaneLocations(filename);
        writeShippingLanes(mapName);
    }

    private void readShippingLaneLocations (String fileName) throws PWCGException 
    {        
        try 
        {           
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.startsWith(DevIOConstants.SHIP))
                {
                    Vehicle vehicle = VehicleIO.readBlock(reader);
                    String shippingLaneName = vehicle.getVehicleName();
                    if (shippingLaneName.contains("Barge"))
                    {
                        PWCGLocation location = new PWCGLocation();
                        location.setName("Barge");
                        location.setPosition(vehicle.getPosition());
                        location.setOrientation(vehicle.getOrientation());
                        bargeLocations.addLocation(location);
                    }
                }
            }
            
            reader.close();
        } 
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    private void writeShippingLanes(String mapName) throws PWCGException
    {
        LocationIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "BargePositions.json", bargeLocations);
    }
    
    
}
