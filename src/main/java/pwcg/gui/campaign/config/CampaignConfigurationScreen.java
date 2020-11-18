package pwcg.gui.campaign.config;

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
import pwcg.gui.utils.CommonUIActions;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignConfigurationScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;

    public CampaignConfigurationScreen(Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }

	public void makePanels() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignSimpleConfigurationScreen);
        this.setImageFromName(imagePath);

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

        JButton simpleConfigButton = PWCGButtonFactory.makeTranslucentMenuButton("Simple Config", "CampSimpleConfig", "Set simple configuration for this campaign", this);
        buttonPanel.add(simpleConfigButton);
        
        JButton advancedConfigButton = PWCGButtonFactory.makeTranslucentMenuButton("Advanced Config", "CampAdvancedConfig", "Set advanced configuration for this campaign", this);
        buttonPanel.add(advancedConfigButton);
		
		simpleConfigAcceptPanel.add(buttonPanel, BorderLayout.NORTH);

		return simpleConfigAcceptPanel;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String action = ae.getActionCommand();
            if (action.equals("CampSimpleConfig"))
            {
                showSimpleConfig();
            }
            else if (action.equals("CampAdvancedConfig"))
            {
                showAdvancedConfig();
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

    private void showSimpleConfig() throws PWCGException 
    {
        CampaignSimpleConfigurationScreen simpleConfigGUI = new CampaignSimpleConfigurationScreen(campaign);
        simpleConfigGUI.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(simpleConfigGUI);
    }

    private void showAdvancedConfig() throws PWCGException 
    {
        CampaignAdvancedConfigurationScreen simpleConfigGUI = new CampaignAdvancedConfigurationScreen(campaign);
        simpleConfigGUI.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(simpleConfigGUI);
    }
}

