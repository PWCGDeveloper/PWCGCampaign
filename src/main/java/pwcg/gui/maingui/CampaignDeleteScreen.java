package pwcg.gui.maingui;

import java.awt.BorderLayout;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javax.swing.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

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
import pwcg.gui.utils.ButtonFactory;

public class CampaignDeleteScreen extends ImageResizingPanel implements ActionListener
{    
    private static final long serialVersionUID = 1L;
    
    private List<CheckBox> campaignCheckBoxes = new ArrayList<CheckBox>();
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

    private Pane makeButtonPanel() throws PWCGException
    {
        Pane navPanel = new Pane(new BorderLayout());
        navPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(6,1));
        buttonPanel.setOpaque(false);
         
        Button deleteCampaignButton = ButtonFactory.makeTranslucentMenuButtonGrayMenu("Delete Campaign", "Delete", "Delete the selected campaign", this);
        buttonPanel.add(deleteCampaignButton);
        
        Label dummyLabel3 = new Label("     ");       
        dummyLabel3.setOpaque(false);
        buttonPanel.add(dummyLabel3);
        
        Button finishedButton = ButtonFactory.makeTranslucentMenuButtonGrayMenu("Finished", "Cancel", "Finished with campaign deletion", this);
        buttonPanel.add(finishedButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return navPanel;
    }

    private Pane makeCampaignSelectPanel() throws PWCGException
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        
        ImageResizingPanel campaignSelectPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        campaignSelectPanel.setLayout(new BorderLayout());
        campaignSelectPanel.setOpaque(true);
        
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        Pane campaignSelectGrid = new Pane(new GridLayout(0,1));
        campaignSelectGrid.setOpaque(false);
        
        List<String> campaigns = Campaign.getCampaignNames();
        for (String campaignName : campaigns)
        {
            CheckBox campaignCheckBox = new CheckBox();
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
        for (CheckBox campaignSelectedCheckBox : campaignCheckBoxes)
        {
            if (campaignSelectedCheckBox.isSelected())
            {
                CampaignRemover.deleteCampaign(campaignSelectedCheckBox.getText());         
            }
        }
    }

 }
