package pwcg.gui.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorSupport;

public class PageTurner
{
    /**
     * 
     */
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


        Color bg = ColorMap.PAPER_BACKGROUND;
        Color fg = ColorMap.PAPER_FOREGROUND;
        
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
            prevButton.setBackground(bg);
            prevButton.setForeground(fg);   
            prevButton.setBorderPainted(false);
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
            nextButton.setBackground(bg);   
            nextButton.setForeground(fg);   
            nextButton.setBorderPainted(false);
            rightButtonPanel.add(nextButton);

            for (int i = 0; i < 1; ++i)
            {
                rightButtonPanel.add(PWCGButtonFactory.makeDummy());
            }
        }
        
        makeVerticalBuffer(buttonPanel);
        
        return buttonPanel;
    }
    
   
    

    /**
     * @param screenSize
     */
    private static void makeVerticalBuffer(JPanel buttonPanel)
    {
        Dimension screenSize = PWCGMonitorSupport.getPWCGFrameSize();
        
        int numSpacingLabels = 3;
        if (screenSize.getHeight() < 1000)
        {
            numSpacingLabels = 2;
        }
        if (screenSize.getHeight() < 800)
        {
            numSpacingLabels = 1;
        }
        
        for (int i = 0; i < numSpacingLabels; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                buttonPanel.add(PWCGButtonFactory.makeDummy());
            }
        }
    }


    /**
     * @param screenSize
     */
    private static int calculateCharsPerLine()
    {
        Dimension screenSize = PWCGMonitorSupport.getPWCGFrameSize();
        
        int numSpacingLabels = 5;
        if (screenSize.getWidth() < 1200)
        {
            numSpacingLabels = 3;
        }
        if (screenSize.getWidth() < 1000)
        {
            numSpacingLabels = 2;
        }
        if (screenSize.getWidth() < 800)
        {
            numSpacingLabels = 1;
        }
        
        return numSpacingLabels;
    }

}
