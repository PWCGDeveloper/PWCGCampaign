package pwcg.gui.utils;

public class ImageResizingPanelBuilder
{
    public static final String NO_IMAGE = "";

	public static ImageResizingPanel makeImageResizingPanel(String imagePath)
	{
        ImageResizingPanel logCenterPanel = new ImageResizingPanel(imagePath);
        return logCenterPanel;
	}
}
