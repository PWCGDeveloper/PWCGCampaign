package pwcg.gui.campaign.home;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;

public class CampaignHomeBottomSpacerBuilder
{
    
    public static  JPanel createLowerSpacerPanel() throws PWCGException
    {
        JPanel spacerPanel = new JPanel(new GridLayout(0, 1));
        spacerPanel.setOpaque(false);

        for (int i = 0; i < 10; ++i)
        {
            JLabel space1 = new JLabel("     ");
            spacerPanel.add(space1);
        }

        return spacerPanel;
    }
}
