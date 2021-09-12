package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javax.swing.JTabbedPane;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignEquipmentDepotScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();
	private Campaign campaign;

	public CampaignEquipmentDepotScreen(Campaign campaign)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignEquipmentDepotScreen);
        this.setImageFromName(imagePath);

        this.add(BorderLayout.WEST, makeNavigatePanel());
        this.add(BorderLayout.CENTER,  makeCenterPanel());
        this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1400));
	}

	private Pane makeNavigatePanel() throws PWCGException  
	{		
        Pane equipmentDepotPanel = new Pane(new BorderLayout());
        equipmentDepotPanel.setOpaque(false);
		equipmentDepotPanel.setLayout(new BorderLayout());
		equipmentDepotPanel.setOpaque(false);

		Pane buttonPanel = new Pane(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
		
        Button finished = ButtonFactory.makeTranslucentMenuButton("Finished", "EquipmentDepoFinished", "Finished reading depo report", this);
		buttonPanel.add(finished);
		
		equipmentDepotPanel.add(buttonPanel, BorderLayout.NORTH);
		
		return equipmentDepotPanel;
	}

	private Pane makeCenterPanel() throws PWCGException 
	{
        ImageResizingPanel equipmentDepotPanel = new ImageResizingPanel("");
        equipmentDepotPanel.setOpaque(false);
        equipmentDepotPanel.setLayout(new BorderLayout());
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        equipmentDepotPanel.setImageFromName(imagePath);
        equipmentDepotPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());
        
        Color tabBG = ColorMap.PAPER_BACKGROUND;
        tabs.setBackground(tabBG);
        tabs.setOpaque(false);
        
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> allArmedServices = serviceManager.getAllActiveArmedServices(campaign.getDate());
        for (ArmedService service : allArmedServices)
        {
            CampaignEquipmentDepotPanel serviceEquipmentDepoTab = new CampaignEquipmentDepotPanel(campaign, service);
            serviceEquipmentDepoTab.makePanel();
            tabs.addTab(service.getName(), serviceEquipmentDepoTab);      
        }
                    
        for (int i = 0; i < tabs.getTabCount(); ++i)
        {
            tabs.setBackgroundAt(i, tabBG);
        }

        equipmentDepotPanel.add(tabs, BorderLayout.CENTER);
		
		return equipmentDepotPanel;
	}

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();

            if (action.equalsIgnoreCase("EquipmentDepoFinished"))
            {
                campaign.write();                
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

}
