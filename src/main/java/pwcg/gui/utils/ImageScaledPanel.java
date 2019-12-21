package pwcg.gui.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.dialogs.MonitorSupport;

/**
 * Resizes an image without altering its proportions
 * 
 * @author Patrick Wilson
 *
 */
public class ImageScaledPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	protected Image image;
	protected String imagePath = "";
	
    protected double imageToScreenRatio = 0;
	
	public ImageScaledPanel(String imagePath, double ratio)
	{
		try 
		{
		    //this.setOpaque(false);
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
			image = ImageCache.getInstance().getBufferedImage(imagePath);
			if (image == null)
			{
			    Logger.log(LogLevel.ERROR, "Request to load null image: " + imagePath);
			}
		}
		catch (Exception ex) 
		{
			Logger.logException(ex);
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
	        
	        
            Double resizedHeight = MonitorSupport.getPWCGFrameSize().getHeight() * imageToScreenRatio;
            Double resizedWidth = resizedHeight * imageRatio;
	        
			Image resizedImage = image.getScaledInstance(resizedWidth.intValue(), resizedHeight.intValue(), Image.SCALE_DEFAULT);
			g.drawImage(resizedImage, 0, 0, null); // see javadoc for more info on the parameters
			resizedImage.flush();
		}
		else
		{
		    Logger.log(LogLevel.ERROR, "Request to paint null image: " + imagePath);
		}
	}

	
	public Dimension getImageSize()
	{
		Dimension dimensions = new Dimension(image.getWidth(null),image.getHeight(null));
		
		return dimensions;
	}
}
