package pwcg.gui.utils;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pwcg.gui.image.ImageCache;

public class ImagePanelLayout extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	
	public ImagePanelLayout(String imagePath, LayoutManager layout)
	{
		setLayout(layout);
		
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
		g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
	}
}
