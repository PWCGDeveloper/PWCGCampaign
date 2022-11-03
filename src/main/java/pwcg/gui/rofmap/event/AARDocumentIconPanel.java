package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.image.ImageCache;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.TextGraphicsMeasurement;

public abstract class AARDocumentIconPanel extends JPanel implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;

    public static final int DOCUMENT_MARGIN = 100;

    protected ArmedService service = null;
    protected String headerText = "";
    protected String bodyText = "";
    protected String footerImagePath = "";

    protected Font headerFont = null;
    protected Font bodyFont = null;
    protected boolean shouldDisplay = false;
    private Graphics originalDocumentImageGraphics = null;

    protected abstract String getHeaderText() throws PWCGException;
    protected abstract String getBodyText() throws PWCGException;
    protected abstract String getFooterImagePath() throws PWCGException;
    
    public AARDocumentIconPanel(Campaign campaign)
    {
        try
        {
            service = campaign.getReferenceService();
        }
        catch (PWCGException e)
        {
            PWCGLogger.log(LogLevel.ERROR, "No reference service found for campaign");
        }
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
            this.setOpaque(false);

            BufferedImage documentImage = buildDocumentImage();
            BufferedImage folderImage = buildFolderImage(documentImage);

            BufferedImage resizedImage = ImageToDisplaySizer.resizeImage(folderImage);

            ImageIcon icon = new ImageIcon(resizedImage);
            JLabel imageLabel= PWCGLabelFactory.makeIconLabel(icon);
            this.add(imageLabel, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    private BufferedImage buildFolderImage(BufferedImage documentImage) throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.DocumentFolder);
        BufferedImage folderImage = ImageCache.getInstance().getBufferedImageByTheme(imagePath, service);
        
        int verticalStartPosition = 50;
        int horizontalStartPosition = 50;

        BufferedImage result = new BufferedImage(folderImage.getWidth(), folderImage.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics g = result.getGraphics();
        g.drawImage(folderImage, 0, 0, null);
        g.drawImage(documentImage, horizontalStartPosition, verticalStartPosition, null);
        return result;
    }
    
    private BufferedImage buildDocumentImage() throws PWCGException, IOException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        BufferedImage documentImage = ImageCache.getInstance().getBufferedImageByTheme(imagePath, service);
        originalDocumentImageGraphics = documentImage.getGraphics();

        BufferedImage documentImageWithHeader = addHeader(documentImage);
        BufferedImage documentImageWithBody = addBody(documentImageWithHeader, 90);
        BufferedImage documentImageWithFooter = addFooterImage(documentImageWithBody, 500);
        return documentImageWithFooter;
     }

    private BufferedImage addHeader(BufferedImage documentImage) throws IOException, PWCGException
    {
        if (headerText.isEmpty())
        {
            return documentImage;
        }
        
        if (headerFont == null)
        {
            headerFont = PWCGMonitorFonts.getDecorativeFontWithSize(PWCGMonitorFonts.LARGE_SCREEN_FONT_SIZE);
        }

        List<String> bodyLinesOfText = formBodyLinesOfText(documentImage, headerText, headerFont);

        int lineHorizontalPosition = 40;
        int lineVerticalPosition = 30;
        for (String headerLineOfText :bodyLinesOfText)
        {
            documentImage = addTextLine(documentImage, lineHorizontalPosition, lineVerticalPosition, headerLineOfText, headerFont, JLabel.CENTER_ALIGNMENT);
            lineVerticalPosition += 22;
        }
        
        return documentImage;
    }

    private BufferedImage addBody(BufferedImage documentImage, int lineVerticalPosition) throws IOException, PWCGException
    {
        if (bodyText.isEmpty())
        {
            return documentImage;
        }
        
        if (bodyFont == null)
        {
            bodyFont = PWCGMonitorFonts.getTypewriterFontWithSize(PWCGMonitorFonts.SMALL_SCREEN_FONT_SIZE);
        }

        List<String> bodyLinesOfText = formBodyLinesOfText(documentImage, bodyText, bodyFont);

        int lineHorizontalPosition = 40;
        for (String bodyLineOfText :bodyLinesOfText)
        {
            documentImage = addTextLine(documentImage, lineHorizontalPosition, lineVerticalPosition, bodyLineOfText, bodyFont, JLabel.LEFT_ALIGNMENT);
            lineVerticalPosition += 17;
        }
        
        return documentImage;
    }
    
    private List<String> formBodyLinesOfText(BufferedImage documentImage, String text, Font font)
    {
        List<String> bodyLinesOfText = new ArrayList<>();
        
        int characterBufferPosition = 0;
        while (characterBufferPosition < text.length())
        {
            StringBuffer lineBuffer = new StringBuffer();
            while (characterBufferPosition < text.length())
            {
                lineBuffer.append(text.charAt(characterBufferPosition));
                int pixelsForBodyLine = TextGraphicsMeasurement.measureTextWidth(originalDocumentImageGraphics, font, lineBuffer.toString());
                if (pixelsForBodyLine > (documentImage.getWidth() - DOCUMENT_MARGIN))
                {
                    lineBuffer.deleteCharAt(lineBuffer.length() - 1);                    
                    while (text.charAt(characterBufferPosition) != ' ')
                    {
                        --characterBufferPosition;
                        lineBuffer.deleteCharAt(lineBuffer.length() - 1);
                    }
                    
                    bodyLinesOfText.add(lineBuffer.toString());
                    break;
                }
                else if (text.charAt(characterBufferPosition) == '\n')
                {
                    bodyLinesOfText.add(lineBuffer.toString());
                    ++characterBufferPosition;
                    break;
                }
                else if (characterBufferPosition == (text.length()-1))
                {
                    bodyLinesOfText.add(lineBuffer.toString());
                    ++characterBufferPosition;
                    break;
                }
                else
                {
                    ++characterBufferPosition;
                }
            }
        }
        return bodyLinesOfText;
    }

    private BufferedImage addTextLine(BufferedImage documentImage, int lineHorizontalPosition, int lineVerticalPosition, String line, Font font, float alignment) throws IOException, PWCGException
    {
        BufferedImage result = new BufferedImage(documentImage.getWidth(), documentImage.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics graphics = result.getGraphics();

        if (alignment == JLabel.CENTER_ALIGNMENT)
        {
            int lineWidthPixels = TextGraphicsMeasurement.measureTextWidth(originalDocumentImageGraphics, headerFont, line);
            lineHorizontalPosition = (documentImage.getWidth() - lineWidthPixels) / 2;
        }
        
        graphics.setColor(Color.DARK_GRAY);
        graphics.setFont(font);
        graphics.drawImage(documentImage, 0, 0, null);
        graphics.drawString(line, lineHorizontalPosition, lineVerticalPosition);

        return result;
    }

    private BufferedImage addFooterImage(BufferedImage documentImage, int verticalStartPosition) throws PWCGException
    {
        if (footerImagePath.isEmpty())
        {
            return documentImage;
        }
        
        BufferedImage documentPictureImage = ImageCache.getImageFromFile(footerImagePath);
        if (documentPictureImage != null)
        {
            int horizontalStartPosition = (documentImage.getWidth() / 2) - (documentPictureImage.getWidth() / 2);

            BufferedImage result = new BufferedImage(documentImage.getWidth(), documentImage.getHeight(), BufferedImage.TRANSLUCENT);
            Graphics g = result.getGraphics();
            g.drawImage(documentImage, 0, 0, null);
            g.drawImage(documentPictureImage, horizontalStartPosition, verticalStartPosition, null);
            return result;
        }
        else
        {
            return documentImage;
        }
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
