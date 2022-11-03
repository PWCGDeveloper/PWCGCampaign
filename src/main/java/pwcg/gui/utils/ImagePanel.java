package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pwcg.gui.image.MapImageCache;

public abstract class ImagePanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    protected BufferedImage image;

    public ImagePanel()
    {
    }

    public ImagePanel(GridLayout layout)
    {
        super(layout);
    }

    public ImagePanel(BorderLayout layout)
    {
        super(layout);
    }

    public void setMapImage(MapImageCache mapImageCache, String mapImageName)
    {
        try
        {
            image = mapImageCache.getMapImage(mapImageName);
        }
        catch (Exception ex)
        {
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the
                                        // parameters
    }

    public Dimension getImageSize()
    {
        Dimension dimensions = new Dimension();
        if (image != null)
        {
            dimensions.width = image.getWidth();
            dimensions.height = image.getHeight();
        }
        return dimensions;
    }
}
