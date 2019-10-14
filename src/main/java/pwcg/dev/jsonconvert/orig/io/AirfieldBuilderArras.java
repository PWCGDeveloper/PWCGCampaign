package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.BoSAirfieldDescriptorIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.product.bos.airfield.AirfieldDescriptor;
import pwcg.product.bos.airfield.AirfieldDescriptorSet;
import pwcg.product.bos.airfield.Runway;

public class AirfieldBuilderArras 
{    
    TreeMap<String, PWCGLocation> airfieldLocations = new TreeMap<>();
    TreeMap<String, List<Runway>> airfieldRunways = new TreeMap<>();
    TreeMap<String, AirfieldDescriptor> airfields = new TreeMap<>();
    AirfieldDescriptorSet airfieldDescriptorSet = new AirfieldDescriptorSet();
    
    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.FC);

        AirfieldBuilderArras jsonConverter = new AirfieldBuilderArras();
        jsonConverter.getAirfieldNames("Arras");
    }

    private void getAirfieldNames (String mapName) throws Exception 
    {
        String filename = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\ArrasAirfields.Mission";     
        readRunwayLocations(filename);
        buildBoSAirfields();
        writeAirfields(mapName);
    }

    private void buildBoSAirfields() throws Exception
    {
        for (String airfieldName : airfieldLocations.keySet())
        {
            if (!airfieldRunways.containsKey(airfieldName))
            {
                throw new Exception ("Airfield has no runway " + airfieldName);
            }
            List<Runway> runways = airfieldRunways.get(airfieldName);

            AirfieldDescriptor airfield = new AirfieldDescriptor();
            airfield.setName(airfieldName);
            airfield.setPosition(runways.get(0).getStartPos());
            airfield.setOrientation(new Orientation(runways.get(0).getHeading()));
            airfield.setName(airfieldName);
            airfield.setRunways(runways);
            
            airfields.put(airfieldName, airfield);
        }
    }

    private void readRunwayLocations (String fileName) throws PWCGException 
    {
        int airfieldId = 1;
        try 
        {           
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            PWCGLocation runwayStart = null;
            PWCGLocation runwayEnd = null;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.startsWith(DevIOConstants.PLANE))
                {
                    PlaneMCU planeThatMarksRunway = PlaneIO.readBlock(reader);
                    String markerPlaneName = planeThatMarksRunway.getScript().toLowerCase();
                    if (markerPlaneName.contains("he111"))
                    {
                        if (runwayStart != null)
                        {
                            throw new PWCGException("Start misplaced at " + lineNumber);
                        }
                        runwayStart = getPositionFromPlane(planeThatMarksRunway);
                    }
                    else if (markerPlaneName.contains("fw190"))
                    {
                        if (runwayEnd != null)
                        {
                            throw new PWCGException("End misplaced at " + lineNumber);
                        }
                        runwayEnd = getPositionFromPlane(planeThatMarksRunway);
                    }
                    else
                    {
                        throw new PWCGException("Unexpected plane marker at " + lineNumber);
                    }
                }

                if (runwayStart != null && runwayEnd != null)
                {
                    PWCGLocation airfieldLocation = new PWCGLocation();
                    airfieldLocation.setName("ArrasAirfield" + airfieldId);
                    airfieldLocation.setPosition(runwayStart.getPosition());
                    double angle = MathUtils.calcAngle(runwayStart.getPosition(), runwayEnd.getPosition());
                    Orientation orientation = new Orientation(angle);
                    airfieldLocation.setOrientation(orientation);
                    airfieldLocations.put(airfieldLocation.getName(), airfieldLocation);
                    ++airfieldId;

                    findAirfieldForRunway(runwayStart, runwayEnd);
                    runwayStart = null;
                    runwayEnd = null;
                }
                ++lineNumber;
            }
            
            reader.close();
        } 
        catch (Exception e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    private PWCGLocation getPositionFromPlane(PlaneMCU planeThatMarksRunway)
    {
        PWCGLocation runwayStart;
        runwayStart = new PWCGLocation();
        runwayStart.setPosition(planeThatMarksRunway.getPosition().copy());
        runwayStart.setOrientation(planeThatMarksRunway.getOrientation().copy());
        return runwayStart;
    }
    
    private void findAirfieldForRunway(PWCGLocation runwayStart, PWCGLocation runwayEnd)
    {
        double closestDistance = 1000000000.0;
        PWCGLocation closestAirfield = null;
        for (PWCGLocation airfieldLocation : airfieldLocations.values())
        {
            double distance = MathUtils.calcDist(runwayStart.getPosition(), airfieldLocation.getPosition());
            if (distance < closestDistance)
            {
                closestDistance = distance;
                closestAirfield = airfieldLocation;
            }
        }
        
        if (!airfieldRunways.containsKey(closestAirfield.getName()))
        {
            List<Runway> airfieldRunwayList = new ArrayList<>();
            airfieldRunways.put(closestAirfield.getName(), airfieldRunwayList);
        }
        
        List<Runway> airfieldRunwayList = airfieldRunways.get(closestAirfield.getName());
        Runway runway = new Runway();
        runway.setStartPos(runwayStart.getPosition().copy());
        runway.setEndPos(runwayEnd.getPosition().copy());
        runway.setStartPos(runwayStart.getPosition().copy());
        airfieldRunwayList.add(runway);
    }

    private void writeAirfields(String mapName) throws PWCGException
    {
        List<AirfieldDescriptor> locations = new ArrayList<>(airfields.values());
        airfieldDescriptorSet = new AirfieldDescriptorSet();
        airfieldDescriptorSet.setLocationSetName("Airfields");
        airfieldDescriptorSet.setLocations(locations);
        BoSAirfieldDescriptorIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "AirfieldLocations.json", airfieldDescriptorSet);
    }
}
