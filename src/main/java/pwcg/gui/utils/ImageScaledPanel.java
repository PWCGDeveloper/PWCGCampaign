package pwcg.gui.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javafx.scene.layout.Pane;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.image.ImageCache;

/**
 * Resizes an image without altering its proportions
 * 
 * @author Patrick Wilson
 *
 */
public class ImageScaledPanel extends Pane
{
	private static final long serialVersionUID = 1L;
	
	protected Image image;
	protected String imagePath = "";
	
    protected double imageToScreenRatio = 0;
	
	public ImageScaledPanel(String imagePath, double ratio)
	{
		try 
		{
		    this.imageToScreenRatio = ratio;
		    this.imagePath = imagePath;
		    
			setImage(imagePath);
		}
		catch (Exception ex) 
		{
		}
	}

	
	public void makeVisible(boolean visible)
	{
		this.setVisible(visible);
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
    			    PWCGLogger.log(LogLevel.ERROR, "Request to load null image: " + imagePath);
    			}
            }
		}
		catch (Exception ex) 
		{
			PWCGLogger.logException(ex);
		}
	}

	@Override
	public void paintComponent(Graphics g) 
	{
		if (image != null)
		{
		    Double imageWidth = Integer.valueOf(image.getWidth(this)).doubleValue();
	        Double imageHeight = Integer.valueOf(image.getHeight(this)).doubleValue();
	        Double imageRatio = imageWidth / imageHeight;
	        
	        
            Double resizedHeight = PWCGMonitorSupport.getPWCGFrameSize().getHeight() * imageToScreenRatio;
            Double resizedWidth = resizedHeight * imageRatio;
	        
			Image resizedImage = image.getScaledInstance(resizedWidth.intValue(), resizedHeight.intValue(), Image.SCALE_DEFAULT);
			g.drawImage(resizedImage, 0, 0, null); // see javadoc for more info on the parameters
			resizedImage.flush();
		}
	}

	
	public Dimension getImageSize()
	{
		Dimension dimensions = new Dimension(image.getWidth(null),image.getHeight(null));
		
		return dimensions;
	}
}
