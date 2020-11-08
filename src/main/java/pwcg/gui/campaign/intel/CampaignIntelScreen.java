package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.intelmap.IntelMapGUI;
import pwcg.gui.utils.CommonUIActions;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignIntelScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;

    public CampaignIntelScreen(Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }

	public void makePanels() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignSimpleConfigurationScreen);
        this.setImage(imagePath);

        this.add(BorderLayout.WEST, makeNavigatePanel());
        this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1400));
	}

	private JPanel makeNavigatePanel() throws PWCGException
	{
        JPanel simpleConfigAcceptPanel = new JPanel(new BorderLayout());
        simpleConfigAcceptPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JLabel spacer1 = PWCGButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer1);

        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", CommonUIActions.FINISHED, "Finished with configuration changes", this);
        buttonPanel.add(finishedButton);

        JLabel spacer2 = PWCGButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer2);

        JButton intellMapButton = PWCGButtonFactory.makeTranslucentMenuButton("Intel Map", "CampIntelMap", "View intelligence maps", this);
        buttonPanel.add(intellMapButton);

        JButton intelligenceButton = PWCGButtonFactory.makeTranslucentMenuButton("Intelligence Report", "CampFlowIntelligence", "View intelligence reports", this);
        buttonPanel.add(intelligenceButton);

        JButton equipmentDepotButton = PWCGButtonFactory.makeTranslucentMenuButton("Depot Report", "EquipmentDepotReport", "View equipment depot report", this);
        buttonPanel.add(equipmentDepotButton);

		simpleConfigAcceptPanel.add(buttonPanel, BorderLayout.NORTH);

		return simpleConfigAcceptPanel;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("CampFlowIntelligence"))
            {
                showIntelReport();
            }
            else if (action.equalsIgnoreCase("EquipmentDepotReport"))
            {
                showEquipmentDepotReport();
            }
            else if (action.equalsIgnoreCase("CampIntelMap"))
            {
                showIntelMap();
            }
            else if (action.equals(CommonUIActions.FINISHED))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void showIntelReport() throws PWCGException 
    {
        CampaignIntelligenceReportScreen intelligence = new CampaignIntelligenceReportScreen(campaign);
        intelligence.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(intelligence);
    }

    private void showEquipmentDepotReport() throws PWCGException 
    {
        CampaignEquipmentDepotScreen depot = new CampaignEquipmentDepotScreen(campaign);
        depot.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(depot);
    }
    
    private void showIntelMap() throws PWCGException 
    {
        IntelMapGUI map = new IntelMapGUI(campaign.getDate());
        map.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(map);
    }
}

