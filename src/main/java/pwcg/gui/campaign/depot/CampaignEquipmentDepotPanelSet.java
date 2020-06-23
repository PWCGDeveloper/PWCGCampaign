package pwcg.gui.campaign.depot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
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
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignEquipmentDepotPanelSet extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();
	private Campaign campaign;

	public CampaignEquipmentDepotPanelSet(Campaign campaign)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImageMain("CampaignTable.jpg");
        this.setImage(imagePath);

        this.add(BorderLayout.CENTER,  makeCenterPanel());
        this.add(BorderLayout.WEST, makeNavigatePanel());
	}

	private JPanel makeNavigatePanel() throws PWCGException  
	{		
        JPanel equipmentDepotPanel = new JPanel(new BorderLayout());
        equipmentDepotPanel.setOpaque(false);
		equipmentDepotPanel.setLayout(new BorderLayout());
		equipmentDepotPanel.setOpaque(false);

		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
		
        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Finished", "EquipmentDepoFinished", this);
		buttonPanel.add(acceptButton);
		
		equipmentDepotPanel.add(buttonPanel, BorderLayout.NORTH);
		
		return equipmentDepotPanel;
	}

	private JPanel makeCenterPanel() throws PWCGException 
	{
        ImageResizingPanel equipmentDepotPanel = new ImageResizingPanel("");
        equipmentDepotPanel.setOpaque(false);
        equipmentDepotPanel.setLayout(new BorderLayout());
        String imagePath = UiImageResolver.getImageMain("document.png");
        equipmentDepotPanel.setImage(imagePath);
        equipmentDepotPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,100));
        
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
