package pwcg.gui.utils;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;

public class PageTurner
{

    public static JPanel makeButtonPanel(int pageNum, int pages, ActionListener actionComponent)
    {
        JPanel buttonPanel = new JPanel(new GridLayout(0,2));
        buttonPanel.setOpaque(false);

        JPanel leftButtonPanel = new JPanel(new GridLayout(1,0));
        leftButtonPanel.setOpaque(false);
        buttonPanel.add(leftButtonPanel);

        JPanel rightButtonPanel = new JPanel(new GridLayout(1,0));
        rightButtonPanel.setOpaque(false);
        buttonPanel.add(rightButtonPanel);
        
        int spacingLabels = calculateCharsPerLine();

        if (pageNum > 1)
        {
            for (int i = 0; i < 1; ++i)
            {
                leftButtonPanel.add(PWCGButtonFactory.makeDummy());
            }

            PWCGJButton prevButton = new PWCGJButton("Previous Page");
            prevButton.addActionListener(actionComponent);
            leftButtonPanel.add (prevButton);
            prevButton.setOpaque(false);
            prevButton.setForeground(ColorMap.CHALK_FOREGROUND);   
            prevButton.setBorderPainted(false);
            prevButton.setFocusPainted(false);
            leftButtonPanel.add(prevButton);
            
            for (int i = 0; i < spacingLabels; ++i)
            {
                leftButtonPanel.add(PWCGButtonFactory.makeDummy());
            }
        }
        
        if (pageNum  < pages)
        {
            
            for (int i = 0; i < spacingLabels; ++i)
            {
                rightButtonPanel.add(PWCGButtonFactory.makeDummy());
            }

            PWCGJButton nextButton = new PWCGJButton("Next Page");
            nextButton.addActionListener(actionComponent);
            rightButtonPanel.add (nextButton);
            nextButton.setOpaque(false);
            nextButton.setForeground(ColorMap.CHALK_FOREGROUND);   
            nextButton.setBorderPainted(false);
            nextButton.setFocusPainted(false);
            rightButtonPanel.add(nextButton);

            for (int i = 0; i < 1; ++i)
            {
                rightButtonPanel.add(PWCGButtonFactory.makeDummy());
            }
        }
        
        makeVerticalBuffer(buttonPanel);
        
        return buttonPanel;
    }

    private static void makeVerticalBuffer(JPanel buttonPanel)
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
                buttonPanel.add(PWCGButtonFactory.makeDummy());
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
