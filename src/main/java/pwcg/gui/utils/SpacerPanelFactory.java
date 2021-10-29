package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorSupport;

public class SpacerPanelFactory
{
    public static JPanel makeSpacerPercentPanel(int percent) throws PWCGException
    {
            JPanel spacerPanel = new JPanel(new BorderLayout());
            spacerPanel.setOpaque(false);

            JPanel spacerGrid = new JPanel(new GridLayout(0,1));
            spacerGrid.setOpaque(false);
            
            Dimension frameSize = PWCGMonitorSupport.getPWCGFrameSize();
            double widthNeeded = Double.valueOf(frameSize.width) * (Double.valueOf(percent) / 100.0);
            int pixelsNeeded = Double.valueOf(widthNeeded).intValue();
            spacerGrid.setBorder(BorderFactory.createEmptyBorder(0, pixelsNeeded, 0, 0));

            spacerGrid.add(PWCGLabelFactory.makeDummyLabel());

            spacerPanel.add(spacerGrid, BorderLayout.NORTH);

            return spacerPanel;
    }
    
    public static JPanel makeSpacerConsumeRemainingPanel(int spaceToLeave) throws PWCGException
    {
            Dimension frameSize = PWCGMonitorSupport.getPWCGFrameSize();
            int widthNeeded = frameSize.width - spaceToLeave;
            if (widthNeeded < 1)
            {
                widthNeeded = 1;
            }

            JPanel spacerGrid = new JPanel(new GridLayout(0,1));
            spacerGrid.setOpaque(false);
            spacerGrid.setPreferredSize(new Dimension(widthNeeded, 300));
            
            spacerGrid.add(PWCGLabelFactory.makeDummyLabel());

            return spacerGrid;
    }

    public static  JPanel createVerticalSpacerPanel(int numRows) throws PWCGException
    {
        JPanel spacerPanel = new JPanel(new GridLayout(0, 1));
        spacerPanel.setOpaque(false);

        for (int i = 0; i < numRows; ++i)
        {
            spacerPanel.add(PWCGLabelFactory.makeDummyLabel());
        }

        return spacerPanel;
    }

    public static JPanel makeDocumentSpacerPanel(int pixelsForUI) throws PWCGException
    {
        JPanel spacerPanel = new JPanel(new BorderLayout());
        spacerPanel.setOpaque(false);

        JPanel spacerGrid = new JPanel(new GridLayout(0,1));
        spacerGrid.setOpaque(false);
        
        Dimension frameSize = PWCGMonitorSupport.getPWCGFrameSize();
        int pixelsNeeded = frameSize.width - pixelsForUI;
        int pixelsNeededAdjusted = Double.valueOf(pixelsNeeded * .75).intValue();
        if (pixelsNeededAdjusted < 1)
        {
            pixelsNeededAdjusted = 1;
        }
        spacerGrid.setBorder(BorderFactory.createEmptyBorder(0,pixelsNeededAdjusted, 0, 0));

        spacerGrid.add(PWCGLabelFactory.makeDummyLabel());

        spacerPanel.add(spacerGrid, BorderLayout.NORTH);

        return spacerPanel;
    }
}
