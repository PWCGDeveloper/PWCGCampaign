package pwcg.gui.campaign.pilot;

import java.awt.Dimension;

import pwcg.gui.dialogs.MonitorSupport;

public class PageSizeCalculator
{
    private int linesPerPage = 50;
    private int charsPerLine = 50;
    
    public void calculateTextSize()
    {
        Dimension screenSize = MonitorSupport.getPWCGFrameSize();
        calculateLinesPerPage(screenSize);
        calculateCharsPerLine(screenSize);
    }


    private void calculateLinesPerPage(Dimension screenSize)
    {
        linesPerPage = 35;
        if (screenSize.getHeight() < 1200)
        {
            linesPerPage = 32;
        }
        if (screenSize.getHeight() < 1050)
        {
            linesPerPage = 27;
        }
        if (screenSize.getHeight() < 800)
        {
            linesPerPage = 22;
        }
    }

    private void calculateCharsPerLine(Dimension screenSize)
    {
        charsPerLine = 60;
        if (screenSize.getWidth() < 1200)
        {
            charsPerLine = 50;
        }
        if (screenSize.getWidth() < 1000)
        {
            charsPerLine = 40;
        }
        if (screenSize.getWidth() < 800)
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
