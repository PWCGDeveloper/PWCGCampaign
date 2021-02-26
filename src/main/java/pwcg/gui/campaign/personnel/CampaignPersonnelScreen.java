package pwcg.gui.campaign.personnel;

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
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.campaigngenerate.CampaignNewPilotScreen;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.CommonUIActions;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignPersonnelScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private CampaignHomeScreen campaignHome = null;

    private Campaign campaign;

    public CampaignPersonnelScreen(Campaign campaign, CampaignHomeScreen campaignHome)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.campaignHome = campaignHome;
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
        JPanel leftSidePanel = new JPanel(new BorderLayout());
        leftSidePanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JLabel spacer1 = PWCGButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer1);

        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", CommonUIActions.FINISHED, "Finished with configuration changes", this);
        buttonPanel.add(finishedButton);

        JLabel spacer2 = PWCGButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer2);

        JButton coopAdminButton = PWCGButtonFactory.makeTranslucentMenuButton("Administer Pilots", "AdminCoopPilots", "Administer pilots for this campaign", this);
        buttonPanel.add(coopAdminButton);

        JButton referencePilotButton = PWCGButtonFactory.makeTranslucentMenuButton("Reference Pilot", "CampChangeReferencePilot", "Change the reference pilot for the UI", this);
        buttonPanel.add(referencePilotButton);

        if (campaign.isCampaignActive())
        {
            JButton skinManagementButton = PWCGButtonFactory.makeTranslucentMenuButton("Skin Management", "CampSkinManager", "Manage skins for the squadron", this);
            buttonPanel.add(skinManagementButton);
        }

		leftSidePanel.add(buttonPanel, BorderLayout.NORTH);

		return leftSidePanel;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("CampChangeReferencePilot"))
            {
                showChangeReferencePilot();
            }
            else if (action.equalsIgnoreCase("AddHumanPilot"))
            {
                showAddHumanPilot();
            }
            else if (action.equalsIgnoreCase("AdminCoopPilots"))
            {
                showAdminPilots();
            }
            else if (action.equalsIgnoreCase("CampSkinManager"))
            {
                showSkinManager();
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
	
    private void showAddHumanPilot() throws PWCGException
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        CampaignNewPilotScreen addPilotDisplay = new CampaignNewPilotScreen(campaign, campaignHome);
        addPilotDisplay.makePanels();        
        CampaignGuiContextManager.getInstance().pushToContextStack(addPilotDisplay);
    }

    private void showChangeReferencePilot() throws PWCGException
    {
        CampaignReferencePilotSelectorScreen referencePilotSelector = new CampaignReferencePilotSelectorScreen(campaign, campaignHome);
        referencePilotSelector.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(referencePilotSelector);
    }

    private void showAdminPilots() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        CampaignPlayerAdminScreen adminCoopPilotDisplay = new CampaignPlayerAdminScreen(campaign);
        adminCoopPilotDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(adminCoopPilotDisplay);
    }

    private void showSkinManager() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        
        CampaignSkinConfigurationScreen skinDisplay = new CampaignSkinConfigurationScreen(campaign);
        skinDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(skinDisplay);
    }
}

