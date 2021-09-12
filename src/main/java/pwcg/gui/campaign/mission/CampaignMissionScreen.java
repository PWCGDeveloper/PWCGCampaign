package pwcg.gui.campaign.mission;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.activity.CampaignLeaveScreen;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.campaign.home.GuiMissionInitiator;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.HelpDialog;
import pwcg.gui.rofmap.brief.BriefingCoopPersonaChooser;
import pwcg.gui.rofmap.brief.BriefingDescriptionScreen;
import pwcg.gui.rofmap.brief.CampaignHomeGuiBriefingWrapper;
import pwcg.gui.rofmap.debrief.DebriefMissionDescriptionScreen;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.CommonUIActions;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;

public class CampaignMissionScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private CampaignHomeScreen campaignHome = null;

    private Campaign campaign;

    public CampaignMissionScreen(Campaign campaign, CampaignHomeScreen campaignHome)
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

	private Pane makeNavigatePanel() throws PWCGException
	{
        Pane leftSidePanel = new Pane(new BorderLayout());
        leftSidePanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        Label spacer1 = ButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer1);

        Button finishedButton = ButtonFactory.makeTranslucentMenuButton("Finished", CommonUIActions.FINISHED, "Finished with configuration changes", this);
        buttonPanel.add(finishedButton);

        Label spacer2 = ButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer2);

        if (isDisplayMissionButton())
        {
            Button missionButton = ButtonFactory.makeTranslucentMenuButton("Mission", "CampMission", "Generate a mission", this);
            buttonPanel.add(missionButton);

            if (!campaign.isCoop())
            {
                Button loneWolfButton = ButtonFactory.makeTranslucentMenuButton("Lone Wolf Mission", "CampMissionLoneWolf", "Generate a lone wolf mission", this);
                buttonPanel.add(loneWolfButton);
            }
        }
        else if (isDisplayLeaveButton())
        {
            Button missionButton = ButtonFactory.makeTranslucentMenuButton("Heal Wounds", "CampLeave", "Take leave to heal wounds", this);
            buttonPanel.add(missionButton);
        }
        
        if (isDisplayAARButton())
        {
            Button aarButton = ButtonFactory.makeTranslucentMenuButton("Combat Report", "CampFlowCombatReport", "File an After Action Report (AAR) for a mission", this);
            buttonPanel.add(aarButton);
        }


		leftSidePanel.add(buttonPanel, BorderLayout.NORTH);

		return leftSidePanel;
	}

    public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("CampMission"))
            {
                if (campaign.isCoop() && campaign.getCurrentMission() == null)
                {
                    showCoopPersonaChooser();
                }
                else
                {
                    MissionHumanParticipants participatingPlayers = buildParticipatingPlayersSinglePlayer();
                    GuiMissionInitiator missionInitiator = new GuiMissionInitiator(campaign, participatingPlayers);
                    Mission mission = missionInitiator.makeMission(false);
                    showBriefingMap(mission);
                }
            }
            else if (action.equalsIgnoreCase("CampLeave"))
            {
                showLeavePage();
            }
            else if (action.equalsIgnoreCase("CampMissionLoneWolf"))
            {
                MissionHumanParticipants participatingPlayers = buildParticipatingPlayersSinglePlayer();
                GuiMissionInitiator missionInitiator = new GuiMissionInitiator(campaign, participatingPlayers);
                Mission mission = missionInitiator.makeMission(true);
                showBriefingMap(mission);
            }
            else if (action.equalsIgnoreCase("CampFlowCombatReport"))
            {
                showAAR();
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


    private void showLeavePage() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        CampaignLeaveScreen leaveDisplay = new CampaignLeaveScreen(campaignHome);
        leaveDisplay.makePanels();
        
        CampaignGuiContextManager.getInstance().pushToContextStack(leaveDisplay);
    }

    private void showBriefingMap(Mission mission) throws PWCGException 
    {
        MusicManager.playMissionBriefingTheme();
        SoundManager.getInstance().playSound("Typewriter.WAV");

        CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper = new CampaignHomeGuiBriefingWrapper(campaignHome);
        BriefingDescriptionScreen briefingMap = new BriefingDescriptionScreen(campaignHomeGuiBriefingWrapper, mission);
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }
    
    private void showCoopPersonaChooser() throws PWCGException 
    {
        CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper = new CampaignHomeGuiBriefingWrapper(campaignHome);
        BriefingCoopPersonaChooser coopPersonaChooser = new BriefingCoopPersonaChooser(campaign, campaignHomeGuiBriefingWrapper);
        coopPersonaChooser.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(coopPersonaChooser);
    }

    private void showAAR() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        
        try
        {
            AARCoordinator.getInstance().aarPreliminary(campaign);
            
            DebriefMissionDescriptionScreen combatReportDisplay = new DebriefMissionDescriptionScreen(campaign, campaignHome);
            combatReportDisplay.makePanels();
            CampaignGuiContextManager.getInstance().pushToContextStack(combatReportDisplay);
        }
        catch (PWCGException e)
        {
            new  HelpDialog("PWCG Could not perform AAR.  " + e.getMessage());
        }
    }
    
    private MissionHumanParticipants buildParticipatingPlayersSinglePlayer() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(referencePlayer);        
        return participatingPlayers;
    }

    private boolean isDisplayMissionButton() throws PWCGException
    {
        if (!campaign.isCampaignActive())
        {
            return false;
        }

        if (!campaign.isCampaignCanFly())
        {
            return false;
        }

        if (campaign.findReferencePlayer() == null)
        {
            return false;
        }

        if (campaign.findReferencePlayer().getPilotActiveStatus() != SquadronMemberStatus.STATUS_ACTIVE)
        {
            return false;
        }

        return true;
    }

    private boolean isDisplayLeaveButton() throws PWCGException
    {
        if (campaign.findReferencePlayer().getPilotActiveStatus() == SquadronMemberStatus.STATUS_WOUNDED ||
            campaign.findReferencePlayer().getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            return true;
        }
        
        return false;
    }

    private boolean isDisplayAARButton() throws PWCGException
    {
        if (!campaign.isCampaignActive())
        {
            return false;
        }

        return true;
    }
}

