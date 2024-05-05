package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.airfield.AirfieldDescriptor;
import pwcg.campaign.group.airfield.AirfieldDescriptorMapSet;
import pwcg.campaign.group.airfield.Runway;
import pwcg.campaign.io.json.AirfieldDescriptorIOMapJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.plane.PlaneMcu;

public class AirfieldBuilderWesternFront 
{    
    TreeMap<String, PWCGLocation> airfieldLocations = new TreeMap<>();
    List<Runway> airfieldRunways = new ArrayList<>();
    TreeMap<String, AirfieldDescriptor> airfields = new TreeMap<>();
    
    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.FC);

        AirfieldBuilderWesternFront jsonConverter = new AirfieldBuilderWesternFront();
        jsonConverter.getAirfieldNames("WesternFront");
    }

    public void getAirfieldNames (String mapName) throws Exception 
    {
        String filename = "D:\\PWCG\\WesternFrontData\\WesternFroneMapFC3Airfields.Mission";     
        readAirfields(filename);
        readRunwayLocations(filename);        
        buildBoSAirfields();
        writeAirfields(mapName);
    }
    

    private void readAirfields (String filename) throws PWCGException 
    {
        try 
        {           
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.startsWith(DevIOConstants.GROUP))
                {
                    String groupName = GroupIO.readGroupName(reader);
                    
                    // Skip  outer group
                    if (groupName.equalsIgnoreCase("Airfields"))
                    {
                        continue;
                    }
                    
                    Block block = GroupIO.readBlock(reader);
                    PWCGLocation airfieldBlockLocation = new PWCGLocation();
                    airfieldBlockLocation.setName(groupName);
                    airfieldBlockLocation.setPosition(block.getPosition());
                    airfieldLocations.put(groupName, airfieldBlockLocation);
                    
                    System.out.println("Add airfield " + groupName);
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

    private void readRunwayLocations (String fileName) throws PWCGException 
    {
        try 
        {           
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                PlaneMcu planeThatMarksRunway = null;
                if (line.startsWith(DevIOConstants.PLANE))
                {
                    planeThatMarksRunway = PlaneIO.readBlock(reader);
                }

                if (planeThatMarksRunway != null)
                {
                    Coordinate runwayStart = planeThatMarksRunway.getPosition();
                    Double runwayOrientation = planeThatMarksRunway.getOrientation().getyOri();
                    int runwayOrientationInt = (int) Math.round(runwayOrientation);
                    runwayOrientation = Double.valueOf(runwayOrientationInt);
                    
                    Coordinate endPosition = MathUtils.calcNextCoord(
                            FrontMapIdentifier.WESTERN_FRONT_MAP, runwayStart, runwayOrientation, 200.0);

                    Runway runway = new Runway();
                    runway.setStartPos(runwayStart);
                    runway.setEndPos(endPosition);
                    runway.setTaxiToStart(new ArrayList<>());
                    runway.setTaxiFromEnd(new ArrayList<>());
                    
                    double parkingPositionAngle = MathUtils.adjustAngle(runwayOrientation, 270);
                    Coordinate parkingPosition = MathUtils.calcNextCoord(
                            FrontMapIdentifier.WESTERN_FRONT_MAP, runwayStart, parkingPositionAngle, 40.0);
                    PWCGLocation runwayParking = new PWCGLocation();
                    runwayParking.setName("Parking");
                    runwayParking.setOrientation(new Orientation(parkingPositionAngle));
                    runwayParking.setPosition(parkingPosition);

                    runway.setParkingLocation(runwayParking);
                    
                    airfieldRunways.add(runway);

                    System.out.println("Add runway at " + runway.getStartPos().toString());
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

    private void buildBoSAirfields() throws Exception
    {
        for (String airfieldName : airfieldLocations.keySet())
        {
            Runway runway = findRunwayForAirfield(airfieldName);
            if (runway == null)
            {
                throw new Exception ("Airfield has no runway " + airfieldName);
            }
            
            List<Runway> runways = new ArrayList<>();
            runways.add(runway);

            AirfieldDescriptor airfield = new AirfieldDescriptor();
            airfield.setName(airfieldName);
            airfield.setPosition(runways.get(0).getStartPos());
            
            Double runwayOrientation = runways.get(0).getHeading();
            int runwayOrientationInt = (int) Math.round(runwayOrientation);
            runwayOrientation = Double.valueOf(runwayOrientationInt);

            airfield.setOrientation(new Orientation(runwayOrientation));
            airfield.setRunways(runways);
            
            airfields.put(airfieldName, airfield);
        }
    }
    
    private Runway findRunwayForAirfield(String airfieldName) throws PWCGException
    {
        PWCGLocation airfieldLocation = airfieldLocations.get(airfieldName);
        
        double closestDistance = 1000000000.0;
        Runway closestRunway = null;
        for (Runway runway : airfieldRunways)
        {
            double distance = MathUtils.calcDist(airfieldLocation.getPosition(), runway.getStartPos());
            if (distance < closestDistance)
            {
                closestDistance = distance;
                closestRunway = runway;
            }
        }
        
        if (closestDistance > 5000.0)
        {
            throw new PWCGException("No runway found for airfield " + airfieldName);
        }

        System.out.println("Found runway for " + airfieldName + " at distance " + closestDistance);
        
        return closestRunway;
    }

    private void writeAirfields(String mapName) throws PWCGException
    {
        AirfieldDescriptorMapSet airfieldDescriptorSet = new AirfieldDescriptorMapSet();
        List<AirfieldDescriptor> locations = new ArrayList<>(airfields.values());
        airfieldDescriptorSet = new AirfieldDescriptorMapSet();
        airfieldDescriptorSet.setLocationSetName("Airfields");
        airfieldDescriptorSet.setLocations(locations);
        AirfieldDescriptorIOMapJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "AirfieldLocations.json", airfieldDescriptorSet);
    }
}
