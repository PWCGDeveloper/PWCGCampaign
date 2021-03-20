package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.CommonUIActions;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignActivityScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private CampaignHomeScreen campaignHome = null;

    private Campaign campaign;

    public CampaignActivityScreen(Campaign campaign, CampaignHomeScreen campaignHome)
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

        if (campaign.isCampaignActive())
        {
            JButton leaveButton = PWCGButtonFactory.makeTranslucentMenuButton("Leave", "CampFlowLeave", "Request leave", this);
            buttonPanel.add(leaveButton);
        }

        if (isDisplayTransferButton())
        {
            JButton transferButton = PWCGButtonFactory.makeTranslucentMenuButton("Transfer", "CampFlowTransfer", "Transfer to a new squadron", this);
            buttonPanel.add(transferButton);
        }

        JLabel space4 = new JLabel("");
        buttonPanel.add(space4);

        JButton journalButton = PWCGButtonFactory.makeTranslucentMenuButton("Journal", "CampFlowJournal", "Update your personal journal", this);
        buttonPanel.add(journalButton);

        JButton equipmentRequestButton = PWCGButtonFactory.makeTranslucentMenuButton("Equipment Request", "EquipmentRequest", "Make a request for specific equipment", this);
        buttonPanel.add(equipmentRequestButton);

        JButton squadronLogButton = PWCGButtonFactory.makeTranslucentMenuButton("Squadron Log", "CampFlowLog", "View campaign logs", this);
        buttonPanel.add(squadronLogButton);

		leftSidePanel.add(buttonPanel, BorderLayout.NORTH);

		return leftSidePanel;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("CampFlowTransfer"))
            {
                showTransfer();
            }
            else if (action.equalsIgnoreCase("CampFlowLeave"))
            {
                showLeavePage();
            }
            else if (action.equalsIgnoreCase("CampFlowJournal"))
            {
                showJournal();
            }
            else if (action.equalsIgnoreCase("EquipmentRequest"))
            {
                showEquipmentRequest();
            }
            else if (action.equalsIgnoreCase("CampFlowLog"))
            {
                showCampaignLog();
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

    private void showTransfer() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        
        boolean passTime = true;
        CampaignTransferScreen transferDisplay = new CampaignTransferScreen(campaign, referencePlayer, campaignHome, passTime);

        transferDisplay.makePanels();        
        CampaignGuiContextManager.getInstance().pushToContextStack(transferDisplay);
    }

    private void showLeavePage() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        CampaignLeaveScreen leaveDisplay = new CampaignLeaveScreen(campaignHome);
        leaveDisplay.makePanels();
        
        CampaignGuiContextManager.getInstance().pushToContextStack(leaveDisplay);
    }

    private void showJournal() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        CampaignJournalScreen journalDisplay = new CampaignJournalScreen(campaign);
        journalDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(journalDisplay);
    }

    private void showEquipmentRequest() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        EquipmentRequestScreen equipmentRequestScreen = new EquipmentRequestScreen(campaign);
        equipmentRequestScreen.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(equipmentRequestScreen);
    }

    private void showCampaignLog() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        SquadronMember referencePlayer = campaign.findReferencePlayer();
        CampaignSquadronLogScreen logDisplay = new CampaignSquadronLogScreen(referencePlayer.getSquadronId());
        logDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(logDisplay);
    }

    private boolean isDisplayTransferButton() throws PWCGException
    {
        if (!campaign.isCampaignActive())
        {
            return false;
        }

        if (!campaign.isCampaignCanFly())
        {
            return false;
        }

        if (campaign.isCoop())
        {
            return false;
        }

        return true;
    }
}

