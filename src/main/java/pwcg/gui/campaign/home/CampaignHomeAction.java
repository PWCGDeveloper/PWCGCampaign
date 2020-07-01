package pwcg.gui.campaign.home;

import java.awt.event.ActionEvent;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGErrorBundler;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.config.CampaignAdvancedConfigurationScreen;
import pwcg.gui.campaign.config.CampaignSimpleConfigurationScreen;
import pwcg.gui.campaign.coop.CampaignCoopAdminScreen;
import pwcg.gui.campaign.depot.CampaignEquipmentDepotScreen;
import pwcg.gui.campaign.intel.CampaignIntelligenceReportScreen;
import pwcg.gui.campaign.journal.CampaignJournalScreen;
import pwcg.gui.campaign.journal.CampaignSquadronLogScreen;
import pwcg.gui.campaign.pilot.CampaignPilotScreen;
import pwcg.gui.campaign.skins.CampaignSkinConfigurationScreen;
import pwcg.gui.campaign.transfer.CampaignLeaveScreen;
import pwcg.gui.campaign.transfer.CampaignTransferScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.HelpDialog;
import pwcg.gui.maingui.campaigngenerate.CampaignNewPilotScreen;
import pwcg.gui.rofmap.brief.BriefingDescriptionScreen;
import pwcg.gui.rofmap.brief.BriefingCoopPersonaChooser;
import pwcg.gui.rofmap.debrief.DebriefMissionDescriptionScreen;
import pwcg.gui.rofmap.intelmap.IntelMapGUI;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.UIUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;

public class CampaignHomeAction
{
    private CampaignHomeScreen parent = null;
    private Campaign campaign = null;

