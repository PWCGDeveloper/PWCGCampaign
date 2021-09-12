package pwcg.gui.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.image.ImageCache;

public class ImageResizingPanel extends Scene
{
    private static final long serialVersionUID = 1L;
    public static final String NO_IMAGE = "";
	
    private Image image;
    protected String imagePath = "";
    private Map<String, Scene> children = new HashMap<>();

    public ImageResizingPanel(String imagePath)
    {
        super(oldRoot);
        this.imagePath = imagePath;
        if (!imagePath.isEmpty())
        {
            setImageFromName(imagePath);
        }
    }

    public ImageResizingPanel(BufferedImage image)
    {
        super(oldRoot);
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        if (image != null)
        {
            Image resizedImage = image.getScaledInstance(Float.valueOf(this.getWidth()).intValue(), Float.valueOf(this.getHeight()).intValue(), Image.SCALE_DEFAULT);
            g.drawImage(resizedImage, 0, 0, null);
            resizedImage.flush();
            
            for (Scene panel : children.values())
            {
                panel.revalidate();
                panel.repaint();
            }
        }
    }

    protected void addChild(String panelId, Scene panel)
    {
        children.put(panelId, panel);
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public void setImageFromName(String imagePath)
	{
		try 
		{
            if (imagePath != "")
            {
    			image = ImageCache.getInstance().getBufferedImage(imagePath);
    			if (image == null)
    			{
    	                PWCGLogger.log(LogLevel.ERROR, "Request to load null resizing image: " + imagePath);
    			}
            }
		}
		catch (Exception ex) 
		{
			PWCGLogger.logException(ex);
		}
	}
	
	public Dimension getImageSize()
	{
		Dimension dimensions = new Dimension(image.getWidth(null),image.getHeight(null));
		
		return dimensions;
	}
}
