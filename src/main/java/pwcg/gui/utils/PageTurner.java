package pwcg.gui.utils;

import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;

public class PageTurner
{

    public static Pane makeButtonPanel(int pageNum, int pages, ActionListener actionComponent) throws PWCGException
    {
        Pane buttonPanel = new Pane(new GridLayout(0,2));
        buttonPanel.setOpaque(false);

        Pane leftButtonPanel = new Pane(new GridLayout(1,0));
        leftButtonPanel.setOpaque(false);
        buttonPanel.add(leftButtonPanel);

        Pane rightButtonPanel = new Pane(new GridLayout(1,0));
        rightButtonPanel.setOpaque(false);
        buttonPanel.add(rightButtonPanel);
        
        int spacingLabels = calculateCharsPerLine();

        if (pageNum > 1)
        {
            for (int i = 0; i < 1; ++i)
            {
                leftButtonPanel.add(ButtonFactory.makeDummy());
            }

            Font font = PWCGMonitorFonts.getPrimaryFont();
            Button prevButton = ButtonFactory.makeTranslucentMenuButton("Previous Page", "Previous Page", "Go to the previous page", actionComponent);
            prevButton.setForeground(ColorMap.CHALK_FOREGROUND);   
            prevButton.setFont(font);
            leftButtonPanel.add(prevButton);
            for (int i = 0; i < spacingLabels; ++i)
            {
                leftButtonPanel.add(ButtonFactory.makeDummy());
            }
        }
        
        if (pageNum  < pages)
        {
            
            for (int i = 0; i < spacingLabels; ++i)
            {
                rightButtonPanel.add(ButtonFactory.makeDummy());
            }

            Font font = PWCGMonitorFonts.getPrimaryFont();
            Button nextButton = ButtonFactory.makeTranslucentMenuButton("Next Page", "Next Page", "Go to the next page", actionComponent);
            nextButton.setForeground(ColorMap.CHALK_FOREGROUND);   
            nextButton.setFont(font);
            rightButtonPanel.add(nextButton);

            for (int i = 0; i < 1; ++i)
            {
                rightButtonPanel.add(ButtonFactory.makeDummy());
            }
        }
        
        makeVerticalBuffer(buttonPanel);
        
        return buttonPanel;
    }

    private static void makeVerticalBuffer(Pane buttonPanel)
    {
        int numSpacingLabels = 3;

        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            numSpacingLabels = 1;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            numSpacingLabels = 2;
        }
        else
        {
            numSpacingLabels = 3;
        }

        for (int i = 0; i < numSpacingLabels; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                buttonPanel.add(ButtonFactory.makeDummy());
            }
        }
    }

    private static int calculateCharsPerLine()
    {
        int numSpacingLabels = 5;

        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            numSpacingLabels = 1;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            numSpacingLabels = 2;
        }
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            numSpacingLabels = 3;
        }
        else
        {
            numSpacingLabels = 5;
        }
        
        return numSpacingLabels;
    }

}
