package pwcg.gui.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.intel.CampaignIntelligenceEnemySquadronsGUI;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.image.ImageCache;

public class ImageToDisplaySizer
{

    public static BufferedImage resizeImage(BufferedImage image)
    {
        Dimension imagePanelDimensions = getDimensionsForScreen(image);
                
        Image tmp = image.getScaledInstance(imagePanelDimensions.width, imagePanelDimensions.height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(imagePanelDimensions.width, imagePanelDimensions.height, BufferedImage.TRANSLUCENT);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    public static Dimension getDimensionsForScreen(BufferedImage documentImage)
    {
        double imageRatio = Double.valueOf(documentImage.getWidth()) / Double.valueOf(documentImage.getHeight());

        Dimension pwcgDimensions = PWCGMonitorSupport.getPWCGFrameSize();
        Double height = (pwcgDimensions.getHeight() * .9);
        Double width = height * imageRatio;

        Dimension imagePanelDimensions = new Dimension(width.intValue(), height.intValue());

        return imagePanelDimensions;
    }


    public static void setDocumentSize(JPanel centerPanel) throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        BufferedImage documentImage = ImageCache.getImageFromFile(imagePath);
        Dimension imagePanelDimensions = ImageToDisplaySizer.getDimensionsForScreen(documentImage);
        centerPanel.setPreferredSize(new Dimension(imagePanelDimensions.width, imagePanelDimensions.height));
    }

    public static void setDocumentSizeWithMultiplier(JPanel panel, int multiplier) throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        BufferedImage documentImage = ImageCache.getImageFromFile(imagePath);
        Dimension imagePanelDimensions = ImageToDisplaySizer.getDimensionsForScreen(documentImage);
        panel.setPreferredSize(new Dimension((imagePanelDimensions.width * multiplier), imagePanelDimensions.height));
         
    }

}
