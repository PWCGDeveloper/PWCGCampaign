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
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.Parsers;

public class CityNameBuilder 
{    
    public static void main(String[] args) throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        CityNameBuilder jsonConverter = new CityNameBuilder();
        jsonConverter.getCityNames("Arras");
    }

    private void getCityNames (String mapName) throws PWCGException 
    {
        String filename = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\Arras_ALL.Group";     
        readGroundObjectsFromFile(filename, mapName);

    }

    private void readGroundObjectsFromFile (String fileName, String mapName) throws PWCGException 
	{
		try 
		{
		    TreeMap<String, PWCGLocation> cityLocations = new TreeMap<>();
		    
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
            boolean cityFound = false;
            String cityName = "";
			while ((line = reader.readLine()) != null) 
			{
				line = line.trim();
				
                if (line.startsWith(DevIOConstants.NAME))
                {
                    cityName = Parsers.getString(line);
                    if (!(cityName.equals("Cities") || cityName.equals("Block")))
                    {
                        cityFound = true;
                    }
                }

				if (line.startsWith(DevIOConstants.BLOCK))
				{
                    Block block = GroupIO.readBlock(reader);
                    if (cityFound)
                    {
                        cityFound = false;
                        
                        PWCGLocation location = new PWCGLocation();
                        location.setName(cityName);
                        location.setPosition(block.getPosition().copy());
                        location.setOrientation(block.getOrientation().copy());
                        cityLocations.put(cityName, location);
                    }
				}
			}
			
			reader.close();
			
            List<PWCGLocation> locations = new ArrayList<>(cityLocations.values());
            LocationSet cities = new LocationSet("MapLocations");
            cities.setLocations(locations);
            
            LocationIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "MapLocations.json", cities);
		} 
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}
}
