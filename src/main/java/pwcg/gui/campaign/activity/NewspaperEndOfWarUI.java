package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.image.ImageCache;

public class NewspaperEndOfWarUI extends Pane
{
    private static int NEWSPAPER_IMAGE_WIDTH = 800;
    private static int NEWSPAPER_IMAGE_HEIGHT = 1000;
    
    private static final long serialVersionUID = 1L;

    public NewspaperEndOfWarUI()
    {
        this.setOpaque(false);
    }

    public void makePanels() throws PWCGException
    {
        Pane newspaperImagePanel = buildNewspaperImagePanel();
        this.add(newspaperImagePanel, BorderLayout.CENTER);
    }

    private Pane buildNewspaperImagePanel() throws PWCGException
    {
        try
        {
            Pane newspaperImagePanel = new Pane();
            newspaperImagePanel.setOpaque(false);

            BufferedImage newspaperImage = buildNewspaperImage();
    
            ImageIcon icon = new ImageIcon(newspaperImage);
            Label imageLabel= new Label(icon);
            newspaperImagePanel.add(imageLabel, BorderLayout.CENTER);

            return newspaperImagePanel;
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
        
        return new Pane();
    }

    private BufferedImage buildNewspaperImage() throws PWCGException, IOException
    {
        String imagePath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Newspaper\\newspaperEndOfWar.png";
        BufferedImage newspaperImage = ImageCache.getImageFromFile(imagePath);
        BufferedImage resizedImage = resizeImage(newspaperImage);
        return resizedImage;
    }
    
    public static BufferedImage resizeImage(BufferedImage newspaperImage) {
        double newspaperRatio = Double.valueOf(NEWSPAPER_IMAGE_WIDTH) / Double.valueOf(NEWSPAPER_IMAGE_HEIGHT);

        Dimension pwcgDimensions = PWCGMonitorSupport.getPWCGFrameSize();
        Double height = (pwcgDimensions.getHeight() * .9);
        Double width = height * newspaperRatio;

        Image tmp = newspaperImage.getScaledInstance(width.intValue(), height.intValue(), Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width.intValue(), height.intValue(), BufferedImage.TRANSLUCENT);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }  
}
