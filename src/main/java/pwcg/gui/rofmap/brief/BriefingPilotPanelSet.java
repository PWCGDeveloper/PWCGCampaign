package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.AutoStart;
import pwcg.campaign.utils.PlanesOwnedManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.MissionLogFileValidator;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.helper.BriefingMissionFlight;
import pwcg.gui.helper.PlayerFlightEditor;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.io.MissionFileWriter;

public class BriefingPilotPanelSet extends PwcgGuiContext implements ActionListener, IFlightChanged
{
    private static final Integer NUM_COLUMNS = 4;
    private static final long serialVersionUID = 1L;
    private CampaignHomeGUI campaignHomeGui;
    private Campaign campaign;
    private Mission mission;
    private ImageResizingPanel pilotPanel;
    private BriefingContext briefingContext;
    private Map<Integer, BriefingPlaneModificationsPicker> planeModifications = new HashMap<>();
    private BriefingFlightChooser briefingFlightChooser;
    private JPanel briefingMapCenterPanel = new JPanel(new BorderLayout());

    public BriefingPilotPanelSet(Campaign campaign, CampaignHomeGUI campaignHomeGui, BriefingContext briefingContext, Mission mission)
    {
        super();

        this.campaign = campaign;
        this.campaignHomeGui = campaignHomeGui;
        this.briefingContext = briefingContext;
        this.mission = mission;
    }

    public void makePanels()
    {
        try
        {
            this.removeAll();
            
            briefingFlightChooser = new BriefingFlightChooser(mission, this);
            briefingFlightChooser.makeComboBox();

            setLeftPanel(makeButtonPanel());
            setCenterPanel(createCenterPanel());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        String imagePath = getSideImage(campaign, "BriefingNav.jpg");

        ImageResizingPanel pilotAssignmentNavPanel = new ImageResizingPanel(imagePath);
        pilotAssignmentNavPanel.setLayout(new BorderLayout());
        pilotAssignmentNavPanel.setOpaque(false);

        JPanel buttonGrid = new JPanel(new GridLayout(0, 1));
        buttonGrid.setOpaque(false);

        JButton payloadAsLeaderButton = PWCGButtonFactory.makeMenuButton("Synchronize Payload", "Synchronize Payload", this);
        buttonGrid.add(payloadAsLeaderButton);
        buttonGrid.add(PWCGButtonFactory.makeDummy());

        JButton scrubButton = PWCGButtonFactory.makeMenuButton("Scrub Mission", "Scrub Mission", this);
        buttonGrid.add(scrubButton);
        buttonGrid.add(PWCGButtonFactory.makeDummy());

        JButton backToMapButton = PWCGButtonFactory.makeMenuButton("Back To Map", "Back To Map", this);
        buttonGrid.add(backToMapButton);
        buttonGrid.add(PWCGButtonFactory.makeDummy());

        if (!mission.isFinalized())
        {
            JButton acceptMissionButton = PWCGButtonFactory.makeMenuButton("Accept Mission", "Accept Mission", this);
            buttonGrid.add(acceptMissionButton);
            buttonGrid.add(PWCGButtonFactory.makeDummy());
        }
        else
        {
            JButton backToCampaignButton = PWCGButtonFactory.makeMenuButton("Back To Campaign", "Back To Campaign", this);
            buttonGrid.add(backToCampaignButton);
            buttonGrid.add(PWCGButtonFactory.makeDummy());
        }
        
        if (PwcgGuiModSupport.isRunningIntegrated() || PwcgGuiModSupport.isRunningDebrief())
        {
            JButton flyMissionButton = PWCGButtonFactory.makeMenuButton("Fly Mission", "Fly Mission", this);
            buttonGrid.add(flyMissionButton);
        }
        pilotAssignmentNavPanel.add(buttonGrid, BorderLayout.NORTH);

        return pilotAssignmentNavPanel;
    }

    private JPanel createCenterPanel() throws PWCGException
    {
        briefingMapCenterPanel = new JPanel(new BorderLayout());

        JPanel flightChooserPanel = createFlightChooserPanel();
        briefingMapCenterPanel.add(flightChooserPanel, BorderLayout.NORTH);

        makePilotPanel();
        briefingMapCenterPanel.add(pilotPanel, BorderLayout.CENTER);
        
        return briefingMapCenterPanel;
    }
    

    private JPanel makePilotPanel() throws PWCGException
    {
        if (pilotPanel != null)
        {
            remove(pilotPanel);
        }

        String imagePath = ContextSpecificImages.imagesMisc() + "PilotSelectChalkboard.jpg";
        pilotPanel = new ImageResizingPanel(imagePath);
        pilotPanel.setLayout(new BorderLayout());

        Insets margins = MonitorSupport.calculateInset(60, 60, 60, 60);
        pilotPanel.setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right));

