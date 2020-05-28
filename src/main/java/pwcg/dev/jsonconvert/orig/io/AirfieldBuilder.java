package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.airfield.AirfieldDescriptor;
import pwcg.campaign.group.airfield.AirfieldDescriptorSet;
import pwcg.campaign.group.airfield.Runway;
import pwcg.campaign.io.json.AirfieldDescriptorIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.Parsers;
import pwcg.mission.flight.plane.PlaneMcu;

public class AirfieldBuilder 
{    
    TreeMap<String, PWCGLocation> airfieldLocations = new TreeMap<>();
    TreeMap<String, List<Runway>> airfieldRunways = new TreeMap<>();
    TreeMap<String, AirfieldDescriptor> airfields = new TreeMap<>();
    AirfieldDescriptorSet airfieldDescriptorSet = new AirfieldDescriptorSet();
    
    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        AirfieldBuilder jsonConverter = new AirfieldBuilder();
        jsonConverter.getAirfieldNames("Bodenplatte");
    }

    private void getAirfieldNames (String mapName) throws Exception 
    {
        String filename = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\BodenplatteAirfields2.Mission";     
        readGroundObjectsFromFile(filename);
        readRunwayLocations(filename);
        buildBoSAirfields();
        writeAirfields(mapName);
    }

    private void buildBoSAirfields() throws Exception
    {
        for (String airfieldLocationName : airfieldLocations.keySet())
        {
            if (!airfieldRunways.containsKey(airfieldLocationName))
            {
                throw new Exception ("Airfield has no runway " + airfieldLocationName);
            }
            List<Runway> runways = airfieldRunways.get(airfieldLocationName);
            String airfieldName = stripAirfieldLocationName(airfieldLocationName);

            AirfieldDescriptor airfield = new AirfieldDescriptor();
            airfield.setName(airfieldLocationName);
            airfield.setPosition(runways.get(0).getStartPos());
            airfield.setOrientation(new Orientation(runways.get(0).getHeading()));
            airfield.setName(airfieldName);
            airfield.setRunways(runways);
            
            airfields.put(airfieldName, airfield);
        }
    }

    private String stripAirfieldLocationName(String airfieldLocationName)
    {
        String airfieldName = airfieldLocationName;
        airfieldName = airfieldName.substring(11);
        return airfieldName.trim();
    }

    private void readGroundObjectsFromFile (String fileName) throws PWCGException 
	{
		try 
		{		    
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
            boolean airfieldFound = false;
            boolean groupFound = false;
            String airfieldName = "";
			while ((line = reader.readLine()) != null) 
			{
				line = line.trim();
				
                if (line.startsWith(DevIOConstants.GROUP))
                {
                    groupFound = true;
                }
                
                if (groupFound)
                {
                    if (line.startsWith(DevIOConstants.NAME))
                    {
                        airfieldName = Parsers.getString(line);
                        if (!(airfieldName.equals("Airfields")))
                        {
                            airfieldFound = true;
                        }
                    }
    			}
			
                if (airfieldFound)
                {
    				if (line.startsWith(DevIOConstants.BLOCK))
    				{
                        Block block = GroupIO.readBlock(reader);
                        if (groupFound)
                        {
                            groupFound = false;
                            
                            PWCGLocation location = new PWCGLocation();
                            location.setName(airfieldName);
                            location.setPosition(block.getPosition().copy());
                            location.setOrientation(block.getOrientation().copy());
                            airfieldLocations.put(airfieldName, location);
                        }
    				}
                }
			}
			
			reader.close();
		} 
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

    private void readRunwayLocations (String fileName) throws PWCGException 
    {        
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
                    PlaneMcu planeThatMarksRunway = PlaneIO.readBlock(reader);
                    String markerPlaneName = planeThatMarksRunway.getScript().toLowerCase();
                    if (markerPlaneName.contains("he111") ||
                        markerPlaneName.contains("ju88") ||
                        markerPlaneName.contains("pe2"))
                    {
                        if (runwayStart != null)
                        {
                            throw new PWCGException("Start misplaced at " + lineNumber);
                        }
                        runwayStart = getPositionFromPlane(planeThatMarksRunway);
                    }
                    else if (markerPlaneName.contains("fw190") ||
                            markerPlaneName.contains("ju87") ||
                            markerPlaneName.contains("u2"))
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
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    private PWCGLocation getPositionFromPlane(PlaneMcu planeThatMarksRunway)
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
        airfieldRunwayList.add(runway);
    }

    private void writeAirfields(String mapName) throws PWCGException
    {
        List<AirfieldDescriptor> locations = new ArrayList<>(airfields.values());
        airfieldDescriptorSet = new AirfieldDescriptorSet();
        airfieldDescriptorSet.setLocationSetName("Airfields");
        airfieldDescriptorSet.setLocations(locations);
        AirfieldDescriptorIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "AirfieldLocations2.json", airfieldDescriptorSet);
    }
}
