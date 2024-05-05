package pwcg.dev.jsonconvert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.AirfieldDescriptor;
import pwcg.campaign.group.airfield.AirfieldDescriptorMapSet;
import pwcg.campaign.group.airfield.Runway;
import pwcg.campaign.io.json.AirfieldDescriptorIOMapJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.dev.jsonconvert.orig.io.DevIOConstants;
import pwcg.dev.jsonconvert.orig.io.PlaneIO;
import pwcg.mission.flight.plane.PlaneMcu;

public class AirfieldBuilder 
{    
    TreeMap<String, AirfieldDescriptor> airfields = new TreeMap<>();
    List<String> largeAirfields = new ArrayList<>();
    
    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        AirfieldBuilder jsonConverter = new AirfieldBuilder();
        jsonConverter.getAirfieldNames("Normandy");
    }

    private void getAirfieldNames (String mapName) throws Exception 
    {
        String filename = "D:\\PWCG\\Normandy\\NormandyPWCGData.Mission";     
        readRunwayLocations(filename);
        writeAirfields(mapName);
    }

    private void readRunwayLocations (String fileName) throws PWCGException 
    {        
        try 
        {           
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.startsWith(DevIOConstants.PLANE))
                {
                    PlaneMcu planeThatMarksRunway = PlaneIO.readBlock(reader);
                    String markerPlaneName = planeThatMarksRunway.getScript().toLowerCase();
                    if (markerPlaneName.contains("bf109") ||
                        markerPlaneName.contains("airco") ||
                        markerPlaneName.contains("a20"))
                    {
                       int runwayLength = 1000;
                        if (markerPlaneName.contains("a20"))
                        {
                            largeAirfields.add(planeThatMarksRunway.getName());
                            runwayLength = 1500;
                        }
                        
                        Coordinate runwayEndCoordinate = MathUtils.calcNextCoord(FrontMapIdentifier.NORMANDY_MAP, planeThatMarksRunway.getPosition(), planeThatMarksRunway.getOrientation().getyOri(), runwayLength);

                        
                        Runway runway = new Runway();
                        runway.setStartPos(planeThatMarksRunway.getPosition());
                        runway.setEndPos(runwayEndCoordinate);
                        
                        List<Runway> runways = new ArrayList<>();
                        runways.add(runway);

                        AirfieldDescriptor airfield = new AirfieldDescriptor();
                        airfield.setName(planeThatMarksRunway.getName());
                        airfield.setPosition(planeThatMarksRunway.getPosition());
                        airfield.setOrientation(planeThatMarksRunway.getOrientation());
                        airfield.setRunways(runways);
                        
                        airfields.put(airfield.getName(), airfield);
                    }
                    else
                    {
                        throw new PWCGException("Unexpected plane marker at " + lineNumber);
                    }
                }
                ++lineNumber;
            }
            
            reader.close();
            
            System.out.println("Large Airfields");
            for (String largeAirfield: largeAirfields)
            {
                System.out.println(largeAirfield);
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
        AirfieldDescriptorMapSet airfieldDescriptorSet = new AirfieldDescriptorMapSet();
        airfieldDescriptorSet.setLocationSetName("Airfields");
        airfieldDescriptorSet.setLocations(new ArrayList<>(airfields.values()));
        AirfieldDescriptorIOMapJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "AirfieldLocations2.json", airfieldDescriptorSet);
    }
    
    
}
