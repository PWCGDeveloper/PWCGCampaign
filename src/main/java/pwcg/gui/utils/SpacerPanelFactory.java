package pwcg.gui.utils;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorSupport;

public class SpacerPanelFactory
{
    public static Pane makeSpacerPercentPanel(int percent) throws PWCGException
    {
            Pane spacerPanel = new Pane(new BorderLayout());
            spacerPanel.setOpaque(false);

            Pane spacerGrid = new Pane(new GridLayout(0,1));
            spacerGrid.setOpaque(false);
            
            Dimension frameSize = PWCGMonitorSupport.getPWCGFrameSize();
            double widthNeeded = Double.valueOf(frameSize.width) * (Double.valueOf(percent) / 100.0);
            int pixelsNeeded = Double.valueOf(widthNeeded).intValue();
            spacerGrid.setBorder(BorderFactory.createEmptyBorder(0, pixelsNeeded, 0, 0));

            Label spacer1 = ButtonFactory.makePaperLabelLarge(" ");
            spacerGrid.add(spacer1);

            spacerPanel.add(spacerGrid, BorderLayout.NORTH);

            return spacerPanel;
    }
    
    public static Pane makeSpacerConsumeRemainingPanel(int spaceToLeave) throws PWCGException
    {
            Dimension frameSize = PWCGMonitorSupport.getPWCGFrameSize();
            int widthNeeded = frameSize.width - spaceToLeave;
            if (widthNeeded < 1)
            {
                widthNeeded = 1;
            }

            Pane spacerGrid = new Pane(new GridLayout(0,1));
            spacerGrid.setOpaque(false);
            spacerGrid.setPreferredSize(new Dimension(widthNeeded, 300));
            
            Label spacer1 = ButtonFactory.makePaperLabelLarge("   ");
            spacer1.setForeground(Color.BLACK);
            spacerGrid.add(spacer1);

            return spacerGrid;
    }

    public static  Pane createVerticalSpacerPanel(int numRows) throws PWCGException
    {
        Pane spacerPanel = new Pane(new GridLayout(0, 1));
        spacerPanel.setOpaque(false);

        for (int i = 0; i < numRows; ++i)
        {
            Label space1 = new Label("     ");
            spacerPanel.add(space1);
        }

        return spacerPanel;
    }

    public static Pane makeDocumentSpacerPanel(int pixelsForUI) throws PWCGException
    {
        Pane spacerPanel = new Pane(new BorderLayout());
        spacerPanel.setOpaque(false);

        Pane spacerGrid = new Pane(new GridLayout(0,1));
        spacerGrid.setOpaque(false);
        
        Dimension frameSize = PWCGMonitorSupport.getPWCGFrameSize();
        int pixelsNeeded = frameSize.width - pixelsForUI;
        int pixelsNeededAdjusted = Double.valueOf(pixelsNeeded * .75).intValue();
        if (pixelsNeededAdjusted < 1)
        {
            pixelsNeededAdjusted = 1;
        }
        spacerGrid.setBorder(BorderFactory.createEmptyBorder(0,pixelsNeededAdjusted, 0, 0));

        Label spacer1 = ButtonFactory.makePaperLabelLarge(" ");
        spacerGrid.add(spacer1);

        spacerPanel.add(spacerGrid, BorderLayout.NORTH);

        return spacerPanel;
    }
}
