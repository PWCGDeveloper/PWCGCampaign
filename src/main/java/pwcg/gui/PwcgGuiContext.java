package pwcg.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanel;

public abstract class PwcgGuiContext extends ImagePanel
{
    protected static final long serialVersionUID = 1L;

    private JPanel leftPanel = null;
    private JPanel rightPanel = null;
    private JPanel centerPanel = null;

    public PwcgGuiContext()
    {
        this.setLayout(new BorderLayout());
    }
    
    public JPanel getLeftPanel()
    {
        return leftPanel;
    }

    public JPanel getRightPanel()
    {
        return rightPanel;
    }

    public JPanel getCenterPanel() throws PWCGException
    {
        refreshScreen();
        return centerPanel;
    }

    public void setLeftPanel(JPanel newLeftPanel)
    {
        if (leftPanel != null)
        {
            remove(leftPanel);      
        }

        leftPanel = null;

        if (newLeftPanel != null)
        {
            leftPanel = newLeftPanel;
            
            add(leftPanel, BorderLayout.WEST);
            leftPanel.revalidate();
            repaintPanel(leftPanel, BorderLayout.WEST);
        }
    }

    public void setRightPanel(JPanel newRightPanel)
    {
        if (rightPanel != null)
        {
            remove(rightPanel);      
        }
        
        rightPanel = null;

        if (newRightPanel != null)
        {
            rightPanel = newRightPanel;
            
            add(rightPanel, BorderLayout.EAST);
            rightPanel.revalidate();
            repaintPanel(rightPanel, BorderLayout.EAST);
        }
        
    }

    public void setCenterPanel(JPanel newCenterPanel)
    {
        if (centerPanel != null)
        {
            remove(centerPanel);      
        }

        centerPanel = null;

        if (newCenterPanel != null)
        {
            this.centerPanel = newCenterPanel;
            add(centerPanel, BorderLayout.CENTER);
            centerPanel.revalidate();
            repaintPanel(centerPanel, BorderLayout.CENTER);
        }
    }

    protected void finishedWithCampaignScreen() throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        campaign.write();
        
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    protected String getSideImage(Campaign campaign, String leftImageName) throws PWCGException
    {
        String imagePath = "";

        int useGenericUI = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.UseGenericUIKey);

        if (useGenericUI == 1)
        {
            String sideImageName = "GenericSide.jpg";
            String menuPath = ContextSpecificImages.menuPathForGeneric();
            imagePath = menuPath + sideImageName;
        }
        else
        {
            String menuPath = ContextSpecificImages.menuPathForNation(campaign);
            imagePath = menuPath + leftImageName;
        }
        
        return imagePath;
        

    }

    protected String getSideImageMain(String leftImageName) throws PWCGException
    {
        String imagePath = "";

        int useGenericUI = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.UseGenericUIKey);

        if (useGenericUI == 1)
        {
            String sideImageName = "GenericSide.jpg";
            String menuPath = ContextSpecificImages.menuPathForGeneric();
            imagePath = menuPath + sideImageName;
        }
        else
        {
            String menuPath = ContextSpecificImages.menuPathMain();
            imagePath = menuPath + leftImageName;
        }
        
        return imagePath;
    }

    public void refreshScreen() throws PWCGException
    {
        // Override when necessary
    }

}
