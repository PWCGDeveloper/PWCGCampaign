package pwcg.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.PWCGFrame;

public class PwcgGuiCurrentPanel extends JPanel
{
    protected static final long serialVersionUID = 1L;

    public PwcgGuiCurrentPanel()
    {
        this.setLayout(new BorderLayout());
    }

    public void displayContext(PwcgThreePanelUI newUI) 
    {
        try
        {            
            PWCGFrame.getInstance().clearPanel();

            clean();
            
            setLeftPanel(newUI.getLeftPanel());
            setCenterPanel(newUI.getCenterPanel());
            setRightPanel(newUI.getRightPanel());
            
            PWCGFrame.getInstance().setPanel(newUI);

        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void setLeftPanel(JPanel newLeftPanel)
    {
        if (newLeftPanel != null)
        {
            add(newLeftPanel, BorderLayout.WEST);
            repaintPanel(newLeftPanel);
        }
    }

    private void setRightPanel(JPanel newRightPanel)
    {
        if (newRightPanel != null)
        {
            add(newRightPanel, BorderLayout.EAST);
            repaintPanel(newRightPanel);
        }        
    }

    private void setCenterPanel(JPanel newCenterPanel)
    {
        if (newCenterPanel != null)
        {
            add(newCenterPanel, BorderLayout.CENTER);
            repaintPanel(newCenterPanel);
        }        
    }
    
    private void clean() throws PWCGException  
    {       
        removeAll();
    }

    private void repaintPanel(JPanel newPanel)
    {
        if (newPanel != null)
        {
            newPanel.revalidate();
            newPanel.repaint();
        }
    }
}
