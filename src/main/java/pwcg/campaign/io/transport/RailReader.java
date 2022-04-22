package pwcg.campaign.io.transport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class RailReader
{

    public static MapTransport readLargeFields (String mapName) throws PWCGException
    {
        MapTransport railLocations = new MapTransport();

        try 
        {
            String iconFileLocation = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\" + "railroads.ini";           
            railLocations = readLocationFile(iconFileLocation);            
        } 
        catch (IOException e) 
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        
        return railLocations;
    }

    private static MapTransport readLocationFile(String fileLocation) throws FileNotFoundException, IOException,
                    PWCGException
    {
        MapTransport railLocations = new MapTransport();
        BufferedReader reader = null;
        try 
        {
            reader = new BufferedReader(new FileReader(fileLocation));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (!line.isEmpty())
                {
                    List<Double> transportRoute = new ArrayList<>();
                    String[] dataElements = line.split(",");
                    for (String element : dataElements) {
                        double transportLocation = Double.valueOf(element);
                        transportRoute.add(transportLocation);
                    }
                }
            }
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }

        return railLocations;
    }

}
