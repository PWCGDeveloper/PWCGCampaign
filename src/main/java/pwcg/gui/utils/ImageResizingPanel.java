package pwcg.gui.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.image.ImageCache;

public class ImageResizingPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    public static final String NO_IMAGE = "";
	
    private Image image;
    protected String imagePath = "";
    private Map<String, JPanel> children = new HashMap<>();

    public ImageResizingPanel(String imagePath)
    {
        this.imagePath = imagePath;
        if (!imagePath.isEmpty())
        {
            setImage(imagePath);
        }
    }

    public ImageResizingPanel(BufferedImage image)
    {
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
            
            for (JPanel panel : children.values())
            {
                panel.revalidate();
                panel.repaint();
            }
        }
    }

    protected void addChild(String panelId, JPanel panel)
    {
        children.put(panelId, panel);
    }

    public void setImage(String imagePath)
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
