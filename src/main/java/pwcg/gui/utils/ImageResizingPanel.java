package pwcg.gui.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.dialogs.ImageCache;

public class ImageResizingPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	protected Image image;
	protected String imagePath = "";

	public ImageResizingPanel(String imagePath)
	{
		try 
		{
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
			    PWCGLogger.log(LogLevel.ERROR, "Request to load null image: " + imagePath);
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
			Image resizedImage = image.getScaledInstance(Float.valueOf(this.getWidth()).intValue(), Float.valueOf(this.getHeight()).intValue(), Image.SCALE_DEFAULT);
			g.drawImage(resizedImage, 0, 0, null); // see javadoc for more info on the parameters
			resizedImage.flush();
		}
		else
		{
		    PWCGLogger.log(LogLevel.ERROR, "Request to paint null image: " + imagePath);
		}
	}

	
	public Dimension getImageSize()
	{
		Dimension dimensions = new Dimension(image.getWidth(null),image.getHeight(null));
		
		return dimensions;
	}
}
