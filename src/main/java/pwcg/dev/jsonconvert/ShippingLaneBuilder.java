package pwcg.dev.jsonconvert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.ShippingLaneIOJson;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.shipping.ShippingLanes;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.dev.jsonconvert.orig.io.DevIOConstants;
import pwcg.dev.jsonconvert.orig.io.VehicleIO;
import pwcg.mission.ground.vehicle.Vehicle;

public class ShippingLaneBuilder 
{    
    TreeMap<String, ShippingLane> alliedShippingLanes = new TreeMap<>();
    TreeMap<String, ShippingLane> axisShippingLanes = new TreeMap<>();
    
    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        ShippingLaneBuilder jsonConverter = new ShippingLaneBuilder();
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
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.startsWith(DevIOConstants.SHIP))
                {
                    Vehicle vehicle = VehicleIO.readBlock(reader);
                    String shippingLaneName = vehicle.getVehicleName();
                    if (shippingLaneName.contains("Shipping Lane"))
                    {
                        ShippingLane shippingLane = getShippingLane(shippingLaneName);
                        setShippingLanePosition(lineNumber, vehicle, shippingLane);
                        setShippingLaneCountryData(vehicle, shippingLane);
                    }
                }
                ++lineNumber;
            }
            
            reader.close();
        } 
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    private void setShippingLanePosition(int lineNumber, Vehicle vehicle, ShippingLane shippingLane) throws PWCGException
    {
        if (vehicle.getScript().contains("subtype2b") || vehicle.getScript().contains("lct"))
        {
            shippingLane.setNeCorner(vehicle.getPosition());
        }
        else if (vehicle.getScript().contains("subtypesh10") || vehicle.getScript().contains("lcvp"))
        {
            shippingLane.setSwCorner(vehicle.getPosition());
        }
        else
        {
            throw new PWCGException("Unexpected shipping lane marker at " + lineNumber);
        }
    }

    private ShippingLane getShippingLane(String shippingLaneName)
    {
        TreeMap<String, ShippingLane> shippingLanes = alliedShippingLanes;
        if (shippingLaneName.contains("German"))
        {
            shippingLanes = axisShippingLanes;
        }

        if (!shippingLanes.containsKey(shippingLaneName))
        {
            ShippingLane shippingLane = new ShippingLane();
            shippingLanes.put(shippingLaneName, shippingLane);
        }
        
        ShippingLane shippingLane = shippingLanes.get(shippingLaneName);
        return shippingLane;
    }

    private void setShippingLaneCountryData(Vehicle vehicle, ShippingLane shippingLane) throws PWCGException
    {
        if (vehicle.getScript().contains("sub"))
        {
            shippingLane.setCountry(Country.GERMANY);
            shippingLane.setStartDate(DateUtils.getDateYYYYMMDD("19410615"));
            shippingLane.setEndDate(DateUtils.getDateYYYYMMDD("19440601"));
        }
        else
        {
            shippingLane.setCountry(Country.BRITAIN);
            shippingLane.setStartDate(DateUtils.getDateYYYYMMDD("19410601"));
            shippingLane.setEndDate(DateUtils.getDateYYYYMMDD("19440901"));
        }
    }

    private void writeShippingLanes(String mapName) throws PWCGException
    {
        ShippingLanes shippingLaneOutput = new ShippingLanes();
        shippingLaneOutput.setAlliedShippingLanes(new ArrayList<>(alliedShippingLanes.values()));
        shippingLaneOutput.setAxisShippingLanes(new ArrayList<>(axisShippingLanes.values()));
        
        ShippingLaneIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "SeaLanes.json", shippingLaneOutput);
    }
    
    
}
