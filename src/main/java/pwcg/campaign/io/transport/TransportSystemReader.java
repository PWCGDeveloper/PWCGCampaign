package pwcg.campaign.io.transport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class TransportSystemReader
{
    private MapTransportSystem mapTransportSystem = new MapTransportSystem();
    
    public MapTransportSystem readTransportSystemFile (String mapName) throws PWCGException
    {
        try 
        {
            String iconFileLocation = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\" + "roadsSystem.ini";           
            readLocationFile(iconFileLocation);            
        } 
        catch (IOException e) 
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        
        return mapTransportSystem;
    }

    private void readLocationFile(String fileLocation) throws FileNotFoundException, IOException,
                    PWCGException
    {
        BufferedReader reader = null;
        try 
        {
            reader = new BufferedReader(new FileReader(fileLocation));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.contains("Map_Width"))
                {
                    double mapWidth = parseParameter(line);
                    mapTransportSystem.setMapWidth(mapWidth);
                }
                else if (line.contains("Map_Height"))
                {
                    double mapHeight = parseParameter(line);
                    mapTransportSystem.setMapHeight(mapHeight);
                }
                else if (line.contains("Map_ScaleFactor"))
                {
                    double mapScaleFactor = parseParameter(line);
                    mapTransportSystem.setMapScaleFactor(mapScaleFactor);
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
    }

    private double parseParameter(String line)
    {
        int start = line.indexOf("=");
        String stringValue = line.substring(start + 1);
        double value = Double.valueOf(stringValue);
        return value;
    }

}
