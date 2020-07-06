package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.PlanesOwnedManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MissionLogFileValidator;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.rofmap.brief.update.BriefingMissionUpdater;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.PlaneMcu;

public class BriefingPilotSelectionScreen extends ImageResizingPanel implements ActionListener, IFlightChanged
{
    private static final long serialVersionUID = 1L;
    private CampaignHomeScreen campaignHomeGui;
    private Campaign campaign;
    private Mission mission;
    private JPanel briefingMapCenterPanel;
    private BriefingPilotChalkboard pilotPanel;
    private BriefingData briefingContext;
    private Map<Integer, BriefingPlaneModificationsPicker> planeModifications = new HashMap<>();
    private BriefingFlightChooser briefingFlightChooser;
    private int selectedPilotSerialNumber = -1;

    public BriefingPilotSelectionScreen(Campaign campaign, CampaignHomeScreen campaignHomeGui, BriefingData briefingContext, Mission mission)
    {
        super("");
        this.setLayout(new BorderLayout());
        

        this.campaign = campaign;
        this.campaignHomeGui = campaignHomeGui;
        this.briefingContext = briefingContext;
        this.mission = mission;
    }

    public void makePanels()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.BriefingPilotSelectionScreen);
            this.setImage(imagePath);

            this.removeAll();
            
            briefingFlightChooser = new BriefingFlightChooser(mission, this);
            briefingFlightChooser.createBriefingSquadronSelectPanel();

            this.add(BorderLayout.WEST, makeLeftPanel());
            this.add(BorderLayout.CENTER, createCenterPanel());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeLeftPanel() throws PWCGException 
    {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setOpaque(false);

        JPanel buttonPanel = makeButtonPanel();
        leftPanel.add(buttonPanel, BorderLayout.NORTH);
        leftPanel.add(briefingFlightChooser.getFlightChooserPanel(), BorderLayout.CENTER);
        return leftPanel;
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        JPanel pilotAssignmentNavPanel = new JPanel(new BorderLayout());
        pilotAssignmentNavPanel.setOpaque(false);

        JPanel buttonGrid = new JPanel(new GridLayout(0, 1));
        buttonGrid.setOpaque(false);

        buttonGrid.add(PWCGButtonFactory.makeDummy());

        JButton backToMapButton = PWCGButtonFactory.makeMenuButton("Back To Map", "Back To Map", this);
        buttonGrid.add(backToMapButton);

        JButton scrubButton = PWCGButtonFactory.makeMenuButton("Scrub Mission", "Scrub Mission", this);
        buttonGrid.add(scrubButton);

        if (!mission.isFinalized())
        {
            JButton acceptMissionButton = PWCGButtonFactory.makeMenuButton("Accept Mission", "Accept Mission", this);
            buttonGrid.add(acceptMissionButton);
        }
        else
        {
            JButton backToCampaignButton = PWCGButtonFactory.makeMenuButton("Back To Campaign", "Back To Campaign", this);
            buttonGrid.add(backToCampaignButton);
        }
        buttonGrid.add(PWCGButtonFactory.makeDummy());
        
        JButton payloadAsLeaderButton = PWCGButtonFactory.makeMenuButton("Synchronize Payload", "Synchronize Payload", this);
        buttonGrid.add(payloadAsLeaderButton);
        buttonGrid.add(PWCGButtonFactory.makeDummy());

        JButton moveUpButton = PWCGButtonFactory.makeMenuButton("Move Pilot Up", "Move Pilot Up:", this);
        buttonGrid.add(moveUpButton);

        JButton moveDownButton = PWCGButtonFactory.makeMenuButton("MovePilot Down", "Move Pilot Down:", this);
        buttonGrid.add(moveDownButton);

        JButton removePilotButton = PWCGButtonFactory.makeMenuButton("Unassign Pilot", "Unassign Pilot:", this);
        buttonGrid.add(removePilotButton);

        pilotAssignmentNavPanel.add(buttonGrid, BorderLayout.NORTH);

        return pilotAssignmentNavPanel;
    }

    private JPanel createCenterPanel() throws PWCGException
    {
        if (briefingMapCenterPanel != null)
        {
            this.remove(briefingMapCenterPanel);
        }
        
        briefingMapCenterPanel = new JPanel(new BorderLayout());
        briefingMapCenterPanel.setOpaque(false);

        pilotPanel = new BriefingPilotChalkboard(briefingContext, this);
        pilotPanel.makePanel();
        briefingMapCenterPanel.add(pilotPanel, BorderLayout.CENTER);
        
        return briefingMapCenterPanel;
    }

    @Override
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
            else if (action.contains("Move Pilot Up:"))
            {
                movePilotUp(action);
            }
            else if (action.contains("Move Pilot Down:"))
            {
                movePilotDown(action);
            }
            else if (action.contains("Select Pilot:"))
            {
                setSelectedPilotSerialNumber(getPilotSerialNumberFromAction(action));
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
            BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
            Integer pilotSerialNumber = getPilotSerialNumberFromAction(action);

            BriefingPlanePicker briefingPlanePicker = new BriefingPlanePicker(briefingMissionHandler, this);
            String newPlaneChoice = briefingPlanePicker.pickPlane(pilotSerialNumber);
            if (newPlaneChoice != null)
            {
                int index = newPlaneChoice.indexOf(":");
                index += 2;
                String planeSerialNumberString = newPlaneChoice.substring(index);
                Integer planeSerialNumber = Integer.valueOf(planeSerialNumberString);

                briefingMissionHandler.changePlane(pilotSerialNumber, planeSerialNumber);
            }

            refreshPilotDisplay();
        }
    }

    private void assignPilot(String action) throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
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
            BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
            CrewPlanePayloadPairing planeCrew = briefingMissionHandler.getPairingByPilot(selectedPilotSerialNumber);
            SquadronMember squadronMember = planeCrew.getPilot();
            briefingMissionHandler.unassignPilotFromBriefing(squadronMember.getSerialNumber());
            refreshPilotDisplay();
        }
    }

    private void movePilotUp(String action) throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
            CrewPlanePayloadPairing planeCrew = briefingMissionHandler.getPairingByPilot(selectedPilotSerialNumber);
            SquadronMember squadronMember = planeCrew.getPilot();
            briefingMissionHandler.movePilotUp(squadronMember.getSerialNumber());
            refreshPilotDisplay();
        }
    }

    private void movePilotDown(String action) throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
            CrewPlanePayloadPairing planeCrew = briefingMissionHandler.getPairingByPilot(selectedPilotSerialNumber);
            SquadronMember squadronMember = planeCrew.getPilot();
            briefingMissionHandler.movePilotDown(squadronMember.getSerialNumber());
            refreshPilotDisplay();
        }        
    }

    private void changePayloadForPlane(String action) throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
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
    
    public void addPlaneModification(int pilotSerialNumber, BriefingPlaneModificationsPicker planeModification)
    {
        planeModifications.put(pilotSerialNumber, planeModification);
    }

    private void setModificationInCrewPlane(Integer pilotSerialNumber) throws PWCGException
    {
        BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
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
        BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
        List<CrewPlanePayloadPairing> assignedPairings = briefingMissionHandler.getCrews();
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
        BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
        List<CrewPlanePayloadPairing> assignedPairings = briefingMissionHandler.getCrews();
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
        this.add(BorderLayout.CENTER, createCenterPanel());
        this.revalidate();
        this.repaint();
    }

    private void scrubMission() throws PWCGException
    {
        campaign.setCurrentMission(null);
        campaignHomeGui.createCampaignHomeContext();
        CampaignGuiContextManager.getInstance().backToCampaignHome();
    }

    private void acceptMission() throws PWCGException, PWCGException
    {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        if (!ensurePlayerIsInMission())
        {
            return;
        }

        if (!ensurePlayerOwnsPlane())
        {
            return;
        }

        SoundManager.getInstance().playSound("BriefingEnd.WAV");

        BriefingMissionUpdater.finalizeMission(briefingContext);
        verifyLoggingEnabled();

        campaign.setCurrentMission(mission);
        
        campaignHomeGui.createCampaignHomeContext();
        
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        CampaignGuiContextManager.getInstance().backToCampaignHome();
    }

    private void backToCampaign() throws PWCGException, PWCGException
    {
        campaignHomeGui.createCampaignHomeContext();
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

    @Override
    public void flightChanged(Squadron squadron) throws PWCGException
    {
        briefingContext.changeSelectedFlight(squadron.getSquadronId());
        this.add(BorderLayout.CENTER, createCenterPanel());
    }

    public int getSelectedPilotSerialNumber()
    {
        return selectedPilotSerialNumber;
    }

    public void setSelectedPilotSerialNumber(int selectedPilotSerialNumber)
    {
        this.selectedPilotSerialNumber = selectedPilotSerialNumber;
    }
}
