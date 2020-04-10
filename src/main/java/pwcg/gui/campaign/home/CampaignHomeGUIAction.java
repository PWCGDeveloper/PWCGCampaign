package pwcg.gui.campaign.home;

import java.awt.event.ActionEvent;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGErrorBundler;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.config.CampaignConfigurationAdvancedGUI;
import pwcg.gui.campaign.config.CampaignConfigurationSimpleGUI;
import pwcg.gui.campaign.coop.CampaignAdminCoopPilotPanelSet;
import pwcg.gui.campaign.depot.CampaignEquipmentDepotPanelSet;
import pwcg.gui.campaign.intel.CampaignIntelligencePanelSet;
import pwcg.gui.campaign.journal.CampaignJournalPanelSet;
import pwcg.gui.campaign.journal.CampaignSquadronLogPanelSet;
import pwcg.gui.campaign.pilot.CampaignPilotPanelSet;
import pwcg.gui.campaign.skins.CampaignSkinManagerPanel;
import pwcg.gui.campaign.transfer.CampaignLeavePanelSet;
import pwcg.gui.campaign.transfer.CampaignTransferPanelSet;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.HelpDialog;
import pwcg.gui.maingui.campaigngenerate.NewPilotGeneratorUI;
import pwcg.gui.rofmap.brief.BriefingDescriptionPanelSet;
import pwcg.gui.rofmap.brief.CoopPersonaChooser;
import pwcg.gui.rofmap.debrief.DebriefMissionDescriptionPanel;
import pwcg.gui.rofmap.intelmap.IntelMapGUI;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ReferencePlayerFinder;
import pwcg.gui.utils.UIUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;

public class CampaignHomeGUIAction
{
    private CampaignHomeGUI parent = null;
    private Campaign campaign = null;

    public CampaignHomeGUIAction(CampaignHomeGUI parent, Campaign campaign) 
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
        NewPilotGeneratorUI addPilotDisplay = new NewPilotGeneratorUI(campaign, parent, null);
        addPilotDisplay.makePanels();        
        CampaignGuiContextManager.getInstance().pushToContextStack(addPilotDisplay);
    }

    private void showBriefingMap(Mission mission) throws PWCGException 
    {
    	MusicManager.playMissionBriefingTheme();
    	SoundManager.getInstance().playSound("Typewriter.WAV");

        BriefingDescriptionPanelSet briefingMap = new BriefingDescriptionPanelSet(parent, mission);
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }

    private void showCoopPersonaChooser() throws PWCGException 
    {
    	CoopPersonaChooser coopPersonaChooser = new CoopPersonaChooser(campaign, parent);
    	coopPersonaChooser.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(coopPersonaChooser);
    }

    private void showChangeReferencePilot() throws PWCGException
    {
        ReferencePilotSelector referencePilotSelector = new ReferencePilotSelector(campaign, parent);
        referencePilotSelector.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(referencePilotSelector);
    }

    private void showTransfer() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        SquadronMember referencePlayer = PWCGContext.getInstance().getReferencePlayer();
        CampaignTransferPanelSet transferDisplay = new CampaignTransferPanelSet(parent, null, referencePlayer);
        transferDisplay.makePanels();        
        CampaignGuiContextManager.getInstance().pushToContextStack(transferDisplay);
    }

    private void showLeavePage() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        CampaignLeavePanelSet leaveDisplay = new CampaignLeavePanelSet(parent);
        leaveDisplay.makePanels();
        
        CampaignGuiContextManager.getInstance().pushToContextStack(leaveDisplay);
    }

    private void showAAR() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        
        try
        {
            AARCoordinator.getInstance().aarPreliminary(campaign);
            
            DebriefMissionDescriptionPanel combatReportDisplay = new DebriefMissionDescriptionPanel(campaign, parent);
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

        CampaignJournalPanelSet journalDisplay = new CampaignJournalPanelSet();
        journalDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(journalDisplay);
    }

    private void showAdminCoopPilots() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        CampaignAdminCoopPilotPanelSet adminCoopPilotDisplay = new CampaignAdminCoopPilotPanelSet(campaign);
        adminCoopPilotDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(adminCoopPilotDisplay);
    }

    private void showCampaignLog() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        CampaignSquadronLogPanelSet logDisplay = new CampaignSquadronLogPanelSet(PWCGContext.getInstance().getReferencePlayer().getSquadronId());
        logDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(logDisplay);
    }

    private void showSkinManager() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        
        CampaignSkinManagerPanel skinDisplay = new CampaignSkinManagerPanel();
        skinDisplay.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(skinDisplay);
    }

    private void showIntelReport() throws PWCGException 
    {
        CampaignIntelligencePanelSet intelligence = new CampaignIntelligencePanelSet();
        intelligence.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(intelligence);
    }

    private void showEquipmentDepotReport() throws PWCGException 
    {
        CampaignEquipmentDepotPanelSet depot = new CampaignEquipmentDepotPanelSet(campaign);
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
        CampaignConfigurationSimpleGUI simpleConfigGUI = new CampaignConfigurationSimpleGUI(campaign);
        simpleConfigGUI.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(simpleConfigGUI);
    }

    private void showAdvancedConfig() throws PWCGException 
    {
        CampaignConfigurationAdvancedGUI simpleConfigGUI = new CampaignConfigurationAdvancedGUI(campaign);
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
            CampaignPilotPanelSet pilotPanel = new CampaignPilotPanelSet(squad, pilot, parent);
            pilotPanel.makePanels();
            
            CampaignGuiContextManager.getInstance().pushToContextStack(pilotPanel);
        }
    }
    
    private MissionHumanParticipants buildParticipatingPlayersSinglePlayer() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
	    SquadronMember referencePlayer = ReferencePlayerFinder.findReferencePlayer(campaign);
        participatingPlayers.addSquadronMember(referencePlayer);        
        return participatingPlayers;
    }

}
