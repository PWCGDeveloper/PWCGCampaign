package pwcg.gui.utils;

import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PWCGFrame
{	
    private static PWCGFrame pwcgFrame = null;
    
    private Stage pwcgStage = new Stage();
	
	public static PWCGFrame getInstance()
	{
		if (pwcgFrame == null)
		{
		    pwcgFrame = new PWCGFrame();
		}
		
		return pwcgFrame;
	}
	
	private PWCGFrame()
	{
		super();
		
		pwcgStage.setTitle("PWCG");
		pwcgStage.initModality(Modality.APPLICATION_MODAL);

        // Dimension2D screenSize = PWCGMonitorSupport.getPWCGMonitorSize();
        Dimension2D screenSize = new Dimension2D(1600, 900);
		pwcgStage.setWidth(screenSize.getWidth());
		pwcgStage.setHeight(screenSize.getHeight());
		
        pwcgStage.show();
 	}

	public void setPanel(Scene newPanel)
	{
	    pwcgStage.setScene(newPanel);
        pwcgStage.show();
	}

    public Rectangle getBounds()
    {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(pwcgStage.getWidth());
        rectangle.setHeight(pwcgStage.getHeight());
        return rectangle;
    }
}