    public CampaignHomeAction(CampaignHomeScreen parent, Campaign campaign) 
    {
        super();
        this.parent = parent;
        this.campaign = campaign;
    }
    

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("CampError"))
            {
                PWCGErrorBundler errorBundler = new PWCGErrorBundler();
                errorBundler.bundleDebuggingData();
                ErrorDialog.internalError("Error during AAR process - please post " + errorBundler.getTargetErrorFileName() + ".zip");
            }
            else if (action.equalsIgnoreCase("CampMission"))
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
            else if (action.equalsIgnoreCase("CampMissionLoneWolf"))
            {
                MissionHumanParticipants participatingPlayers = buildParticipatingPlayersSinglePlayer();
             	GuiMissionInitiator missionInitiator = new GuiMissionInitiator(campaign, participatingPlayers);
                Mission mission = missionInitiator.makeMission(true);
                showBriefingMap(mission);
            }
            else if (action.equalsIgnoreCase("CampChangeReferencePilot"))
            {
                showChangeReferencePilot();
            }
            else if (action.equalsIgnoreCase("CampFlowTransfer"))
            {
                showTransfer();
            }
            else if (action.equalsIgnoreCase("CampFlowCombatReport"))
            {
                showAAR();
            }
            else if (action.equalsIgnoreCase("CampFlowLeave"))
            {
                showLeavePage();
            }
            else if (action.equalsIgnoreCase("CampFlowJournal"))
            {
                showJournal();
            }
            else if (action.equalsIgnoreCase("AdminCoopPilots"))
            {
                showAdminCoopPilots();
            }
            else if (action.equalsIgnoreCase("CampFlowLog"))
            {
                showCampaignLog();
            }
            else if (action.equalsIgnoreCase("CampSkinManager"))
            {
                showSkinManager();
            }
            else if (action.equalsIgnoreCase("CampFlowIntelligence"))
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
            else if (action.startsWith("CampFlowPilot"))
            {
                showPilot(action);
            }
            else if (action.equalsIgnoreCase("AddHumanPilot"))
            {
                showAddHumanPilot();
            }
            else if (action.equals("CampSimpleConfig"))
            {
                showSimpleConfig();
            }
            else if (action.equals("CampAdvancedConfig"))
            {
                showAdvancedConfig();
            }
        }
        catch (PWCGUserException ue)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(ue);
            ErrorDialog.userError(ue.getMessage());
        }
        catch (Exception e)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
        catch (Throwable t)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(t);
            ErrorDialog.internalError(t.getMessage());
        }
    }

    private void showAddHumanPilot() throws PWCGException
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        CampaignNewPilotScreen addPilotDisplay = new CampaignNewPilotScreen(campaign, parent, null);
        addPilotDisplay.makePanels();        
        CampaignGuiContextManager.getInstance().pushToContextStack(addPilotDisplay);
    }

    private void showBriefingMap(Mission mission) throws PWCGException 
    {
    	MusicManager.playMissionBriefingTheme();
    	SoundManager.getInstance().playSound("Typewriter.WAV");

        BriefingDescriptionScreen briefingMap = new BriefingDescriptionScreen(parent, mission);
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }

    private void showCoopPersonaChooser() throws PWCGException 
    {
    	BriefingCoopPersonaChooser coopPersonaChooser = new BriefingCoopPersonaChooser(campaign, parent);
    	coopPersonaChooser.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(coopPersonaChooser);
    }

    private void showChangeReferencePilot() throws PWCGException
    {
        CampaignReferencePilotSelectorScreen referencePilotSelector = new CampaignReferencePilotSelectorScreen(campaign, parent);
        referencePilotSelector.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(referencePilotSelector);
    }

    private void showTransfer() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        CampaignTransferScreen transferDisplay = new CampaignTransferScreen(parent, null, referencePlayer);
        transferDisplay.makePanels();        
        CampaignGuiContextManager.getInstance().pushToContextStack(transferDisplay);
    }

    private void showLeavePage() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        CampaignLeaveScreen leaveDisplay = new CampaignLeaveScreen(parent);
        leaveDisplay.makePanels();
        
        CampaignGuiContextManager.getInstance().pushToContextStack(leaveDisplay);
    }

    private void showAAR() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        
        try
        {
            AARCoordinator.getInstance().aarPreliminary(campaign);
            
            DebriefMissionDescriptionScreen combatReportDisplay = new DebriefMissionDescriptionScreen(campaign, parent);
            combatReportDisplay.makePanels();
            CampaignGuiContextManager.getInstance().pushToContextStack(combatReportDisplay);
        }
        catch (PWCGException e)
        {
            new  HelpDialog("PWCG was unable to find a log set for your last mission " + campaign.getCampaignData().getName());
        }
    }

    private void showJournal() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        CampaignJournalScreen journalDisplay = new CampaignJournalScreen(campaign);
        journalDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(journalDisplay);
    }

    private void showAdminCoopPilots() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        CampaignCoopAdminScreen adminCoopPilotDisplay = new CampaignCoopAdminScreen(parent, campaign);
        adminCoopPilotDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(adminCoopPilotDisplay);
    }

    private void showCampaignLog() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        SquadronMember referencePlayer = campaign.findReferencePlayer();
        CampaignSquadronLogScreen logDisplay = new CampaignSquadronLogScreen(referencePlayer.getSquadronId());
        logDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(logDisplay);
    }

    private void showSkinManager() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        
        CampaignSkinConfigurationScreen skinDisplay = new CampaignSkinConfigurationScreen(campaign);
        skinDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(skinDisplay);
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

    private void showPilot(String action) throws PWCGException 
    {
        SquadronMember pilot = UIUtils.getPilotFromAction(campaign, action);
        if (pilot != null)
        {
            Squadron squad = pilot.determineSquadron();
            if (pilot instanceof Ace)
            {
                Ace ace = (Ace)pilot;
                squad =  ace.determineSquadron();;
            }
            CampaignPilotScreen pilotPanel = new CampaignPilotScreen(campaign, squad, pilot, parent);
            pilotPanel.makePanels();
            
            CampaignGuiContextManager.getInstance().pushToContextStack(pilotPanel);
        }
    }
    
    private MissionHumanParticipants buildParticipatingPlayersSinglePlayer() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
	    SquadronMember referencePlayer = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(referencePlayer);        
        return participatingPlayers;
    }

}
