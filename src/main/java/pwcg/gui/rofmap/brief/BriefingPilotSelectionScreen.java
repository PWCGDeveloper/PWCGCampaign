package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.PlanesOwnedManager;
import pwcg.core.exception.PWCGException;
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

public class BriefingPilotSelectionScreen extends ImageResizingPanel implements ActionListener, MouseWheelListener, IFlightChanged
{
    private static final long serialVersionUID = 1L;
    private CampaignHomeScreen campaignHomeGui;
    private Campaign campaign;
    private Mission mission;
    private JPanel briefingMapCenterPanel;
    private BriefingPilotChalkboard pilotPanel;
    private BriefingData briefingData;
    private Map<Integer, BriefingPlaneModificationsPicker> planeModifications = new HashMap<>();
    private BriefingFlightChooser briefingFlightChooser;
    private int selectedPilotSerialNumber = -1;

    public BriefingPilotSelectionScreen(CampaignHomeScreen campaignHomeGui)
    {
        super("");
        this.setLayout(new BorderLayout());
        
        this.campaignHomeGui = campaignHomeGui;
        
        this.briefingData =  BriefingContext.getInstance().getBriefingData();
        this.mission =  briefingData.getMission();
        this.campaign = mission.getCampaign();
    }

    public void makePanels()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.BriefingPilotSelectionScreen);
            this.setImageFromName(imagePath);
            
            briefingFlightChooser = new BriefingFlightChooser(mission, this);
            briefingFlightChooser.createBriefingSquadronSelectPanel();

            this.add(BorderLayout.WEST, makeLeftPanel());
            this.add(BorderLayout.CENTER, createCenterPanel());
            
            this.addMouseWheelListener(this);
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

        JButton scrubButton = PWCGButtonFactory.makeTranslucentMenuButton("Scrub Mission", "Scrub Mission", "Scrub this mission and return to campaign home screen", this);
        buttonGrid.add(scrubButton);

        buttonGrid.add(PWCGButtonFactory.makeDummy());

        JButton backToMapButton = PWCGButtonFactory.makeTranslucentMenuButton("Back: WP Editor", "Back: WP Editor", "Go back to waypoint editor screen", this);
        buttonGrid.add(backToMapButton);

        buttonGrid.add(PWCGButtonFactory.makeDummy());

        if (!mission.isFinalized())
        {
            JButton acceptMissionButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept Mission", "Accept Mission", "The mission will be written fow use in game", this);
            buttonGrid.add(acceptMissionButton);
        }
        else
        {
            JButton backToCampaignButton = PWCGButtonFactory.makeTranslucentMenuButton("Return To Campaign", "Return To Campaign", "Return to campaign home screen", this);
            buttonGrid.add(backToCampaignButton);
        }
        buttonGrid.add(PWCGButtonFactory.makeDummy());
        
        JButton payloadAsLeaderButton = PWCGButtonFactory.makeTranslucentMenuButton("Synchronize Payload", "Synchronize Payload", "Make flight payload the same as the leaders", this);
        buttonGrid.add(payloadAsLeaderButton);
        buttonGrid.add(PWCGButtonFactory.makeDummy());

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

        pilotPanel = new BriefingPilotChalkboard(briefingData, this);
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
            if (action.equalsIgnoreCase("Back: WP Editor"))
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
            else if (action.equals("Return To Campaign"))
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
                movePilotUp();
            }
            else if (action.contains("Move Pilot Down:"))
            {
                movePilotDown();
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
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
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
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
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
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
            CrewPlanePayloadPairing planeCrew = briefingMissionHandler.getPairingByPilot(selectedPilotSerialNumber);
            SquadronMember squadronMember = planeCrew.getPilot();
            briefingMissionHandler.unassignPilotFromBriefing(squadronMember.getSerialNumber());
            refreshPilotDisplay();
        }
    }

    private void movePilotUp() throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
            CrewPlanePayloadPairing planeCrew = briefingMissionHandler.getPairingByPilot(selectedPilotSerialNumber);
            SquadronMember squadronMember = planeCrew.getPilot();
            briefingMissionHandler.movePilotUp(squadronMember.getSerialNumber());
            refreshPilotDisplay();
        }
    }

    private void movePilotDown() throws PWCGException
    {
        if (!mission.isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
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
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
            Integer pilotSerialNumber = getPilotSerialNumberFromAction(action);
            CrewPlanePayloadPairing crewPlane = briefingMissionHandler.getPairingByPilot(pilotSerialNumber);

            BriefingPayloadPicker briefingPayloadPicker = new BriefingPayloadPicker(this, briefingData);
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
        BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
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
        BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
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
        BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
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
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        if (!ensurePlayerOwnsPlane())
        {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        SoundManager.getInstance().playSound("BriefingEnd.WAV");

        BriefingMissionUpdater.finalizeMission(briefingData);

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
    	
    	IFlight playerFlight = briefingData.getSelectedFlight();
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
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            List<BriefingFlight> briefingFlights = briefingData.getBriefingFlights();
            for (BriefingFlight briefingFlight : briefingFlights)
            {
                for (CrewPlanePayloadPairing crewPlanePair : briefingFlight.getCrews())
                {
                    SquadronMember  pilot = crewPlanePair.getPilot();
                    if (pilot.isPlayer())
                    {
                        EquippedPlane playerPlane = crewPlanePair.getPlane();
                        if (!PlanesOwnedManager.getInstance().isPlaneOwned(playerPlane.getType()))
                        {
                            ErrorDialog.userError("Player does not own his assigned plane: " + playerPlane.getDisplayName() + ".  Mission will not be written.");
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void flightChanged(Squadron squadron) throws PWCGException
    {
        briefingData.changeSelectedFlight(squadron.getSquadronId());
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

    @Override
    public void mouseWheelMoved(MouseWheelEvent event)
    {
        try
        {
            int notches = event.getWheelRotation();
            if (notches < 0) 
            {
                movePilotUp();
            } 
            else 
            {
                movePilotDown();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}
