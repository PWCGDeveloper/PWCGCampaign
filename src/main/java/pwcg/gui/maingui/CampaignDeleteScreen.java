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
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignDeleteScreen extends ImageResizingPanel implements ActionListener
{    
    private static final long serialVersionUID = 1L;
    
    private List<JCheckBox> campaignCheckBoxes = new ArrayList<JCheckBox>();
    private PwcgMainScreen parent = null;

    public CampaignDeleteScreen(PwcgMainScreen parent)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.parent = parent;
    }

    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignDeleteScreen);
            this.setImageFromName(imagePath);

            this.add(BorderLayout.WEST, makeButtonPanel());
            this.add(BorderLayout.CENTER, makeCampaignSelectPanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setOpaque(false);
         
        JButton deleteCampaignButton = PWCGButtonFactory.makeTranslucentMenuButtonGrayMenu("Delete Campaign", "Delete", "Delete the selected campaign", this);
        buttonPanel.add(deleteCampaignButton);
        
        JLabel dummyLabel3 = new JLabel("     ");       
        dummyLabel3.setOpaque(false);
        buttonPanel.add(dummyLabel3);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButtonGrayMenu("Finished", "Cancel", "Finished with campaign deletion", this);
        buttonPanel.add(finishedButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return navPanel;
    }

    private JPanel makeCampaignSelectPanel() throws PWCGException
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        
        ImageResizingPanel campaignSelectPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        campaignSelectPanel.setLayout(new BorderLayout());
        campaignSelectPanel.setOpaque(true);
        
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

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
