package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.Dimension;
import javafx.scene.text.Font;
import javafx.scene.text.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.image.ImageCache;

public class NewspaperAceLostUI extends Pane
{
    private static int NEWSPAPER_IMAGE_WIDTH = 800;
    private static int NEWSPAPER_IMAGE_HEIGHT = 1000;
    
    private static final long serialVersionUID = 1L;
    private SquadronMember ace;

    public NewspaperAceLostUI(SquadronMember ace)
    {
        this.setOpaque(false);

        this.ace = ace;
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
        String imagePath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Newspaper\\newspaperAce.png";
        BufferedImage newspaperImage = ImageCache.getImageFromFile(imagePath);

        BufferedImage newspaperImageWithPicture = addAcePicture(newspaperImage);
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

    private BufferedImage addAcePicture(BufferedImage newspaperImage) throws PWCGException
    {
        BufferedImage acePicture = ace.getPilotPictureAsBufferedImage();
        if (acePicture != null)
        {
            BufferedImage result = new BufferedImage(newspaperImage.getWidth(), newspaperImage.getHeight(), BufferedImage.TRANSLUCENT);
            Graphics g = result.getGraphics();
            g.drawImage(newspaperImage, 0, 0, null);
            g.drawImage(acePicture, 10, 165, null);
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
        
        String headline = ace.getNameAndRank() + " Lost In Combat";
        int pixelsForHeadline = measureTextWidth(graphics, headlineFont, headline);        
        int newspaperImageWidth = 800;
        int startPosition = (newspaperImageWidth - pixelsForHeadline) / 2;
        if (startPosition < 40)
        {
            startPosition = 40;
        }

        
        graphics.setFont(headlineFont);
        graphics.drawImage(newspaperImage, 0, 0, null);
        graphics.drawString(headline, startPosition, 120);
        return result;
    }
    
    private int measureTextWidth(Graphics graphics, Font font, String text)
    {
        FontMetrics metrics = graphics.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        return textWidth;
    }
}
