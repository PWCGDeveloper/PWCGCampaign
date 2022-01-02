package pwcg.gui.dialogs;

import java.awt.Dimension;
import java.awt.Rectangle;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.PWCGFrame;

public class PWCGMonitorSupport 
{
    public enum MonitorSize
    {
        FRAME_VERY_SMALL,
        FRAME_SMALL,
        FRAME_MEDIUM,
        FRAME_LARGE
    }
    
    public static MonitorSize getFrameWidth()
    {
        if (PWCGMonitorSupport.getPWCGFrameSize().width < 1400)
        {
            return MonitorSize.FRAME_VERY_SMALL;
        }
        else if (PWCGMonitorSupport.getPWCGFrameSize().width < 1600)
        {
            return MonitorSize.FRAME_SMALL;
        }
        else if (PWCGMonitorSupport.getPWCGFrameSize().width < 2000)
        {
            return MonitorSize.FRAME_MEDIUM;
        }
        else
        {
            return MonitorSize.FRAME_LARGE;
        }
    }
    
    public static MonitorSize getFrameHeight()
    {
        if (PWCGMonitorSupport.getPWCGFrameSize().height < 800)
        {
            return MonitorSize.FRAME_VERY_SMALL;
        }
        else if (PWCGMonitorSupport.getPWCGFrameSize().height < 900)
        {
            return MonitorSize.FRAME_SMALL;
        }
        else if (PWCGMonitorSupport.getPWCGFrameSize().height < 1150)
        {
            return MonitorSize.FRAME_MEDIUM;
        }
        else
        {
            return MonitorSize.FRAME_LARGE;
        }
    }

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

    public static int getCrewMemberPlateHeight() throws PWCGException
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

    public static boolean isVerySmallScreen()
    {
        if (getPWCGFrameSize().getHeight() <= 900)
        {
            return true;
        }
        
        return false;
    }

    public static boolean isSmallScreen()
    {
        if (getPWCGFrameSize().getHeight() <= 1100)
        {
            return true;
        }
        
        return false;
    }

    public static boolean isMediumScreen()
    {
        if (getPWCGFrameSize().getHeight() < 1400)
        {
            return true;
        }
        
        return false;
    }

}
