package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.newspapers.Newspaper;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.image.ImageCache;
import pwcg.gui.utils.TextGraphicsMeasurement;

public class NewspaperUI extends JPanel
{
    private static int NEWSPAPER_IMAGE_WIDTH = 800;
    private static int NEWSPAPER_IMAGE_HEIGHT = 1000;
    
    private static final long serialVersionUID = 1L;
    private Newspaper newspaper;

    public NewspaperUI(Newspaper newspaper)
    {
        this.setOpaque(false);
        this.newspaper = newspaper;
    }

    public void makePanels() throws PWCGException
    {
        JPanel newspaperImagePanel = buildNewspaperImagePanel();
        this.add(newspaperImagePanel, BorderLayout.CENTER);
    }

    private JPanel buildNewspaperImagePanel() throws PWCGException
    {
        try
        {
            JPanel newspaperImagePanel = new JPanel();
            newspaperImagePanel.setOpaque(false);

            BufferedImage newspaperImage = buildNewspaperImage();
    
            ImageIcon icon = new ImageIcon(newspaperImage);
            JLabel imageLabel= new JLabel(icon);
            newspaperImagePanel.add(imageLabel, BorderLayout.CENTER);

            return newspaperImagePanel;
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
        
        return new JPanel();
    }

    private BufferedImage buildNewspaperImage() throws PWCGException, IOException
    {
        String imagePath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Newspaper\\newspaper.png";
        BufferedImage newspaperImage = ImageCache.getImageFromFile(imagePath);

        BufferedImage newspaperImageWithPicture = addNewpaperPicture(newspaperImage);
        BufferedImage newspaperImageWithPictureAndHeadline = addheadline(newspaperImageWithPicture);
        BufferedImage resizedImage = resizeImage(newspaperImageWithPictureAndHeadline);
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

    private BufferedImage addNewpaperPicture(BufferedImage newspaperImage) throws PWCGException
    {
        String imagePicturePath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Newspaper\\" + newspaper.formNewspaperPictureName();
        BufferedImage newspaperPictureImage = ImageCache.getImageFromFile(imagePicturePath);
        if (newspaperPictureImage != null)
        {
            BufferedImage result = new BufferedImage(newspaperImage.getWidth(), newspaperImage.getHeight(), BufferedImage.TRANSLUCENT);
            Graphics g = result.getGraphics();
            g.drawImage(newspaperImage, 0, 0, null);
            g.drawImage(newspaperPictureImage, 235, 160, null);
            return result;
        }
        else
        {
            return newspaperImage;
        }
    }

    private BufferedImage addheadline(BufferedImage newspaperImage) throws IOException, PWCGException
    {
        BufferedImage result = new BufferedImage(newspaperImage.getWidth(), newspaperImage.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics graphics = result.getGraphics();
        graphics.setColor(Color.DARK_GRAY);
        
        Font headlineFont = PWCGMonitorFonts.getNewspaperFont();
        
        int pixelsForHeadline = TextGraphicsMeasurement.measureTextWidth(graphics, headlineFont, newspaper.getHeadline());        
        int newspaperImageWidth = 800;
        int startPosition = (newspaperImageWidth - pixelsForHeadline) / 2;
        if (startPosition < 40)
        {
            startPosition = 40;
        }

        
        graphics.setFont(headlineFont);
        graphics.drawImage(newspaperImage, 0, 0, null);
        graphics.drawString(newspaper.getHeadline(), startPosition, 120);
        return result;
    }
}
