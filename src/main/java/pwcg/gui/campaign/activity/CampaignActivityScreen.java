package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.sound.ProceedWithMission;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.CommonUIActions;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
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

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", CommonUIActions.FINISHED, "Finished with configuration changes", this);
        buttonPanel.add(finishedButton);

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

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

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton journalButton = PWCGButtonFactory.makeTranslucentMenuButton("Journal", "CampFlowJournal", "Update your personal journal", this);
        buttonPanel.add(journalButton);

        JButton equipmentRequestButton = PWCGButtonFactory.makeTranslucentMenuButton("Equipment Request", "EquipmentRequest", "Make a request for specific equipment", this);
        buttonPanel.add(equipmentRequestButton);

        JButton squadronLogButton = PWCGButtonFactory.makeTranslucentMenuButton("Squadron Log", "CampFlowLog", "View campaign logs", this);
        buttonPanel.add(squadronLogButton);

        JButton newsButton = PWCGButtonFactory.makeTranslucentMenuButton("News", "CampFlowNews", "View campaign news", this);
        buttonPanel.add(newsButton);

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
            else if (action.equalsIgnoreCase("CampFlowNews"))
            {
                showCampaignNews();
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
        boolean shouldProceed = ProceedWithMission.shouldProceedWithMission(campaign, "Existing mission results detected.  Transferring will prevent an AAR.  Proceed?");
        if (!shouldProceed)
        {
            return;
        }
        
        SoundManager.getInstance().playSound("Typewriter.WAV");
        CrewMember referencePlayer = campaign.findReferencePlayer();
        
        boolean passTime = true;
        CampaignTransferScreen transferDisplay = new CampaignTransferScreen(campaign, referencePlayer, campaignHome, passTime);

        transferDisplay.makePanels();        
        CampaignGuiContextManager.getInstance().pushToContextStack(transferDisplay);
    }

    private void showLeavePage() throws PWCGException 
    {
        boolean shouldProceed = ProceedWithMission.shouldProceedWithMission(campaign, "Existing mission results detected.  Taking leave will prevent an AAR.  Proceed?");
        if (!shouldProceed)
        {
            return;
        }
        
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

        CrewMember referencePlayer = campaign.findReferencePlayer();
        CampaignSquadronLogScreen logDisplay = new CampaignSquadronLogScreen(referencePlayer.getCompanyId());
        logDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(logDisplay);
    }

    private void showCampaignNews() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        CampaignNewsStandScreen newsDisplay = new CampaignNewsStandScreen(campaign);
        newsDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(newsDisplay);
    }

    private boolean isDisplayTransferButton() throws PWCGException
    {
        if (!campaign.isCampaignActive())
        {
            return false;
        }

        if (!campaign.isCampaignCanOperate())
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

