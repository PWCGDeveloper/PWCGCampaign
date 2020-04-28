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
import java.lang.ref.SoftReference;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class ImageCache 
{
	private static ImageCache instance = null;
	private static HashMap<String, SoftReference<BufferedImage>> bufferedImageCache = new HashMap<>();
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

	public BufferedImage getBufferedImage(String imagePath) throws PWCGException 
	{
        imagePath = imagePath.toLowerCase();
	    String themeImagePath = getThemePath(imagePath);
	    if(themeImagePath != null)
	    {
    	    BufferedImage image = getBufferedImageForPath(themeImagePath);
            if (image != null)
            {
                return image;
            }
	    }
        
	    BufferedImage image = getBufferedImageForPath(imagePath);
        if (image != null)
        {
            return image;
        }
        
        PWCGLogger.log(LogLevel.ERROR, "Image not found: " + imagePath);
        
        return image;
	}
	
	private BufferedImage getBufferedImageForPath(String imagePath) throws PWCGException 
    {
        SoftReference<BufferedImage> ref = bufferedImageCache.get(imagePath);
        BufferedImage image = ref != null ? ref.get() : null;
        if (image == null)
        {
            synchronized (this) 
            {
                image = getImageFromCache(imagePath);
                if (image != null)
                {
                    return image;
                }
                
                image = getImageFromFile(imagePath);
                if (image != null)
                {
                    return image;
                }
                
                PWCGLogger.log(LogLevel.ERROR, "Image not found: " + imagePath);
            }
        }
        
        return image;
    }


    private BufferedImage getImageFromCache(String imagePath)
    {
        SoftReference<BufferedImage> ref;
        BufferedImage image;
        ref = bufferedImageCache.get(imagePath);
        image = ref != null ? ref.get() : null;
        if (image != null)
        {
            return image;
        }
        return image;
    }

    private String getThemePath(String imagePath) throws PWCGException
    {
        if (!imagePath.contains("\\images\\")) 
        {
            return null;
        }
        
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        if (campaign == null)
        {
            return null;
        }
        
        SquadronMember referencePlayer = campaign.getPersonnelManager().getAnyCampaignMember(campaign.getCampaignData().getReferencePlayerSerialNumber());
        if (referencePlayer == null)
        {
            return null;
        }
        
        ArmedService service = referencePlayer.determineService(campaign.getDate());
        String substitute = "\\images\\themes\\" + service.getName() + "\\\\images\\"; 
        String themeImagePath = imagePath.replace("\\images\\", substitute);
        return themeImagePath;
    }

    private BufferedImage getImageFromFile(String imagePath) throws PWCGException
    {
        BufferedImage image = null;
        try
        {
            File file = new File(imagePath);
            if (file.exists())
            {
                image = ImageIO.read(new File(imagePath));
                bufferedImageCache.put(imagePath, new SoftReference<>(image));
            }
        }
        catch (IOException ioe)
        {
            PWCGLogger.logException(ioe);
            throw new PWCGException("Failed to load image file " + imagePath);
        }
        return image;
    }

	public BufferedImage getRotatedImage(String imagePath, int angle) throws PWCGException  
	{
		BufferedImage origImage = getBufferedImage(imagePath);
	    if (origImage == null) {

	        PWCGLogger.log(LogLevel.ERROR, "getRotatedImage: input image is null");
	      return null;

	    }

	    BufferedImage expandedImage = expandAndCenter(origImage);
	    
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
	
	public ImageIcon getRotatedImageIcon(String imagePath, int angle) throws PWCGException 
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
