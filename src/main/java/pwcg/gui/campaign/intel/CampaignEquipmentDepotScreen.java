package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
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
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignEquipmentDepotScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();
	private Campaign campaign;

	public CampaignEquipmentDepotScreen(Campaign campaign)
	{
        super();
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);

        this.campaign = campaign;
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignEquipmentDepotScreen);
        this.setThemedImageFromName(campaign, imagePath);
        
        GridBagConstraints constraints = initializeGridbagConstraints();

        constraints.weightx = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(makeNavigatePanel(), constraints);

        constraints.weightx = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.add(makeCenterPanel(), constraints);
        
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridy = 0;
        this.add(SpacerPanelFactory.makeDocumentSpacerPanel(1400), constraints);
	}

	private JPanel makeNavigatePanel() throws PWCGException  
	{		
        JPanel equipmentDepotPanel = new JPanel(new BorderLayout());
        equipmentDepotPanel.setOpaque(false);
		equipmentDepotPanel.setLayout(new BorderLayout());
		equipmentDepotPanel.setOpaque(false);

		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
		
        JButton finished = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "EquipmentDepoFinished", "Finished reading depo report", this);
		buttonPanel.add(finished);
		
		equipmentDepotPanel.add(buttonPanel, BorderLayout.NORTH);
		
		return equipmentDepotPanel;
	}

	private JPanel makeCenterPanel() throws PWCGException 
	{
        ImageResizingPanel equipmentDepotPanel = new ImageResizingPanel();
        equipmentDepotPanel.setOpaque(false);
        equipmentDepotPanel.setLayout(new BorderLayout());
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        equipmentDepotPanel.setThemedImageFromName(campaign, imagePath);
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

        ImageToDisplaySizer.setDocumentSize(equipmentDepotPanel);
        
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

    private GridBagConstraints initializeGridbagConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipadx = 3;
        constraints.ipady = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        Insets margins = new Insets(0, 50, 50, 0);
        constraints.insets = margins;
        return constraints;
    }
}
