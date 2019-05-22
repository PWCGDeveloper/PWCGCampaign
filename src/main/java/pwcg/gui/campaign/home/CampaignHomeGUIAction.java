package pwcg.gui.campaign.home;

import java.awt.event.ActionEvent;
import java.util.List;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.AARPilotEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.PWCGErrorBundler;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.config.CampaignConfigurationAdvancedGUI;
import pwcg.gui.campaign.config.CampaignConfigurationSimpleGUI;
import pwcg.gui.campaign.depo.CampaignEquipmentDepoPanelSet;
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
import pwcg.gui.rofmap.debrief.DebriefMissionDescriptionPanel;
import pwcg.gui.rofmap.event.AARMainPanel;
import pwcg.gui.rofmap.event.AARMainPanel.EventPanelReason;
import pwcg.gui.rofmap.intelmap.IntelMapGUI;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ReferencePlayerFInder;
import pwcg.gui.utils.UIUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;

public class CampaignHomeGUIAction
{
    private CampaignHomeGUI parent = null;
    private Campaign campaign = null;
    private SquadronMember referencePlayer = null;

    public CampaignHomeGUIAction(CampaignHomeGUI parent, Campaign campaign) 
    {
        super();
        this.parent = parent;
        this.campaign = campaign;
        this.referencePlayer = ReferencePlayerFInder.findReferencePlayer(campaign);
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
                Mission mission = makeMission(false);
                showBriefingMap(mission);
            }
            else if (action.equalsIgnoreCase("CampMissionLoneWolf"))
            {
                Mission mission = makeMission(true);
                showBriefingMap(mission);
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
            else if (action.equalsIgnoreCase("EquipmentDepoReport"))
            {
                showEquipmentDepoReport();
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
            Logger.logException(ue);
            ErrorDialog.userError(ue.getMessage());
        }
        catch (Exception e)
        {
            campaign.setCurrentMission(null);
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
        catch (Throwable t)
        {
            campaign.setCurrentMission(null);
            Logger.logException(t);
            ErrorDialog.internalError(t.getMessage());
        }
    }


    private Mission makeMission(boolean isLoneWolf) throws PWCGException 
    {
        Mission mission = null;

        if (!(campaign.getDate().before(DateUtils.getEndOfWar())))
        {
            throw new PWCGUserException ("The war is over.  Go home.");
        }
        else if (endOfEast())
        {
            throw new PWCGUserException ("Eastern front operations have ended.  Transfer or go home.");
        }
        else
        {
        	MissionHumanParticipants participatingPlayers = buildParticipatingPlayers();        	
            if (campaign.getCurrentMission() == null)
            {
                MissionGenerator missionGenerator = new MissionGenerator(participatingPlayers);
                if (isLoneWolf)
                {
                    mission = missionGenerator.makeLoneWolfMission(participatingPlayers);
                }
                else
                {
                    mission = missionGenerator.makeMission(participatingPlayers);                    
                }
            }
            else
            {
                mission = campaign.getCurrentMission();
            }
        }

        return mission;
    }

    private MissionHumanParticipants buildParticipatingPlayers()
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        if (campaign.getCampaignData().isCoop())
        {
            // TODO COOP Have to make this screen ... with no human participants added mission create will not work
        }
        else
        {
            participatingPlayers.addSquadronMember(this.referencePlayer);
        }
        
        return participatingPlayers;
    }

    private boolean endOfEast() throws PWCGException 
    {
        // We want to know if we are on the Galician map.
        // Get the maps for the airfield.  If the airfield is on the Galician
        // map then it can only be on that map (some Western airfields are
        // on both the France and Channel maps)
    	
    	SquadronMember referencePlayer = PWCGContextManager.getInstance().getReferencePlayer();
        String airfieldName = referencePlayer.determineSquadron().determineCurrentAirfieldName(campaign.getDate());
        List<FrontMapIdentifier> mapsForAirfield =  AirfieldManager.getMapIdForAirfield(airfieldName);
        FrontMapIdentifier mapId = mapsForAirfield.get(0);

        if (mapId == FrontMapIdentifier.GALICIA_MAP)
        {
            if (campaign.getDate().after(DateUtils.getEndOfWWIRussia()))
            {
                return true;
            }
        }
        return false;
    }

    private void showAddHumanPilot() throws PWCGException
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        NewPilotGeneratorUI addPilotDisplay = new NewPilotGeneratorUI(campaign);
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

    private void showTransfer() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        CampaignTransferPanelSet transferDisplay = new CampaignTransferPanelSet(parent);
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

    public void campaignTimePassed(int timePassedDays, AARPilotEvent pilotEvent, EventPanelReason reason) throws PWCGException 
    {
        campaign.setCurrentMission(null);

        if (reason == EventPanelReason.EVENT_PANEL_REASON_LEAVE)
        {
            AARCoordinator.getInstance().submitLeave(campaign, timePassedDays);            
        }
        else
        {
            AARCoordinator.getInstance().submitTransfer(campaign, timePassedDays);
            
        }

        AARMainPanel eventDisplay = new AARMainPanel(campaign, parent, reason, pilotEvent);
        eventDisplay.makePanels();		
        
        CampaignGuiContextManager.getInstance().pushToContextStack(eventDisplay);
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

    private void showCampaignLog() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        CampaignSquadronLogPanelSet logDisplay = new CampaignSquadronLogPanelSet();
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

    private void showEquipmentDepoReport() throws PWCGException 
    {
        CampaignEquipmentDepoPanelSet depo = new CampaignEquipmentDepoPanelSet(campaign);
        depo.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(depo);
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
            CampaignPilotPanelSet pilotPanel = new CampaignPilotPanelSet(squad, pilot);
            pilotPanel.makePanels();
            
            CampaignGuiContextManager.getInstance().pushToContextStack(pilotPanel);
        }
    }
}
