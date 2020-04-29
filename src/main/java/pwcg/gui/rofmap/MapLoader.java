package pwcg.gui.rofmap;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.dialogs.MapImageCache;

public class MapLoader implements Runnable
{
    private String mapFileNameBase;
    
    public MapLoader(String mapFileNameBase)
    {
        this.mapFileNameBase = mapFileNameBase;
    }

    protected void loadPrimaryMap()
    {
        try
        {
            MapImageCache.getInstance().getMapImage(mapFileNameBase + "100"); 
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
            MapImageCache.getInstance().getMapImage(mapFileNameBase + "050"); 
            MapImageCache.getInstance().getMapImage(mapFileNameBase + "075"); 
            MapImageCache.getInstance().getMapImage(mapFileNameBase + "125"); 
            MapImageCache.getInstance().getMapImage(mapFileNameBase + "150"); 
        }
        catch (PWCGException e)
        {
            
        } 
    }
}
