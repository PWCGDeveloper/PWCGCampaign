package pwcg.gui.utils;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;

public class PwcgBorderFactory
{
    public static Border createStandardDocumentBorder()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_LARGE)
        {
            return BorderFactory.createEmptyBorder(30,30,80,120);
        }
        else if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return BorderFactory.createEmptyBorder(20,20,50,70);
        }
        else 
        {
            return BorderFactory.createEmptyBorder(15,15,40,60);
        }
    }
    
    public static Border createDocumentBorderWithExtraSpaceFromTop()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_LARGE)
        {
            return BorderFactory.createEmptyBorder(300,30,80,120);
        }
        else if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return BorderFactory.createEmptyBorder(200,20,50,70);
        }
        else 
        {
            return BorderFactory.createEmptyBorder(100,15,40,60);
        }
    }
    
    public static Border createPlaqueBackgroundBorder()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_LARGE)
        {
            return BorderFactory.createEmptyBorder(50,50,70,70);
        }
        else if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return BorderFactory.createEmptyBorder(40,40,60,60);
        }
        else 
        {
            return BorderFactory.createEmptyBorder(20,20,30,30);
        }
    }

    public static Border createMedalBoxBorder()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_LARGE)
        {
            return BorderFactory.createEmptyBorder(75,75,100,100);
        }
        else if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return BorderFactory.createEmptyBorder(50,50,75,75);
        }
        else 
        {
            return BorderFactory.createEmptyBorder(30,30,50,50);
        }
    }
    
    public static Border createCampaignHomeChalkboardBoxBorder()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_LARGE)
        {
            return BorderFactory.createEmptyBorder(75,75,100,100);
        }
        else if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return BorderFactory.createEmptyBorder(30,30,50,50);
        }
        else 
        {
            return BorderFactory.createEmptyBorder(20,20,40,40);
        }
    }
    
    public static Dimension createSideTextPreferredSize()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_LARGE)
        {
            return new Dimension(500, 900);
        }
        else if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return new Dimension(400, 800);
        }
        else 
        {
            return new Dimension(300, 700);
        }
    }
  
}
