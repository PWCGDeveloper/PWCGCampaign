package pwcg.gui.image;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.utils.ContextSpecificImages;

public class MapImageCache implements Runnable
{
    private HashMap<String, BufferedImage> bufferedMapImageCache = new HashMap<>();
    private String mapFileNameBase = "";
    private boolean isLoaded = false;
    private Thread mapLoadThread = null;

    public MapImageCache(FrontMapIdentifier mapIdentifier)
    {
        this.mapFileNameBase = PWCGContext.getInstance().getMap(mapIdentifier).getMapName() + "Map";
    }

    public void loadCurrentMap()
    {
        try
        {
            if (isLoaded)
            {
                return;
            }
            
            loadPrimaryMap();
            isLoaded = true;

            mapLoadThread = new Thread(this);
            mapLoadThread.start();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }
    
    private void loadPrimaryMap()
    {
        try
        {
            getMapImage(mapFileNameBase + "100"); 
        }
        catch (Exception e)
        {
            PWCGLogger.log(LogLevel.ERROR, "Failed to load map " + mapFileNameBase + "100");
            PWCGLogger.logException(e);
        }
    }

    @Override
    public void run()
    {
        try
        {
            getMapImage(mapFileNameBase + "050"); 
            getMapImage(mapFileNameBase + "075"); 
            getMapImage(mapFileNameBase + "125"); 
            getMapImage(mapFileNameBase + "150"); 
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
        } 
    }

    public BufferedImage getMapImage(String mapImageFileName) throws PWCGException
    {
        BufferedImage mapImageForDisplay = getImageFromCache(mapImageFileName);
        if (mapImageForDisplay != null)
        {
            return mapImageForDisplay;
        }

        mapImageForDisplay = buildMapWithOverlay(mapImageFileName);
        if (mapImageForDisplay != null)
        {
            bufferedMapImageCache.put(mapImageFileName, mapImageForDisplay);
            return mapImageForDisplay;
        }

        return null;
    }

    private BufferedImage getImageFromCache(String mapImageFileName)
    {
        BufferedImage mapImage = bufferedMapImageCache.get(mapImageFileName);
        return mapImage;
    }

    private BufferedImage buildMapWithOverlay(String mapImageFileName) throws PWCGException
    {
        BufferedImage mapImage = getMapImageNoOverlay(mapImageFileName);
        return mapImage;
    }

    private BufferedImage getMapImageNoOverlay(String mapImageFileName) throws PWCGException
    {
        String mapFullPath = ContextSpecificImages.imagesMaps() + mapImageFileName + ".jpg";
        BufferedImage mapImage = ImageRetriever.getImageFromFile(mapFullPath);
        return mapImage;
    }
}
