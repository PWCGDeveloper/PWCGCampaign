package pwcg.campaign.io.transport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

public class TransportReader
{
    private MapTransport mapTransport = new MapTransport();
    private MapTransportSystem mapTransportSystem = new MapTransportSystem();

    public static void main(String[] args) 
    {
        try
        {
            PWCGContext.setProduct(PWCGProduct.BOS);
            TransportReader transportReader = new TransportReader();
            MapTransport mapTransport = transportReader.readTransportFile("Bodenplatte", "railroads.ini");
            System.out.println(mapTransport.toString());
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
    }
    
    public MapTransport readTransportFile (String mapName, String transportFileName) throws PWCGException
    {
        try 
        {
            TransportSystemReader transportReader = new TransportSystemReader();
            mapTransportSystem = transportReader.readTransportSystemFile(mapName);

            String transportLocationFile = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\" + transportFileName;           
            readLocationFile(transportLocationFile);            
        } 
        catch (IOException e) 
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        
        return mapTransport;
    }

    private void readLocationFile(String transportLocationFile) throws FileNotFoundException, IOException,
                    PWCGException
    {
        BufferedReader reader = null;
        try 
        {
            reader = new BufferedReader(new FileReader(transportLocationFile));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (!line.isEmpty())
                {
                    parseTransportRoute(line);
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

    private void parseTransportRoute(String line)
    {
        List<Coordinate> transportRoute = new ArrayList<>();
        String[] dataElements = line.split(" ");
        for (String element : dataElements) 
        {
            if (element.contains(","))
            {
                TransportLocationValue transportLocation = parseParameter(element);

                double coordinateX = (mapTransportSystem.getMapHeight() - transportLocation.yr) * mapTransportSystem.getMapScaleFactor();
                double coordinateZ = transportLocation.xr * mapTransportSystem.getMapScaleFactor();
                
                coordinateX = (double) Math.round(coordinateX * 1000) / 1000;
                coordinateZ = (double) Math.round(coordinateZ * 1000) / 1000;
                                
                Coordinate transportCoordinate = new Coordinate(coordinateX, 0.0, coordinateZ);
                transportRoute.add(transportCoordinate);
            }
        }
        mapTransport.addTransportLocations(transportRoute);
    }


    private TransportLocationValue parseParameter(String element)
    {
        int start = element.indexOf(",");
        String stringValueX = element.substring(0, start);
        String stringValueY = element.substring(start + 1);
        
        TransportLocationValue transportLocation = new TransportLocationValue();
        transportLocation.xr = Double.valueOf(stringValueX);
        transportLocation.yr = Double.valueOf(stringValueY);
        return transportLocation;
    }

    private class TransportLocationValue
    {
        double xr;
        double yr;
    }
}
