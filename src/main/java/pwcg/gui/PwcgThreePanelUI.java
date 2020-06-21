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

    public void setLeftPanel(JPanel coopPersonaInfoPanel) throws PWCGException
    {
        if (leftPanel != null)
        {
            parent.remove(leftPanel);
        }
        
        leftPanel = coopPersonaInfoPanel;
        parent.add(BorderLayout.WEST, leftPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(parent);
    }

    public void setRightPanel(JPanel coopPersonaInfoPanel) throws PWCGException
    {
        if (rightPanel != null)
        {
            parent.remove(rightPanel);
        }
        
        rightPanel = coopPersonaInfoPanel;
        parent.add(BorderLayout.EAST, rightPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(parent);
    }

    public void setCenterPanel(JPanel coopPersonaInfoPanel) throws PWCGException
    {
        if (centerPanel != null)
        {
            parent.remove(centerPanel);
        }
        
        centerPanel = coopPersonaInfoPanel;
        parent.add(BorderLayout.CENTER, centerPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(parent);
    }

}
