package pwcg.dev.jsonconvert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.AirfieldDescriptor;
import pwcg.campaign.group.airfield.AirfieldDescriptorSet;
import pwcg.campaign.group.airfield.Runway;
import pwcg.campaign.io.json.AirfieldDescriptorIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.dev.jsonconvert.orig.io.DevIOConstants;
import pwcg.dev.jsonconvert.orig.io.PlaneIO;
import pwcg.mission.flight.plane.PlaneMcu;

public class AirfieldBuilderFC 
{    
    TreeMap<String, AirfieldDescriptor> airfields = new TreeMap<>();
    
    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        AirfieldBuilderFC jsonConverter = new AirfieldBuilderFC();
        jsonConverter.getAirfieldNames("WesternFront");
    }

    private void getAirfieldNames (String mapName) throws Exception 
    {
        String filename = "";
        try
        {
            filename = "D:\\PWCG\\WesternFrontData\\WesternFrontAirfieldsMarked1.Mission";     
            readRunwayLocations(filename);
            filename = "D:\\PWCG\\WesternFrontData\\WesternFrontAirfieldsMarked2.Mission";     
            readRunwayLocations(filename);
            filename = "D:\\PWCG\\WesternFrontData\\WesternFrontAirfieldsMarked3.Mission";     
            readRunwayLocations(filename);
            filename = "D:\\PWCG\\WesternFrontData\\WesternFrontAirfieldsMarked4.Mission";     
            readRunwayLocations(filename);
            writeAirfields(mapName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void readRunwayLocations (String fileName) throws PWCGException 
    {        
        try 
        {           
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            int lineNumber = 0;
            AirfieldDescriptor airfield = new AirfieldDescriptor();
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.startsWith(DevIOConstants.PLANE))
                {

                    PlaneMcu planeThatMarksRunway = PlaneIO.readBlock(reader);
                    String markerPlaneName = planeThatMarksRunway.getScript().toLowerCase();
                    if (markerPlaneName.contains("a20"))
                    {                        
                        Runway runway = new Runway();
                        runway.setStartPos(planeThatMarksRunway.getPosition());
                        
                        List<Runway> runways = new ArrayList<>();
                        runways.add(runway);

                        String airfieldName = planeThatMarksRunway.getName();
                        airfieldName = airfieldName.trim();
                        airfieldName = airfieldName.replaceAll("-", " ");
                        airfieldName = airfieldName.replaceAll("_", " ");
                        airfield.setName(airfieldName);
                        
                        
                        airfield.setPosition(planeThatMarksRunway.getPosition());
                        airfield.setPosition(planeThatMarksRunway.getPosition());
                        airfield.setRunways(runways);
                        
                    }
                    else if (markerPlaneName.contains("airco"))
                    {                        
                        airfield.getRunways().get(0).setEndPos(planeThatMarksRunway.getPosition());

                        double angle = MathUtils.calcAngle(airfield.getRunways().get(0).getStartPos(), airfield.getRunways().get(0).getEndPos());
                        angle = Double.valueOf(angle).intValue();
                        
                        double angleOpposite = MathUtils.calcAngle(airfield.getRunways().get(0).getEndPos(), airfield.getRunways().get(0).getStartPos());
                        angleOpposite = Double.valueOf(angleOpposite).intValue();

                        PWCGLocation parkingLocation = new PWCGLocation();
                        parkingLocation.setPosition(airfield.getPosition());
                        parkingLocation.setOrientation(new Orientation(angleOpposite));
                        airfield.getRunways().get(0).setParkingLocation(parkingLocation);
                        
                        airfield.setOrientation(new Orientation(angle));

                        airfields.put(airfield.getName(), airfield);
                        airfield = new AirfieldDescriptor();
                    }
                    else
                    {
                        throw new PWCGException("Unexpected plane marker at " + lineNumber + " " + markerPlaneName);
                    }
                }
                
                
                ++lineNumber;
            }
            
            reader.close();  
            
            
            for (AirfieldDescriptor airfieldToPrint : airfields.values())
            {
                System.out.println("Airfield: " + airfieldToPrint.getName());
                System.out.println("    XPOS: " + airfieldToPrint.getPosition().getXPos());
                System.out.println("    YORI: " + airfieldToPrint.getOrientation().getyOri());
            }

        } 
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    private void writeAirfields(String mapName) throws PWCGException
    {
        AirfieldDescriptorSet airfieldDescriptorSet = new AirfieldDescriptorSet();
        airfieldDescriptorSet.setLocationSetName("Airfields");
        airfieldDescriptorSet.setLocations(new ArrayList<>(airfields.values()));
        String dirname = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
        AirfieldDescriptorIOJson.writeJson(dirname, "AirfieldLocations", airfieldDescriptorSet);
    }
    
    
}
