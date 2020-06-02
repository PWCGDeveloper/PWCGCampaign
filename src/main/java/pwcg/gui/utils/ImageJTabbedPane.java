package pwcg.gui.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JTabbedPane;

import pwcg.gui.image.ImageCache;

public class ImageJTabbedPane extends JTabbedPane
{
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	
	public ImageJTabbedPane()
	{
	}

	public void setImage(String imagePath)
	{
		try 
		{
			image = ImageCache.getInstance().getBufferedImage(imagePath);
		}
		catch (Exception ex) 
		{
		}
	}

	@Override
	public void paintComponent(Graphics g) 
	{
		g.drawImage(image, 0, 0, null);
		super.paintComponent(g);
	}
}
