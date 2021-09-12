package pwcg.gui;

import java.awt.BorderLayout;

import javafx.scene.layout.Pane;

import pwcg.core.exception.PWCGException;

public class PwcgThreePanelUI
{
    private Pane parent;
    private Pane leftPanel;
    private Pane centerPanel;
    private Pane rightPanel;
    
    public PwcgThreePanelUI(Pane parent)
    {
        this.parent = parent;
    }

    public void setLeftPanel(Pane newLeftPanel) throws PWCGException
    {
        if (leftPanel != null)
        {
            parent.remove(leftPanel);
        }
        
        leftPanel = newLeftPanel;
        parent.add(BorderLayout.WEST, leftPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(parent);
    }

    public void setRightPanel(Pane newRightPanel) throws PWCGException
    {
        if (rightPanel != null)
        {
            parent.remove(rightPanel);
        }
        
        rightPanel = newRightPanel;
        parent.add(BorderLayout.EAST, rightPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(parent);
    }

    public void setCenterPanel(Pane newCenterPanel) throws PWCGException
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
