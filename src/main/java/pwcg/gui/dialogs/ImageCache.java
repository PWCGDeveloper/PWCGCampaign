package pwcg.gui.dialogs;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class ImageCache 
{
	private static ImageCache instance = null;
	private static HashMap<String, BufferedImage> bufferedImageCache = new HashMap<String, BufferedImage>();
	private static HashMap<String, ImageIcon> imageIconCache = new HashMap<String, ImageIcon>();
	
	private ImageCache ()
	{
	}
	
	public static ImageCache getInstance() 
	{
		if (instance == null)
		{
			instance = new ImageCache();
		}
		
		return instance;
	}

	public BufferedImage getBufferedImage(String imagePath) throws PWCGIOException 
	{
		try
        {
            BufferedImage image = bufferedImageCache.get(imagePath);
            if (image == null)
            {
            	File file = new File(imagePath);
            	if (file.exists())
            	{
            		image = ImageIO.read(new File(imagePath));
            		bufferedImageCache.put(imagePath, image);
            	}
            	else
            	{
            	    Logger.log(LogLevel.ERROR, "Image not found: " + imagePath);
            	}
            }
            
            return image;
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	public BufferedImage getRotatedImage(String imagePath, int angle) throws PWCGIOException  
	{
		BufferedImage origImage = getBufferedImage(imagePath);
	    if (origImage == null) {

	        Logger.log(LogLevel.ERROR, "getRotatedImage: input image is null");
	      return null;

	    }

	    BufferedImage expandedImage = expandAndCenter(origImage);
	    
        //Get current GraphicsConfiguration
        GraphicsConfiguration graphicsConfiguration 
                = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

	    BufferedImage dest =  graphicsConfiguration.createCompatibleImage(expandedImage.getWidth(), expandedImage.getHeight(), Transparency.TRANSLUCENT );
	    Graphics2D g2d = dest.createGraphics();

	    AffineTransform origAT = g2d.getTransform(); 

	    AffineTransform rot = new AffineTransform(); 
	    rot.rotate(Math.toRadians(angle), expandedImage.getWidth()/2, expandedImage.getHeight()/2); 
	    g2d.transform(rot); 

	    g2d.drawImage(expandedImage, 0, 0, null);   

	    g2d.setTransform(origAT);   
	    g2d.dispose();

	    return dest; 
	}
	
	
	public BufferedImage expandAndCenter(BufferedImage origImage)  
	{
        //Get current GraphicsConfiguration
        GraphicsConfiguration graphicsConfiguration 
                = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
        
        int scaleDivisor = 2;
        int doubleScaleDivisor = scaleDivisor * 2;

        int newWidth = origImage.getWidth() + (origImage.getWidth() / scaleDivisor);
        int newHeight = origImage.getHeight() + (origImage.getHeight() / scaleDivisor);
	    BufferedImage dest =  graphicsConfiguration.createCompatibleImage(newWidth, newHeight, Transparency.TRANSLUCENT );
	    Graphics2D g2d = dest.createGraphics();

	    g2d.drawImage(dest, origImage.getWidth() / doubleScaleDivisor, origImage.getHeight() / doubleScaleDivisor, origImage.getWidth(), origImage.getHeight(), null, null);
	    
	    AffineTransform origAT = g2d.getTransform(); 
	    AffineTransform shift = new AffineTransform(); 
	    shift.translate(.1, .1);
	    g2d.transform(shift); 

	    g2d.drawImage(origImage, origImage.getWidth() / doubleScaleDivisor, origImage.getHeight() / doubleScaleDivisor, null);   
	    g2d.setTransform(origAT);   
	    
	    g2d.dispose();

	    return dest; 
	}
	
	public ImageIcon getRotatedImageIcon(String imagePath, int angle) throws PWCGIOException 
	{
		BufferedImage bufferedImage = getRotatedImage(imagePath, angle);
		ImageIcon image = new ImageIcon(bufferedImage);  
		
		return image;
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
            Logger.log(LogLevel.ERROR, "Error readingimage file " + imagePath);
            Logger.logException(e);
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
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

	
}
