package pwcg.gui.dialogs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import javax.imageio.ImageIO;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.utils.ContextSpecificImages;

public class MapImageCache 
{
	private static MapImageCache instance = null;
	private static HashMap<String, SoftReference<BufferedImage>> bufferedMapImageCache = new HashMap<>();
	
	private MapImageCache ()
	{
	}
	
	public static MapImageCache getInstance() 
	{
		if (instance == null)
		{
			instance = new MapImageCache();
		}
		
		return instance;
	}

	public BufferedImage getMapImage(String mapImageFileName) throws PWCGException 
	{
        String mapImageFullPath = ContextSpecificImages.imagesMaps() + mapImageFileName;

        mapImageFullPath = mapImageFullPath.toLowerCase();
	    if(mapImageFullPath != null)
	    {
    	    BufferedImage image = getBufferedImageForPath(mapImageFullPath);
            if (image != null)
            {
                return image;
            }
	    }
        
	    BufferedImage image = getBufferedImageForPath(mapImageFullPath);
        if (image != null)
        {
            return image;
        }
        
        PWCGLogger.log(LogLevel.ERROR, "Image not found: " + mapImageFullPath);
        
        return image;
	}

    private BufferedImage getBufferedImageForPath(String imagePath) throws PWCGException 
    {
        SoftReference<BufferedImage> ref = bufferedMapImageCache.get(imagePath);
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
        ref = bufferedMapImageCache.get(imagePath);
        image = ref != null ? ref.get() : null;
        if (image != null)
        {
            return image;
        }
        return image;
    }

    private String getOverlayPath(String imagePath) throws PWCGException
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
                bufferedMapImageCache.put(imagePath, new SoftReference<>(image));
            }
        }
        catch (IOException ioe)
        {
            PWCGLogger.logException(ioe);
            throw new PWCGException("Failed to load image file " + imagePath);
        }
        return image;
    }

}
