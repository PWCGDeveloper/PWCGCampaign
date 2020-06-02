package pwcg.gui.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class ImageIconCache 
{
	private static ImageIconCache instance = null;
	private static HashMap<String, ImageIcon> imageIconCache = new HashMap<String, ImageIcon>();
	
	private ImageIconCache ()
	{
	}
	
	public static ImageIconCache getInstance() 
	{
		if (instance == null)
		{
			instance = new ImageIconCache();
		}
		
		return instance;
	}

	public ImageIcon getImageIcon(String imagePath) throws PWCGIOException 
	{
		try
        {
            ImageIcon image = imageIconCache.get(imagePath);
            if (image == null)
            {
            	BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
            	image = new ImageIcon(bufferedImage);  
            	imageIconCache.put(imagePath, image);
            }
            
            return image;
        }
        catch (IOException e)
        {
            PWCGLogger.log(LogLevel.ERROR, "Error readingimage file " + imagePath);
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

    public ImageIcon getImageIconResized(String imagePath) throws PWCGIOException 
    {
        try
        {
            ImageIcon image = imageIconCache.get(imagePath);
            if (image == null)
            {
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
                image = new ImageIcon(bufferedImage);  

                int width = 250;
                int height = 150;
                
                BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image.getImage(), 0, 0, width, height, null);
                g2.dispose();
                
                image = new ImageIcon(resizedImg);
                imageIconCache.put(imagePath, image);
            }
            
            return image;
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
}
