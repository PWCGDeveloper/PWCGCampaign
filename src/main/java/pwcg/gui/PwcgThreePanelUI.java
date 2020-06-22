package pwcg.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;

public class PwcgThreePanelUI
{
    private JPanel parent;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    
    public PwcgThreePanelUI(JPanel parent)
    {
        this.parent = parent;
    }

    public void setLeftPanel(JPanel newLeftPanel) throws PWCGException
    {
        if (leftPanel != null)
        {
            parent.remove(leftPanel);
        }
        
        leftPanel = newLeftPanel;
        parent.add(BorderLayout.WEST, leftPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(parent);
    }

    public void setRightPanel(JPanel newRightPanel) throws PWCGException
    {
        if (rightPanel != null)
        {
            parent.remove(rightPanel);
        }
        
        rightPanel = newRightPanel;
        parent.add(BorderLayout.EAST, rightPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(parent);
    }

    public void setCenterPanel(JPanel newCenterPanel) throws PWCGException
    {
        if (centerPanel != null)
        {
            parent.remove(centerPanel);
        }
        
        centerPanel = newCenterPanel;
        parent.add(BorderLayout.CENTER, centerPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(parent);
    }

}
