package pwcg.dev.jsonconvert.orig.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.Block;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.Parsers;
import pwcg.mission.flight.plane.PlaneMCU;

public class AirfieldNameBuilder 
{    
    TreeMap<String, PWCGLocation> airfieldLocations = new TreeMap<>();
    TreeMap<String, List<PWCGLocation>> airfieldRunways = new TreeMap<>();
    TreeMap<String, PWCGLocation> airfields = new TreeMap<>();
    
    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        AirfieldNameBuilder jsonConverter = new AirfieldNameBuilder();
        jsonConverter.getAirfieldNames("Bodenplatte");
    }

    private void getAirfieldNames (String mapName) throws Exception 
    {
        String filename = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\RhineAirfieldMap.Mission";     
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
            List<PWCGLocation> runways = airfieldRunways.get(airfieldLocationName);
            String airfieldName = stripAirfieldLocationName(airfieldLocationName);
            PWCGLocation airfield = new PWCGLocation();
            airfield.setName(airfieldLocationName);
            airfield.setPosition(runways.get(0).getPosition());
            airfield.setOrientation(runways.get(0).getOrientation());
            airfield.setName(airfieldName);
            
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
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
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
                
                if (line.startsWith(DevIOConstants.PLANE))
                {
                    PlaneMCU planeThatMarksRunway = PlaneIO.readBlock(reader);
                    PWCGLocation location = new PWCGLocation();
                    location.setPosition(planeThatMarksRunway.getPosition().copy());
                    location.setOrientation(planeThatMarksRunway.getOrientation().copy());
                    findAirfieldForRunway(location);
                }
            }
            
            reader.close();
        } 
        catch (Exception e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
    
    private void findAirfieldForRunway(PWCGLocation runwayLocation)
    {
        double closestDistance = 1000000000.0;
        PWCGLocation closestAirfield = null;
        for (PWCGLocation airfieldLocation : airfieldLocations.values())
        {
            double distance = MathUtils.calcDist(runwayLocation.getPosition(), airfieldLocation.getPosition());
            if (distance < closestDistance)
            {
                closestDistance = distance;
                closestAirfield = airfieldLocation;
            }
        }
        
        if (!airfieldRunways.containsKey(closestAirfield.getName()))
        {
            List<PWCGLocation> airfieldRunwayList = new ArrayList<>();
            airfieldRunways.put(closestAirfield.getName(), airfieldRunwayList);
        }
        List<PWCGLocation> airfieldRunwayList = airfieldRunways.get(closestAirfield.getName());
        airfieldRunwayList.add(runwayLocation);
    }

    private void writeAirfields(String mapName) throws PWCGException
    {
        List<PWCGLocation> locations = new ArrayList<>(airfields.values());
        LocationSet airfieldsForMap = new LocationSet("Airfields");
        airfieldsForMap.setLocations(locations);
        LocationIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "AirfieldLocations.json", airfieldsForMap);
    }
}
