package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.AARPilotEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.GreatAce;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.PWCGErrorBundler;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.CampaignRosterBasePanelFactory;
import pwcg.gui.campaign.CampaignRosterSquadronPanelFactory;
import pwcg.gui.campaign.CampaignRosterTopAcesPanelFactory;
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
import pwcg.gui.maingui.CampaignMainGUI;
import pwcg.gui.rofmap.brief.BriefingDescriptionPanelSet;
import pwcg.gui.rofmap.debrief.DebriefMissionDescriptionPanel;
import pwcg.gui.rofmap.event.AARMainPanel;
import pwcg.gui.rofmap.event.AARMainPanel.EventPanelReason;
import pwcg.gui.rofmap.intelmap.IntelMapGUI;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;
import pwcg.gui.utils.UIUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;

public class CampaignHomeGUI extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private CampaignMainGUI parent = null;
    private Campaign campaign = null;
    private JButton loneWolfMission = null;
    private List<JButton> activeButtons = new ArrayList<JButton>();
    private boolean needContextRefresh = false;

    public CampaignHomeGUI(CampaignMainGUI parent, Campaign campaign) 
    {
        super();
        this.parent = parent;
        this.campaign = campaign;
        this.makeGUI();
    }

    public void makeGUI() 
    {
        try
        {
            createPilotContext();
        }
        catch (PWCGException e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
    
    @Override
    public void refreshCenterPanel() throws PWCGException
    {
        if (needContextRefresh)
        {
            needContextRefresh = false;
            createPilotContext();
        }
    }

    private JPanel makeLeftPanel() throws PWCGException 
    {
        String imagePath = getSideImage("CampaignLeft.jpg");

        ImageResizingPanel campaignButtonPanel = new ImageResizingPanel(imagePath);
        campaignButtonPanel.setLayout(new BorderLayout());
        campaignButtonPanel.setOpaque(true);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        makePlainButtons(buttonPanel);

        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);

        return campaignButtonPanel;
    }

    private void makePlainButtons(JPanel buttonPanel) throws PWCGException
    {
        JLabel spacer = new JLabel("");
        buttonPanel.add(spacer);

        activeButtons.clear();
        
        if (isDisplayMissionButton())
        {
            JButton createButton = makeMenuButton("Mission", "CampMission", "Generate a mission");
            addButton(buttonPanel, createButton);

            if (!campaign.getCampaignData().isCoop())
            {
                loneWolfMission = makeMenuButton("Lone Wolf Mission", "CampMissionLoneWolf", "Generate a lone wolf mission");
                addButton(buttonPanel, loneWolfMission);
            }

            JButton combatReportButton = makeMenuButton("Combat Report", "CampFlowCombatReport", "File an After Action Report (AAR) for a mission");
            addButton(buttonPanel, combatReportButton);
        }
        
        JLabel space1 = new JLabel("");
        buttonPanel.add(space1);

        JButton pilotsButton = makeMenuButton("Pilots", "CampPilots", "Show squadron pilot chalk board");
        addButton(buttonPanel, pilotsButton);

        JButton topAcesButton = makeMenuButton("Top Aces", "CampTopAces", "Show top aces chalk board");
        addButton(buttonPanel, topAcesButton);

        JButton equipmentButton = makeMenuButton("Equipment", "Equipment", "Show equipment chalk board");
        addButton(buttonPanel, equipmentButton);

        if (isAddHumanPilot())
        {
            JButton addHumanPilotButton = makeMenuButton("Add Pilot", "AddHumanPilot", "Add a human pilot");
            addButton(buttonPanel, addHumanPilotButton);
        }
        
        JLabel space2 = new JLabel("");
        buttonPanel.add(space2);

        if (campaign.isCampaignActive())
        {
            JButton leaveButton = makeMenuButton("Leave", "CampFlowLeave", "Request leave");
            addButton(buttonPanel, leaveButton);
        }
        
        if (isDisplayTransferButton())
        {
            JButton transferButton = makeMenuButton("Transfer", "CampFlowTransfer", "Transfer to a new squadron");
            addButton(buttonPanel, transferButton);
        }

        JButton recordsButton = makeMenuButton("Journal", "CampFlowJournal", "Update your personal journal");
        addButton(buttonPanel, recordsButton);

        JButton squadronLogButton = makeMenuButton("Squadron Log", "CampFlowLog", "View campaign logs");
        addButton(buttonPanel, squadronLogButton);

        if (campaign.isCampaignActive())
        {
            JButton skinManagementButton = makeMenuButton("Skin Management", "CampSkinManager", "Manage skins for the squadron");
            addButton(buttonPanel, skinManagementButton);
    
            JButton intellMapButton = makeMenuButton("Intel Map", "CampIntelMap", "View intelligence maps");
            addButton(buttonPanel, intellMapButton);
    
            JButton intelligenceButton = makeMenuButton("Intelligence Report", "CampFlowIntelligence", "View intelligence reports");
            addButton(buttonPanel, intelligenceButton);
    
            JButton equipmentDepoButton = makeMenuButton("Equipment Depo Report", "EquipmentDepoReport", "View equipment depo report");
            addButton(buttonPanel, equipmentDepoButton);
    
            JLabel space3 = new JLabel("");
            buttonPanel.add(space3);
    
            JButton simpleConfigButton = makeMenuButton("Simple Config", "CampSimpleConfig", "Set simple configuration for this campaign");
            addButton(buttonPanel, simpleConfigButton);
    
            JButton advancedConfigButton = makeMenuButton("Advanced Config", "CampAdvancedConfig", "Set advanced configuration for this campaign");
            addButton(buttonPanel, advancedConfigButton);
        }
        
        JLabel space4 = new JLabel("");
        buttonPanel.add(space4);

        JButton mainButton = makeMenuButton("Leave Campaign", "CampMainMenu", "Return to PWCG main menu");
        addButton(buttonPanel, mainButton);

        JLabel space5 = new JLabel("");
        buttonPanel.add(space5);

        JButton errorButton = makeMenuButton("Report Error", "CampError", "Bundle up data files for error reporting");
        addButton(buttonPanel, errorButton);
    }

    private boolean isAddHumanPilot() throws PWCGException
    {
        if (!campaign.isCampaignActive())
        {
            return true;
        }

        if (campaign.getCampaignData().isCoop())
        {
            return true;
        }

        return false;
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
        
        return true;
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
        
        if (campaign.getCampaignData().isCoop())
        {
            return false;
        }
        
        return true;
    }
    
    private void addButton(JPanel buttonPanel, JButton button) 
    {
        buttonPanel.add(button);
        activeButtons.add(button);
    }

    private JButton makeMenuButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, commandText, this);
         
        ToolTipManager.setToolTip(button, toolTiptext);

        return button;
    }

    public void createPilotContext() throws PWCGException 
    {
		MusicManager.playCampaignTheme(campaign);

        CampaignRosterBasePanelFactory pilotListDisplay = new CampaignRosterSquadronPanelFactory(this);
        pilotListDisplay.makePilotList();
        createSquadronMemberContext(pilotListDisplay);
    }

    private void createTopAceContext() throws PWCGException 
    {
		CampaignRosterBasePanelFactory topAceListDisplay = new CampaignRosterTopAcesPanelFactory(this);
        topAceListDisplay.makePilotList();
        createSquadronMemberContext(topAceListDisplay);
    }

    private void createEquipmentContext() throws PWCGException 
    {
        setLeftPanel(makeLeftPanel());
        
        CampaignEquipmentChalkBoard equipmentDisplay = new CampaignEquipmentChalkBoard();
        equipmentDisplay.makeEquipmentPanel(campaign);
        setCenterPanel(equipmentDisplay);

        CampaignRosterBasePanelFactory pilotListDisplay = new CampaignRosterSquadronPanelFactory(this);
        pilotListDisplay.makePilotList();
        pilotListDisplay.makeCampaignHomePanels();
        setRightPanel(pilotListDisplay.getPilotListPanel());

        CampaignGuiContextManager.getInstance().clearContextStack();
        CampaignGuiContextManager.getInstance().pushToContextStack(this);
    }    

    private void createSquadronMemberContext(CampaignRosterBasePanelFactory squadronMemberListDisplay) throws PWCGException 
    {
        squadronMemberListDisplay.makeCampaignHomePanels();
        setLeftPanel(makeLeftPanel());
        setCenterPanel(squadronMemberListDisplay.getChalkboardPanel());
        setRightPanel(squadronMemberListDisplay.getPilotListPanel());

        CampaignGuiContextManager.getInstance().clearContextStack();
        CampaignGuiContextManager.getInstance().pushToContextStack(this);

        enableButtonsAsNeeded();
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
            if (campaign.getCurrentMission() == null)
            {
                MissionGenerator missionGenerator = new MissionGenerator();

                if (isLoneWolf)
                {
                    mission = missionGenerator.makeLoneWolfMission();
                }
                else
                {
                    mission = missionGenerator.makeMission();                    
                }
            }
            else
            {
                mission = campaign.getCurrentMission();
            }
        }

        return mission;
    }

    private boolean endOfEast() throws PWCGException 
    {
        // We want to know if we are on the Galician map.
        // Get the maps for the airfield.  If the airfield is on the Galician
        // map then it can only be on that map (some Western airfields are
        // on both the France and Channel maps)
        String airfieldName = campaign.getAirfieldName();
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

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("CampMainMenu"))
            {
                campaign.write();
                parent.refresh();

                return;
            }
            else if (action.equalsIgnoreCase("CampError"))
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
            else if (action.equalsIgnoreCase("CampPilots"))
            {
                createPilotContext();
            }
            else if (action.equalsIgnoreCase("AddHumanPilot"))
            {
                showAddHumanPilot();
            }
            else if (action.equalsIgnoreCase("CampTopAces"))
            {
                createTopAceContext();
            }
            else if (action.equalsIgnoreCase("Equipment"))
            {
                createEquipmentContext();
            }
            else if (action.startsWith("CampFlowPilot"))
            {
                showPilot(action);
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

    private void showAddHumanPilot() throws PWCGException
    {
        needContextRefresh = true;

        SoundManager.getInstance().playSound("Typewriter.WAV");
        CampaignAddHumanPilotPanelSet addPilotDisplay = new CampaignAddHumanPilotPanelSet();
        addPilotDisplay.makePanels();        
        CampaignGuiContextManager.getInstance().pushToContextStack(addPilotDisplay);
    }

    private void showBriefingMap(Mission mission) throws PWCGException 
    {
    	MusicManager.playMissionBriefingTheme();
    	SoundManager.getInstance().playSound("Typewriter.WAV");

        BriefingDescriptionPanelSet briefingMap = new BriefingDescriptionPanelSet(this, mission);
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }

    private void showTransfer() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        CampaignTransferPanelSet transferDisplay = new CampaignTransferPanelSet(this);
        transferDisplay.makePanels();        
        CampaignGuiContextManager.getInstance().pushToContextStack(transferDisplay);
    }

    private void showLeavePage() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        CampaignLeavePanelSet leaveDisplay = new CampaignLeavePanelSet(this);
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

        AARMainPanel eventDisplay = new AARMainPanel(campaign, this, reason, pilotEvent);
        eventDisplay.makePanels();		
        
        CampaignGuiContextManager.getInstance().pushToContextStack(eventDisplay);
    }

    private void showAAR() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        
        try
        {
            AARCoordinator.getInstance().aarPreliminary(campaign);
            
            DebriefMissionDescriptionPanel combatReportDisplay = new DebriefMissionDescriptionPanel(campaign, this);
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
        CampaignIntelligencePanelSet intelligence = new CampaignIntelligencePanelSet(campaign);
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
            Squadron squad = campaign.determineSquadron();
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

    public void clean() throws PWCGException  
    {       
        PwcgGuiContext context = CampaignGuiContextManager.getInstance().getCurrentContext();
        if (context.getCenterPanel() != null)
        {
            remove(context.getCenterPanel());        
        }
        if (context.getRightPanel() != null)
        {
            remove(context.getRightPanel());       
        }
        if (context.getLeftPanel() != null)
        {
            remove(context.getLeftPanel());       
        }
    }

    public void enableButtonsAsNeeded() throws PWCGException  
    {
        if (campaign.isCampaignActive() && !campaign.getCampaignData().isCoop())
        {            
            if (GreatAce.isGreatAce(campaign))
            {
                loneWolfMission.setEnabled(true);
            }
            else
            {
                loneWolfMission.setEnabled(false);
            }
        }
    }

    public Campaign getCampaign()
    {
        return campaign;
    }
    
}
