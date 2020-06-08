package pwcg.gui.dialogs;

import java.awt.Dimension;
import java.awt.Rectangle;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.PWCGFrame;

public class PWCGMonitorSupport 
{
	public static Dimension getPWCGFrameSize()
	{
        Dimension frameSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        
	    Rectangle r = PWCGFrame.getInstance().getBounds();
	    frameSize.setSize(r.width, r.height);	    

		return frameSize;
	}

    public static Dimension getPWCGMonitorSize()
    {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.height -= 40;
        
        // Or use the user specified value
        int screenSizeAuto;
        try
        {
            screenSizeAuto = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ScreenSizeAutoKey);
            if (screenSizeAuto == 0)
            {
                screenSize.height = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ScreenSizeHeightKey);
                screenSize.width = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ScreenSizeWidthKey);
            }
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }


        return screenSize;
    }

    public static int getPilotPlateHeight() throws PWCGException
    {
        if (isSmallScreen())
        {
            return 25;
        }
        if (isMediumScreen())
        {
            return 30;
        }
        
        return 40;
    }

    static boolean isVerySmallScreen()
    {
        if (getPWCGFrameSize().getHeight() <= 900)
        {
            return true;
        }
        
        return false;
    }

    static boolean isSmallScreen()
    {
        if (getPWCGFrameSize().getHeight() <= 1100)
        {
            return true;
        }
        
        return false;
    }

    static boolean isMediumScreen()
    {
        if (getPWCGFrameSize().getHeight() < 1400)
        {
            return true;
        }
        
        return false;
    }

}
