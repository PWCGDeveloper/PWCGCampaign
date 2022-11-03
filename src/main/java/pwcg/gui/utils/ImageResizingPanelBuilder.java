package pwcg.gui.utils;

import java.awt.image.BufferedImage;

public class ImageResizingPanelBuilder
{
    public static final String NO_IMAGE = "";

    public static ImageResizingPanel makeImageResizingPanel(BufferedImage image)
    {
        ImageResizingPanel logCenterPanel = new ImageResizingPanel(image);
        return logCenterPanel;
    }
}