        JPanel assignedPilotPanel = createAssignedPilots();
        for (int i = 0; i < NUM_COLUMNS; ++i)
        {
            JLabel spacerLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
            assignedPilotPanel.add(spacerLabel);
        }

        JPanel unassignedPilotPanel = createUnassignedPilots();
        for (int i = 0; i < (NUM_COLUMNS); ++i)
        {
            JLabel spacerLabelBottom = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
            unassignedPilotPanel.add(spacerLabelBottom);
        }

        JPanel dummy1 = makeDummyPanel();
        pilotPanel.add(dummy1, BorderLayout.WEST);

        JPanel dummy2 = makeDummyPanel();
        pilotPanel.add(dummy2, BorderLayout.EAST);

        JPanel dummy3 = makeDummyPanel();
        pilotPanel.add(dummy3, BorderLayout.NORTH);

        pilotPanel.add(BorderLayout.CENTER, assignedPilotPanel);
        pilotPanel.add(BorderLayout.SOUTH, unassignedPilotPanel);

        return pilotPanel;
    }
    
    private JPanel createFlightChooserPanel() throws PWCGException
    {
        JPanel flightChooserPanel = new JPanel(new BorderLayout());
        flightChooserPanel.setOpaque(false);
        
        flightChooserPanel.add(briefingFlightChooser.getCbFlightChooser(), BorderLayout.NORTH);
        return flightChooserPanel;
    }

    private JPanel createAssignedPilots() throws PWCGException
    {
        JPanel assignedPilotPanel = new JPanel(new GridLayout(0, NUM_COLUMNS));
        assignedPilotPanel.setOpaque(false);

        makeLabelsForChalkboard(assignedPilotPanel);
        addDataForChalkboard(assignedPilotPanel);
        return assignedPilotPanel;
    }

    private void makeLabelsForChalkboard(JPanel assignedPilotPanel) throws PWCGException
    {
        JLabel assignedPilotLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Assigned Pilots:");
        assignedPilotPanel.add(assignedPilotLabel);

        JLabel assignedAircraftLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Aircraft:");
        assignedPilotPanel.add(assignedAircraftLabel);

        JLabel payloadLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Payload:");
        assignedPilotPanel.add(payloadLabel);

        JLabel modificationsLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Modifications:");
        assignedPilotPanel.add(modificationsLabel);
    }

    private void addDataForChalkboard(JPanel assignedPilotPanel) throws PWCGException
    {
        BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
        for (CrewPlanePayloadPairing crewPlane : briefingMissionHandler.getCrewsSorted())
        {
            addPilotColumn(assignedPilotPanel, crewPlane);
            addPlaneColumn(assignedPilotPanel, crewPlane);
            addPayloadColumn(assignedPilotPanel, crewPlane);
            addModificationsColumn(assignedPilotPanel, crewPlane);
        }
    }

    private void addPilotColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        JPanel containerForUppperLeftPlacement = new JPanel(new GridLayout(0, NUM_COLUMNS));
        containerForUppperLeftPlacement.setOpaque(false);

        String pilotNameText = crewPlane.getPilot().getNameAndRank();
        JButton assignedPilotButton = PWCGButtonFactory.makeBriefingChalkBoardButton(pilotNameText,
                "Unassign Pilot:" + crewPlane.getPilot().getSerialNumber(), this);
        assignedPilotButton.setVerticalAlignment(SwingConstants.TOP);
        assignedPilotButton.setHorizontalAlignment(SwingConstants.LEFT);

        assignedPilotPanel.add(assignedPilotButton);
    }

    private void addPlaneColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        String planeName = crewPlane.getPlane().getDisplayName() + " (" + crewPlane.getPlane().getDisplayMarkings() + ")";
        JButton planeButton = PWCGButtonFactory.makeBriefingChalkBoardButton(planeName, "Change Plane:" + crewPlane.getPilot().getSerialNumber(), this);
        planeButton.setVerticalAlignment(SwingConstants.TOP);
        planeButton.setHorizontalAlignment(SwingConstants.LEFT);
        assignedPilotPanel.add(planeButton);
    }

    private void addPayloadColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        PayloadDesignation payloadDesignation = payloadFactory.getPlanePayloadDesignation(crewPlane.getPlane().getType(), crewPlane.getPayloadId());
        String planePayloadDescription = payloadDesignation.getPayloadDescription();
        JButton payloadButton = PWCGButtonFactory.makeBriefingChalkBoardButton(planePayloadDescription,
                "Change Payload:" + crewPlane.getPilot().getSerialNumber(), this);
        payloadButton.setVerticalAlignment(SwingConstants.TOP);
        payloadButton.setHorizontalAlignment(SwingConstants.LEFT);
        assignedPilotPanel.add(payloadButton);
    }

    private void addModificationsColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        BriefingPlaneModificationsPicker planeModification = new BriefingPlaneModificationsPicker(this, crewPlane);
        planeModifications.put(crewPlane.getPilot().getSerialNumber(), planeModification);
        JPanel extrasPanel = planeModification.makePlaneModifications();
        assignedPilotPanel.add(extrasPanel);
    }

    private JPanel createUnassignedPilots() throws PWCGException
    {
        JPanel unassignedPilotPanel = new JPanel(new GridLayout(0, NUM_COLUMNS));
        unassignedPilotPanel.setOpaque(false);

        JLabel unassignedLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Unassigned Pilots:");
        unassignedPilotPanel.add(unassignedLabel);

        JLabel assignedAircraftLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotPanel.add(assignedAircraftLabel);

        JLabel payloadLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotPanel.add(payloadLabel);

        JLabel modificationsLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotPanel.add(modificationsLabel);

        BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
        List<SquadronMember> sortedUnassignedPilots = briefingMissionHandler.getSortedUnassignedPilots();
        List<EquippedPlane> sortedUnassignedPlanes = briefingMissionHandler.getSortedUnassignedPlanes();

        int numRows = sortedUnassignedPilots.size();
        if (sortedUnassignedPlanes.size() > numRows)
        {
            numRows = sortedUnassignedPlanes.size();
        }

        for (int i = 0; i < numRows; ++i)
        {
            if (sortedUnassignedPilots.size() > i)
            {
                SquadronMember unassignedSquadronMember = sortedUnassignedPilots.get(i);
                String pilotNameText = unassignedSquadronMember.getNameAndRank();
                JButton unassignedPilotButton = PWCGButtonFactory.makeBriefingChalkBoardButton(pilotNameText,
                        "Assign Pilot:" + unassignedSquadronMember.getSerialNumber(), this);
                unassignedPilotPanel.add(unassignedPilotButton);
            }
            else
            {
                JLabel planeSpaceLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotPanel.add(planeSpaceLabel);
            }

            if (sortedUnassignedPlanes.size() > i)
            {
                EquippedPlane unassignedPlane = sortedUnassignedPlanes.get(i);
                String planeNameText = unassignedPlane.getDisplayName() + " (" + unassignedPlane.getDisplayMarkings() + ")";
                JLabel planeLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel(planeNameText);
                unassignedPilotPanel.add(planeLabel);
            }
            else
            {
                JLabel planeSpaceLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotPanel.add(planeSpaceLabel);
            }

            JLabel payloadSpaceLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
            unassignedPilotPanel.add(payloadSpaceLabel);

            JLabel modificationsSpaceLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
            unassignedPilotPanel.add(modificationsSpaceLabel);
        }
        return unassignedPilotPanel;
    }

    private JPanel makeDummyPanel() throws PWCGException
    {
        Color bg = ColorMap.MAP_BACKGROUND;

        JPanel dummyPanel = new JPanel();
        dummyPanel.setLayout(new GridLayout(0, 1));
        dummyPanel.setOpaque(false);

        JLabel backToMapLabel = new JLabel("          ");
        backToMapLabel.setOpaque(false);
        backToMapLabel.setBackground(bg);
        dummyPanel.add(backToMapLabel);

        return dummyPanel;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("Back To Map"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
                return;
            }
            else if (action.equals("Synchronize Payload"))
            {
                synchronizePayload();
                synchronizeModifications();
                refreshPilotDisplay();
            }
            else if (action.equals("Scrub Mission"))
            {
                scrubMission();
            }
            else if (action.equals("Accept Mission"))
            {
                acceptMission();
            }
            else if (action.equals("Back To Campaign"))
            {
                backToCampaign();
            }
            else if (action.equals("Fly Mission"))
            {
                flyMission();
            }
            else if (action.contains("Change Plane:"))
            {
                changePlaneForPilot(action);
            }
            else if (action.contains("Change Payload:"))
            {
                changePayloadForPlane(action);
            }
            else if (action.contains("SelectPlaneModification:"))
            {
                changeModificationsForPlane(action);
            }
            else if (action.contains("Assign Pilot:"))
            {
                assignPilot(action);
            }
            else if (action.contains("Unassign Pilot:"))
            {
                unassignPilot(action);
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void changePlaneForPilot(String action) throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
            Integer pilotSerialNumber = getPilotSerialNumberFromAction(action);

            BriefingPlanePicker briefingPlanePicker = new BriefingPlanePicker(briefingMissionHandler, this);
            Integer planeSerialNumber = briefingPlanePicker.pickPlane(pilotSerialNumber);
            if (planeSerialNumber != null)
            {
                briefingMissionHandler.changePlane(pilotSerialNumber, planeSerialNumber);
            }

            refreshPilotDisplay();
        }
    }

    private void assignPilot(String action) throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
            if (briefingMissionHandler.getBriefingAssignmentData().getUnassignedPlanes().size() > 0)
            {
                Integer pilotSerialNumber = getPilotSerialNumberFromAction(action);

                briefingMissionHandler.assignPilotFromBriefing(pilotSerialNumber);
                refreshPilotDisplay();
            }
        }
    }

    private void unassignPilot(String action) throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
            Integer pilotSerialNumber = getPilotSerialNumberFromAction(action);

            CrewPlanePayloadPairing planeCrew = briefingMissionHandler.getPairingByPilot(pilotSerialNumber);
            SquadronMember squadronMember = planeCrew.getPilot();
            briefingMissionHandler.unassignPilotFromBriefing(squadronMember.getSerialNumber());
            refreshPilotDisplay();
        }
    }

    private void changePayloadForPlane(String action) throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
            Integer pilotSerialNumber = getPilotSerialNumberFromAction(action);
            CrewPlanePayloadPairing crewPlane = briefingMissionHandler.getPairingByPilot(pilotSerialNumber);

            BriefingPayloadPicker briefingPayloadPicker = new BriefingPayloadPicker(this);
            int newPayload = briefingPayloadPicker.pickPayload(crewPlane.getPlane().getType());
            if (newPayload != -1)
            {
                briefingMissionHandler.modifyPayload(pilotSerialNumber, newPayload);
            }

            refreshPilotDisplay();
        }
    }

    private void changeModificationsForPlane(String action) throws PWCGException
    {
        if (!mission.isFinalized())
        {
            Integer pilotSerialNumber = getPilotSerialNumberFromAction(action);
            setModificationInCrewPlane(pilotSerialNumber);
            refreshPilotDisplay();
        }
    }

    private void setModificationInCrewPlane(Integer pilotSerialNumber) throws PWCGException
    {
        BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
        CrewPlanePayloadPairing crewPlane = briefingMissionHandler.getPairingByPilot(pilotSerialNumber);
        crewPlane.clearModification();
        BriefingPlaneModificationsPicker modificationPicker = planeModifications.get(pilotSerialNumber);
        for (String modificationDescription : modificationPicker.getPlaneModifications().keySet())
        {
            JCheckBox planeModificationCheckBox = modificationPicker.getPlaneModifications().get(modificationDescription);
            boolean ismodificationSelected = planeModificationCheckBox.isSelected();
            if (ismodificationSelected)
            {
                crewPlane.addModification(modificationDescription);
            }
            else
            {
                crewPlane.removeModification(modificationDescription);
            }
        }
    }

    private Integer getPilotSerialNumberFromAction(String action)
    {
        int index = action.indexOf(":");
        String pilotSerialNumberString = action.substring(index + 1);
        Integer pilotSerialNumber = Integer.valueOf(pilotSerialNumberString);
        return pilotSerialNumber;
    }

    private void synchronizePayload() throws PWCGException
    {
        BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
        List<CrewPlanePayloadPairing> assignedPairings = briefingMissionHandler.getCrewsSorted();
        CrewPlanePayloadPairing leadPlane = assignedPairings.get(0);
        for (int i = 1; i < assignedPairings.size(); ++i)
        {
            CrewPlanePayloadPairing subordinatePlane = assignedPairings.get(i);
            if (leadPlane.getPlane().getType().equals(subordinatePlane.getPlane().getType()))
            {
                subordinatePlane.setPayloadId(leadPlane.getPayloadId());
                subordinatePlane.setModifications(leadPlane.getModifications());
            }
        }
    }

    private void synchronizeModifications() throws PWCGException
    {
        BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
        List<CrewPlanePayloadPairing> assignedPairings = briefingMissionHandler.getCrewsSorted();
        CrewPlanePayloadPairing leadPlane = assignedPairings.get(0);
        for (int i = 1; i < assignedPairings.size(); ++i)
        {
            CrewPlanePayloadPairing subordinatePlane = assignedPairings.get(i);
            if (leadPlane.getPlane().equals(subordinatePlane.getPlane()))
            {
                subordinatePlane.clearModification();
                for (String modificationDescription : leadPlane.getModifications())
                {
                    subordinatePlane.addModification(modificationDescription);
                }
            }
        }
    }

    private void refreshPilotDisplay() throws PWCGException
    {
        setCenterPanel(createCenterPanel());
    }

    private void scrubMission() throws PWCGException
    {
        campaign.setCurrentMission(null);

        campaignHomeGui.clean();
        campaignHomeGui.createPilotContext();

        campaignHomeGui.enableButtonsAsNeeded();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void acceptMission() throws PWCGException, PWCGException
    {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        pushEditsToMission();
        if (!ensurePlayerIsInMission())
        {
            return;
        }

        if (!ensurePlayerOwnsPlane())
        {
            return;
        }

        SoundManager.getInstance().playSound("BriefingEnd.WAV");

        briefingContext.finalizeMission();
        verifyLoggingEnabled();

        campaign.setCurrentMission(mission);
        
        campaignHomeGui.clean();
        campaignHomeGui.createPilotContext();
        campaignHomeGui.enableButtonsAsNeeded();
        
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void backToCampaign() throws PWCGException, PWCGException
    {
        campaignHomeGui.clean();
        campaignHomeGui.createPilotContext();

        campaignHomeGui.enableButtonsAsNeeded();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }
    
    private boolean ensurePlayerIsInMission() throws PWCGException
    {
    	if (campaignHomeGui.getCampaign().isCoop())
    	{
    		return true;
    	}
    	
    	IFlight playerFlight = briefingContext.getSelectedFlight();
        List<PlaneMcu> playerPlanes = playerFlight.getFlightPlanes().getPlayerPlanes();
        for (PlaneMcu playerPlane : playerPlanes)
        {
            SquadronMember squadronMember = playerPlane.getPilot();
            if (squadronMember.isPlayer())
            {
                return true;
            }
        }

        ErrorDialog.userError("Player is not assigned to this mission");
        return false;
    }

    private boolean ensurePlayerOwnsPlane() throws PWCGException
    {
        IFlight playerFlight = briefingContext.getSelectedFlight();
        List<PlaneMcu> playerPlanes = playerFlight.getFlightPlanes().getPlayerPlanes();
        for (PlaneMcu playerPlane : playerPlanes)
        {
            if (!PlanesOwnedManager.getInstance().isPlaneOwned(playerPlane.getType()))
            {
                ErrorDialog.userError("Player does not own his assigned plane: " + playerPlane.getDisplayName() + ".  Mission will not be written.");
                return false;
            }
        }
        
        return true;
    }

    private void verifyLoggingEnabled()
    {
        MissionLogFileValidator missionLogFileValidator = new MissionLogFileValidator();
        boolean missionLogsEnabled = missionLogFileValidator.validateMissionLogsEnabled();
        if (!missionLogsEnabled)
        {
            ErrorDialog.userError(
                    "Mission logging is not enabled.  Before flying the mission open <game install dir>\\Data\\Startup.cfg and set mission_text_log = 1");
        }
    }

    private void flyMission() throws PWCGException, PWCGIOException
    {
        pushEditsToMission();

        MissionLogFileValidator missionLogFileValidator = new MissionLogFileValidator();
        boolean missionLogsEnabled = missionLogFileValidator.validateMissionLogsEnabled();
        if (missionLogsEnabled)
        {
            SoundManager.getInstance().playSound("BriefingEnd.WAV");

            briefingContext.finalizeMission();

            makeDataFileForMission();

            CampaignGuiContextManager.getInstance().popFromContextStack();

            System.exit(0);
        }
        else
        {
            ErrorDialog
                    .userError("Mission not started because logging is not enabled.  Open .<game install dir>\\Data\\Startup.cfg and set mission_text_log = 1");
        }

    }

    private void makeDataFileForMission() throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        String campaignName = campaign.getCampaignData().getName();

        String missionFileName = MissionFileWriter.getMissionFileName(campaign) + ".mission";

        AutoStart autoStartFile = new AutoStart();
        autoStartFile.setCampaignName(campaignName);
        autoStartFile.setMissionFileName(missionFileName);
        autoStartFile.write();
    }



    private void pushEditsToMission() throws PWCGException
    {
        BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
        PlayerFlightEditor planeGeneratorPlayer = new PlayerFlightEditor(mission.getCampaign(), briefingMissionHandler.getFlight());
        planeGeneratorPlayer.updatePlayerPlanes(briefingMissionHandler.getCrewsSorted());
    }

    @Override
    public void flightChanged(Squadron squadron) throws PWCGException
    {
        pushEditsToMission();
        briefingContext.changeSelectedFlight(squadron);
        setCenterPanel(createCenterPanel());
    }
}
