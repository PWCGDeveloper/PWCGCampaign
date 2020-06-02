package pwcg.gui.utils;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JTextArea;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.image.ImageCache;

public class ImageTextArea extends JTextArea
{
	private static final long serialVersionUID = 1L;
	protected Image image;

	/**
	 * @param imagePath
	 */
	public ImageTextArea(String imagePath)  
	{  
		setImage(imagePath);

		//Make JTextArea transparent  
		setOpaque(false);  

		//Make JTextArea line wrap  
		setLineWrap(true);  

		//Make JTextArea word wrap  
		setWrapStyleWord(true);  
	}  
	
	/**
	 * @param imagePath
	 */
	public void setImage(String imagePath)
	{
		try 
		{
			image = ImageCache.getInstance().getBufferedImage(imagePath);
		}
		catch (Exception ex) 
		{
		    PWCGLogger.log(LogLevel.ERROR, "Request to load null image: " + imagePath);
		}
	}


	//Override JTextArea paint method  
	//It enable us to paint JTextArea background image  
	public void paint(Graphics g)  
	{  
		//Draw JTextArea background image  
		//g.drawImage(image, 0, 0, null, this);  
		
		Image resizedImage = image.getScaledInstance(Float.valueOf(this.getWidth()).intValue(), Float.valueOf(this.getHeight()).intValue(), Image.SCALE_DEFAULT);
		g.drawImage(resizedImage, 0, 0, null); // see javadoc for more info on the parameters
		resizedImage.flush();

		//Call super.paint. If we don't do this...We can't see JTextArea  
		super.paint(g);  
	}   
}
