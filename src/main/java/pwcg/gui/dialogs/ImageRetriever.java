package pwcg.gui.dialogs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class ImageRetriever
{

    public static BufferedImage getImageFromFile(String mapOverlayPath) throws PWCGException
    {
        BufferedImage image = null;
        try
        {
            File file = new File(mapOverlayPath);
            if (file.exists())
            {
                image = ImageIO.read(new File(mapOverlayPath));
            }
        }
        catch (IOException ioe)
        {
            PWCGLogger.logException(ioe);
            throw new PWCGException("Failed to load image file " + mapOverlayPath);
        }
        return image;
    }

}
