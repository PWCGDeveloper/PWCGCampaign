package pwcg.gui.rofmap;

import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.utils.ContextSpecificImages;

public abstract class MapLoader implements Runnable
{
    public MapLoader()
    {
    }

    protected void loadMap(String mapFileName)
    {
        try
        {
            String mapImageFullPath = ContextSpecificImages.imagesMaps() + mapFileName;
            ImageCache.getInstance().getBufferedImage(mapImageFullPath); 
        }
        catch (Exception e)
        {
            Logger.log(LogLevel.ERROR, "Failed to load map " + mapFileName);
            Logger.logException(e);
        }

    }

}
