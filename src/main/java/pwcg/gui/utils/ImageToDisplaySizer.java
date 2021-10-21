package pwcg.gui.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import pwcg.gui.dialogs.PWCGMonitorSupport;

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

}
