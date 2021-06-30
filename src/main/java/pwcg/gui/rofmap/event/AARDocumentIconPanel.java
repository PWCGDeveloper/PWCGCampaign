package pwcg.gui.rofmap.event;

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

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.image.ImageCache;
import pwcg.gui.utils.TextGraphicsMeasurement;

public abstract class AARDocumentIconPanel extends JPanel implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;

    public static final int DOCUMENT_MARGIN = 80;

    protected String headerText = "";
    protected String bodyText = "";
    protected String footerImagePath = "";

    protected Font headerFont;
    protected Font bodyFont;
    protected boolean shouldDisplay = false;

    protected abstract String getHeaderText() throws PWCGException;
    protected abstract String getBodyText() throws PWCGException;
    protected abstract String getFooterImagePath() throws PWCGException;
    
    public AARDocumentIconPanel()
    {
    }

    @Override
    public void makePanel() throws PWCGException
    {
        this.headerText = getHeaderText();
        this.bodyText = getBodyText();
        this.footerImagePath = getFooterImagePath();

        makeDocumentPanel();        
    }

    private void makeDocumentPanel() throws PWCGException
    {

        try
        {
            headerFont = PWCGMonitorFonts.getDecorativeFont();
            bodyFont = PWCGMonitorFonts.getTypewriterFont();

            this.setOpaque(false);

            BufferedImage documentIMage = buildDocumentImage();
            ImageIcon icon = new ImageIcon(documentIMage);
            JLabel imageLabel = new JLabel(icon);
            this.add(imageLabel, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    private BufferedImage buildDocumentImage() throws PWCGException, IOException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        BufferedImage documentImage = ImageCache.getInstance().getBufferedImage(imagePath);

        BufferedImage documentImageWithHeader = addHeader(documentImage);
        BufferedImage documentImageWithBody = addBody(documentImageWithHeader);
        BufferedImage documentImageWithFooter = addFooterImage(documentImageWithBody);
        BufferedImage resizedImage = resizeImage(documentImageWithFooter);
        return resizedImage;
    }

    private BufferedImage addHeader(BufferedImage documentImage) throws IOException, PWCGException
    {
        if (headerText.isEmpty())
        {
            return documentImage;
        }

        BufferedImage result = new BufferedImage(documentImage.getWidth(), documentImage.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics graphics = result.getGraphics();
        graphics.setColor(Color.DARK_GRAY);

        int pixelsForHeadline = TextGraphicsMeasurement.measureTextWidth(graphics, headerFont, headerText);
        int lineHorizontalPosition = (documentImage.getWidth() - pixelsForHeadline) / 2;
        int lineVerticalPosition = 120;
        if (lineHorizontalPosition < 40)
        {
            lineHorizontalPosition = 40;
        }

        graphics.setFont(headerFont);
        graphics.drawImage(documentImage, 0, 0, null);
        graphics.drawString(headerText, lineHorizontalPosition, lineVerticalPosition);
        return result;
    }

    private BufferedImage addBody(BufferedImage documentImage) throws IOException, PWCGException
    {
        if (bodyText.isEmpty())
        {
            return documentImage;
        }

        Graphics graphics = documentImage.getGraphics();

        int lineHorizontalPosition = 40;
        int lineVerticalPosition = 240;
        int characterBufferPosition = 0;
        while (characterBufferPosition < bodyText.length())
        {
            StringBuffer lineBuffer = new StringBuffer();
            while (characterBufferPosition < bodyText.length())
            {
                lineBuffer.append(bodyText.charAt(characterBufferPosition));
                int pixelsForBodyLine = TextGraphicsMeasurement.measureTextWidth(graphics, bodyFont, lineBuffer.toString());
                if (pixelsForBodyLine > (documentImage.getWidth() - DOCUMENT_MARGIN))
                {
                    lineBuffer.deleteCharAt(lineBuffer.length() - 1);
                    break;
                }
                else if (bodyText.charAt(characterBufferPosition) == '\n')
                {
                    ++characterBufferPosition;
                    break;
                }
                else
                {
                    ++characterBufferPosition;
                }
            }

            documentImage = addBodyLine(documentImage, lineHorizontalPosition, lineVerticalPosition, lineBuffer);
            lineVerticalPosition += 30;
        }
        return documentImage;
    }

    private BufferedImage addBodyLine(BufferedImage documentImage, int lineHorizontalPosition, int lineVerticalPosition, StringBuffer lineBuffer) throws IOException, PWCGException
    {
        if (bodyText.isEmpty())
        {
            return documentImage;
        }

        BufferedImage result = new BufferedImage(documentImage.getWidth(), documentImage.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics graphics = result.getGraphics();
        graphics.setColor(Color.DARK_GRAY);
        graphics.setFont(bodyFont);
        graphics.drawImage(documentImage, 0, 0, null);
        graphics.drawString(lineBuffer.toString(), lineHorizontalPosition, lineVerticalPosition);

        return result;
    }

    private BufferedImage addFooterImage(BufferedImage documentImage) throws PWCGException
    {
        if (footerImagePath.isEmpty())
        {
            return documentImage;
        }

        BufferedImage documentPictureImage = ImageCache.getImageFromFile(footerImagePath);
        if (documentPictureImage != null)
        {
            BufferedImage result = new BufferedImage(documentImage.getWidth(), documentImage.getHeight(), BufferedImage.TRANSLUCENT);
            Graphics g = result.getGraphics();
            g.drawImage(documentImage, 0, 0, null);
            g.drawImage(documentPictureImage, 500, 160, null);
            return result;
        }
        else
        {
            return documentImage;
        }
    }

    public static BufferedImage resizeImage(BufferedImage documentImage)
    {
        double newspaperRatio = Double.valueOf(documentImage.getWidth()) / Double.valueOf(documentImage.getHeight());

        Dimension pwcgDimensions = PWCGMonitorSupport.getPWCGFrameSize();
        Double height = (pwcgDimensions.getHeight() * .9);
        Double width = height * newspaperRatio;

        Image tmp = documentImage.getScaledInstance(width.intValue(), height.intValue(), Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width.intValue(), height.intValue(), BufferedImage.TRANSLUCENT);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    @Override
    public boolean isShouldDisplay()
    {
        return shouldDisplay;
    }

    @Override
    public JPanel getPanel()
    {
        return this;
    }
}
