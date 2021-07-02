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
import java.util.ArrayList;
import java.util.List;

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
    private Graphics originalDocumentImageGraphics = null;

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

            BufferedImage documentImage = buildDocumentImage();
            BufferedImage folderImage = buildFolderImage(documentImage);

            BufferedImage resizedImage = resizeImage(folderImage);

            ImageIcon icon = new ImageIcon(resizedImage);
            JLabel imageLabel = new JLabel(icon);
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
        BufferedImage folderImage = ImageCache.getInstance().getBufferedImage(imagePath);
        
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
        BufferedImage documentImage = ImageCache.getInstance().getBufferedImage(imagePath);
        originalDocumentImageGraphics = documentImage.getGraphics();

        BufferedImage documentImageWithHeader = addHeader(documentImage);
        BufferedImage documentImageWithBody = addBody(documentImageWithHeader, 240);
        BufferedImage documentImageWithFooter = addFooterImage(documentImageWithBody, 500);
        return documentImageWithFooter;
     }

    private BufferedImage addHeader(BufferedImage documentImage) throws IOException, PWCGException
    {
        if (headerText.isEmpty())
        {
            return documentImage;
        }

        List<String> bodyLinesOfText = formBodyLinesOfText(documentImage, headerText, headerFont);

        int lineHorizontalPosition = 40;
        int lineVerticalPosition = 120;
        for (String headerLineOfText :bodyLinesOfText)
        {
            documentImage = addTextLine(documentImage, lineHorizontalPosition, lineVerticalPosition, headerLineOfText, headerFont, JLabel.CENTER_ALIGNMENT);
            lineVerticalPosition += 30;
        }
        
        return documentImage;
    }

    private BufferedImage addBody(BufferedImage documentImage, int verticalStartPosition) throws IOException, PWCGException
    {
        if (bodyText.isEmpty())
        {
            return documentImage;
        }

        List<String> bodyLinesOfText = formBodyLinesOfText(documentImage, bodyText, bodyFont);

        int lineHorizontalPosition = 40;
        int lineVerticalPosition = verticalStartPosition;
        for (String bodyLineOfText :bodyLinesOfText)
        {
            documentImage = addTextLine(documentImage, lineHorizontalPosition, lineVerticalPosition, bodyLineOfText, bodyFont, JLabel.LEFT_ALIGNMENT);
            lineVerticalPosition += 25;
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
