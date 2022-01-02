package pwcg.gui.campaign.crewmember;

import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;

public class PageSizeCalculator
{
    private int linesPerPage = 50;
    private int charsPerLine = 50;
    
    public void calculateTextSize()
    {
        calculateLinesPerPage();
        calculateCharsPerLine();
    }


    private void calculateLinesPerPage()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        linesPerPage = 35;
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            linesPerPage = 32;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            linesPerPage = 27;
        }
        else if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            linesPerPage = 22;
        }
    }

    private void calculateCharsPerLine()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        charsPerLine = 60;
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            charsPerLine = 50;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            charsPerLine = 40;
        }
        else if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            charsPerLine = 30;
        }
    }


    public int getLinesPerPage()
    {
        return linesPerPage;
    }


    public int getCharsPerLine()
    {
        return charsPerLine;
    }

}
