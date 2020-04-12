package pwcg.gui.maingui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.CampaignRemover;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignDeletePanelSet extends PwcgGuiContext implements ActionListener
{    
    private static final long serialVersionUID = 1L;
    
    private List<JCheckBox> campaignCheckBoxes = new ArrayList<JCheckBox>();
    private CampaignMainGUI parent = null;

    public CampaignDeletePanelSet(CampaignMainGUI parent)
    {
        this.parent = parent;
    }

    public void makePanels() 
    {
        try
        {
            setLeftPanel(makeButtonPanel());
            setCenterPanel(makeCampaignSelectPanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        String imagePath = getSideImageMain("CampaignDeleteNav.jpg");
        
        ImageResizingPanel configPanel = new ImageResizingPanel(imagePath);
        configPanel.setLayout(new BorderLayout());
        configPanel.setOpaque(true);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setOpaque(false);

        JButton createCampaignButton = PWCGButtonFactory.makeMenuButton("Delete Selected Campaign", "Delete", this);
        buttonPanel.add(createCampaignButton);
        
        JLabel dummyLabel3 = new JLabel("     ");       
        dummyLabel3.setOpaque(false);
        buttonPanel.add(dummyLabel3);
        
        JButton cancelChanges = PWCGButtonFactory.makeMenuButton("Cancel", "Cancel", this);
        buttonPanel.add(cancelChanges);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return configPanel;
    }

    private JPanel makeCampaignSelectPanel() throws PWCGException
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperHalf.jpg";
        
        ImageResizingPanel campaignSelectPanel = new ImageResizingPanel(imagePath);
        campaignSelectPanel.setLayout(new BorderLayout());
        campaignSelectPanel.setOpaque(true);
        
        Font font = MonitorSupport.getPrimaryFontLarge();

        JPanel campaignSelectGrid = new JPanel(new GridLayout(0,1));
        campaignSelectGrid.setOpaque(false);
        
        List<String> campaigns = Campaign.getCampaignNames();
        for (String campaignName : campaigns)
        {
            JCheckBox campaignCheckBox = new JCheckBox();
            campaignCheckBox.setText(campaignName);
            campaignCheckBox.setSelected(false);
            campaignCheckBox.setOpaque(false);
            campaignCheckBox.setFont(font);
            
            campaignSelectGrid.add(campaignCheckBox);
            
            campaignCheckBoxes.add(campaignCheckBox);
        }
        
        campaignSelectPanel.add(campaignSelectGrid, BorderLayout.NORTH);
        
        return campaignSelectPanel;
    }
    

    @Override
    public void actionPerformed(ActionEvent ae)
    {        
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("Cancel"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("Delete"))
            {
                deleteSelectedCampaigns();
                parent.refresh();

                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void deleteSelectedCampaigns()
    {        
        for (JCheckBox campaignSelectedJCheckBox : campaignCheckBoxes)
        {
            if (campaignSelectedJCheckBox.isSelected())
            {
                CampaignRemover.deleteCampaign(campaignSelectedJCheckBox.getText());         
            }
        }
    }

 }
