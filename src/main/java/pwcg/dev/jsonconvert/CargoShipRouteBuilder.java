package pwcg.dev.jsonconvert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CargoRoutesIOJson;
import pwcg.campaign.shipping.CargoShipRoute;
import pwcg.campaign.shipping.CargoShipRoutes;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.dev.jsonconvert.orig.io.DevIOConstants;
import pwcg.dev.jsonconvert.orig.io.VehicleIO;
import pwcg.dev.jsonconvert.orig.io.WaypointIO;
import pwcg.mission.ground.vehicle.Vehicle;
import pwcg.mission.mcu.McuWaypoint;

public class CargoShipRouteBuilder 
{    
    TreeMap<Integer, CargoShipRoute > cargoRoutes = new TreeMap<>();
    
    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        CargoShipRouteBuilder jsonConverter = new CargoShipRouteBuilder();
        jsonConverter.getCargoRoutes("Normandy");
    }

    private void getCargoRoutes (String mapName) throws Exception 
    {
        String filename = "D:\\PWCG\\NormandyData\\NormandyPWCGData.Mission";     
        readCargoRouteLocations(filename);
        writeCargoRoutes(mapName);
    }

    private void readCargoRouteLocations (String fileName) throws PWCGException 
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
                    String cargoRouteName = vehicle.getVehicleName();
                    if (cargoRouteName.contains("Cargo Route"))
                    {
                        initCargoRoute(cargoRouteName, vehicle);
                    }
                }
                else if (line.startsWith(DevIOConstants.MCU_WAYPOINT))
                {
                    McuWaypoint waypoint = WaypointIO.readBlock(reader);
                    setCargoRouteStopPosition(waypoint);
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

    private CargoShipRoute  initCargoRoute(String routeName, Vehicle vehicle) throws PWCGException
    {
        if (!cargoRoutes.containsKey(vehicle.getLinkTrId()))
        {
            CargoShipRoute  cargoRoute = new CargoShipRoute ();
            cargoRoute.setCountry(Country.BRITAIN);
            cargoRoute.setName(routeName);
            cargoRoute.setRouteStartPosition(vehicle.getPosition());
            
            if (routeName.contains("Dunkirk"))
            {
                cargoRoute.setRouteStartDate(DateUtils.getDateYYYYMMDD("19410601"));
                cargoRoute.setRouteStopDate(DateUtils.getDateYYYYMMDD("19410610"));
            }
            else
            {
                cargoRoute.setRouteStartDate(DateUtils.getDateYYYYMMDD("19410615"));
                cargoRoute.setRouteStopDate(DateUtils.getDateYYYYMMDD("19411001"));
            }
            
            cargoRoutes.put(vehicle.getLinkTrId(), cargoRoute);
        }
        
        CargoShipRoute  cargoRoute = cargoRoutes.get(vehicle.getLinkTrId());
        return cargoRoute;
    }

    private void setCargoRouteStopPosition(McuWaypoint waypoint) throws PWCGException
    {
        int index = Integer.valueOf(waypoint.getObjects().get(0));
        CargoShipRoute  cargoRoute = cargoRoutes.get(index);
        cargoRoute.setRouteDestination(waypoint.getPosition());
    }

    private void writeCargoRoutes(String mapName) throws PWCGException
    {
        CargoShipRoutes cargoRouteOutput = new CargoShipRoutes();
        cargoRouteOutput.setRouteDefinitions(new ArrayList<>(cargoRoutes.values()));
        
        CargoRoutesIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "CargoRoutes.json", cargoRouteOutput);
    }
    
    
}
