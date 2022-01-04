package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.utils.PlanesOwnedManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.mission.MissionGeneratorHelper;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.rofmap.brief.update.BriefingMissionUpdater;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.PlaneMcu;

public class BriefingCrewMemberSelectionScreen extends ImageResizingPanel implements ActionListener, MouseWheelListener, IFlightChanged
{
    private static final long serialVersionUID = 1L;

    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private Campaign campaign;
    private Mission mission;
    private JPanel briefingMapCenterPanel;
    private BriefingCrewMemberChalkboard crewMemberPanel;
    private BriefingData briefingData;
    private Map<Integer, BriefingPlaneModificationsPicker> planeModifications = new HashMap<>();
    private BriefingFlightChooser briefingFlightChooser;
    private int selectedCrewMemberSerialNumber = -1;

    public BriefingCrewMemberSelectionScreen(CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.campaignHomeGuiBriefingWrapper = campaignHomeGuiBriefingWrapper;

        this.briefingData = BriefingContext.getInstance().getBriefingData();
        this.mission = briefingData.getMission();
        this.campaign = mission.getCampaign();
    }

    public void makePanels()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.BriefingCrewMemberSelectionScreen);
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
        JPanel crewMemberAssignmentNavPanel = new JPanel(new BorderLayout());
        crewMemberAssignmentNavPanel.setOpaque(false);

        JPanel buttonGrid = new JPanel(new GridLayout(0, 1));
        buttonGrid.setOpaque(false);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());

        JButton scrubButton = PWCGButtonFactory.makeTranslucentMenuButton("Scrub Mission", "Scrub Mission",
                "Scrub this mission and return to campaign home screen", this);
        buttonGrid.add(scrubButton);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());

        JButton backToMapButton = PWCGButtonFactory.makeTranslucentMenuButton("Back: WP Editor", "Back: WP Editor", "Go back to waypoint editor screen", this);
        buttonGrid.add(backToMapButton);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());

        if (!mission.getFinalizer().isFinalized())
        {
            JButton acceptMissionButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept Mission", "Accept Mission",
                    "The mission will be written fow use in game", this);
            buttonGrid.add(acceptMissionButton);
        }
        else
        {
            JButton backToCampaignButton = PWCGButtonFactory.makeTranslucentMenuButton("Return To Campaign", "Return To Campaign",
                    "Return to campaign home screen", this);
            buttonGrid.add(backToCampaignButton);
        }
        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());

        JButton payloadAsLeaderButton = PWCGButtonFactory.makeTranslucentMenuButton("Synchronize Payload", "Synchronize Payload",
                "Make flight payload the same as the leaders", this);
        buttonGrid.add(payloadAsLeaderButton);
        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());

        crewMemberAssignmentNavPanel.add(buttonGrid, BorderLayout.NORTH);

        return crewMemberAssignmentNavPanel;
    }

    private JPanel createCenterPanel() throws PWCGException
    {
        if (briefingMapCenterPanel != null)
        {
            this.remove(briefingMapCenterPanel);
        }

        briefingMapCenterPanel = new JPanel(new BorderLayout());
        briefingMapCenterPanel.setOpaque(false);

        crewMemberPanel = new BriefingCrewMemberChalkboard(briefingData, this);
        crewMemberPanel.makePanel();
        briefingMapCenterPanel.add(crewMemberPanel, BorderLayout.CENTER);

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
                refreshCrewMemberDisplay();
            }
            else if (action.equals("Scrub Mission"))
            {
                MissionGeneratorHelper.scrubMission(mission.getCampaign(), campaignHomeGuiBriefingWrapper);
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
                changePlaneForCrewMember(action);
            }
            else if (action.contains("Change Payload:"))
            {
                changePayloadForPlane(action);
            }
            else if (action.contains("SelectPlaneModification:"))
            {
                changeModificationsForPlane(action);
            }
            else if (action.contains("Assign CrewMember:"))
            {
                assignCrewMember(action);
            }
            else if (action.contains("Unassign CrewMember:"))
            {
                unassignCrewMember(action);
            }
            else if (action.contains("Move CrewMember Up:"))
            {
                moveCrewMemberUp();
            }
            else if (action.contains("Move CrewMember Down:"))
            {
                moveCrewMemberDown();
            }
            else if (action.contains("Select CrewMember:"))
            {
                setSelectedCrewMemberSerialNumber(getCrewMemberSerialNumberFromAction(action));
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void changePlaneForCrewMember(String action) throws PWCGException
    {
        if (!mission.getFinalizer().isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
            Integer crewMemberSerialNumber = getCrewMemberSerialNumberFromAction(action);

            BriefingPlanePicker briefingPlanePicker = new BriefingPlanePicker(briefingMissionHandler, this);
            Integer planeSerialNumber = briefingPlanePicker.pickPlane(crewMemberSerialNumber);
            if (planeSerialNumber != null)
            {
                briefingMissionHandler.changePlane(crewMemberSerialNumber, planeSerialNumber);
            }

            refreshCrewMemberDisplay();
        }
    }

    private void assignCrewMember(String action) throws PWCGException
    {
        if (!mission.getFinalizer().isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
            if (briefingMissionHandler.getBriefingAssignmentData().getUnassignedPlanes().size() > 0)
            {
                Integer crewMemberSerialNumber = getCrewMemberSerialNumberFromAction(action);

                briefingMissionHandler.assignCrewMemberFromBriefing(crewMemberSerialNumber);
                refreshCrewMemberDisplay();
            }
        }
    }

    private void unassignCrewMember(String action) throws PWCGException
    {
        if (!mission.getFinalizer().isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
            Integer crewMemberSerialNumber = getCrewMemberSerialNumberFromAction(action);
            CrewPlanePayloadPairing planeCrew = briefingMissionHandler.getPairingByCrewMember(crewMemberSerialNumber);
            CrewMember crewMember = planeCrew.getCrewMember();
            briefingMissionHandler.unassignCrewMemberFromBriefing(crewMember.getSerialNumber());
            refreshCrewMemberDisplay();
        }
    }

    private void moveCrewMemberUp() throws PWCGException
    {
        if (!mission.getFinalizer().isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
            CrewPlanePayloadPairing planeCrew = briefingMissionHandler.getPairingByCrewMember(selectedCrewMemberSerialNumber);
            CrewMember crewMember = planeCrew.getCrewMember();
            briefingMissionHandler.moveCrewMemberUp(crewMember.getSerialNumber());
            refreshCrewMemberDisplay();
        }
    }

    private void moveCrewMemberDown() throws PWCGException
    {
        if (!mission.getFinalizer().isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
            CrewPlanePayloadPairing planeCrew = briefingMissionHandler.getPairingByCrewMember(selectedCrewMemberSerialNumber);
            CrewMember crewMember = planeCrew.getCrewMember();
            briefingMissionHandler.moveCrewMemberDown(crewMember.getSerialNumber());
            refreshCrewMemberDisplay();
        }
    }

    private void changePayloadForPlane(String action) throws PWCGException
    {
        if (!mission.getFinalizer().isFinalized())
        {
            BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
            Integer crewMemberSerialNumber = getCrewMemberSerialNumberFromAction(action);
            CrewPlanePayloadPairing crewPlane = briefingMissionHandler.getPairingByCrewMember(crewMemberSerialNumber);

            BriefingPayloadPicker briefingPayloadPicker = new BriefingPayloadPicker(this, briefingData);
            Date date = mission.getCampaign().getDate();
            int newPayload = briefingPayloadPicker.pickPayload(crewPlane.getPlane().getType(), date);
            if (newPayload != -1)
            {
                briefingMissionHandler.modifyPayload(crewMemberSerialNumber, newPayload);
            }

            refreshCrewMemberDisplay();
        }
    }

    private void changeModificationsForPlane(String action) throws PWCGException
    {
        if (!mission.getFinalizer().isFinalized())
        {
            Integer crewMemberSerialNumber = getCrewMemberSerialNumberFromAction(action);
            setModificationInCrewPlane(crewMemberSerialNumber);
            refreshCrewMemberDisplay();
        }
    }

    public void addPlaneModification(int crewMemberSerialNumber, BriefingPlaneModificationsPicker planeModification)
    {
        planeModifications.put(crewMemberSerialNumber, planeModification);
    }

    private void setModificationInCrewPlane(Integer crewMemberSerialNumber) throws PWCGException
    {
        BriefingFlight briefingMissionHandler = briefingData.getActiveBriefingFlight();
        CrewPlanePayloadPairing crewPlane = briefingMissionHandler.getPairingByCrewMember(crewMemberSerialNumber);
        crewPlane.clearModification();
        BriefingPlaneModificationsPicker modificationPicker = planeModifications.get(crewMemberSerialNumber);
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

    private Integer getCrewMemberSerialNumberFromAction(String action)
    {
        int index = action.indexOf(":");
        String crewMemberSerialNumberString = action.substring(index + 1);
        Integer crewMemberSerialNumber = Integer.valueOf(crewMemberSerialNumberString);
        return crewMemberSerialNumber;
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

    private void refreshCrewMemberDisplay() throws PWCGException
    {
        this.add(BorderLayout.CENTER, createCenterPanel());
        this.revalidate();
        this.repaint();
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
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        campaignHomeGuiBriefingWrapper.refreshCampaignPage();
        CampaignGuiContextManager.getInstance().backToCampaignHome();
    }

    private void backToCampaign() throws PWCGException, PWCGException
    {
        campaignHomeGuiBriefingWrapper.refreshCampaignPage();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private boolean ensurePlayerIsInMission() throws PWCGException
    {
        if (mission.getCampaign().isCoop())
        {
            return true;
        }

        IFlight playerFlight = briefingData.getSelectedFlight();
        List<PlaneMcu> playerPlanes = playerFlight.getFlightPlanes().getPlayerPlanes();
        for (PlaneMcu playerPlane : playerPlanes)
        {
            CrewMember crewMember = playerPlane.getCrewMember();
            if (crewMember.isPlayer())
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
                    CrewMember crewMember = crewPlanePair.getCrewMember();
                    if (crewMember.isPlayer())
                    {
                        EquippedTank playerPlane = crewPlanePair.getPlane();
                        if (!PlanesOwnedManager.getInstance().isPlaneOwned(playerPlane.getType()))
                        {
                            ErrorDialog
                                    .userError("Player does not own his assigned plane: " + playerPlane.getDisplayName() + ".  Mission will not be written.");
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void flightChanged(Company squadron) throws PWCGException
    {
        briefingData.changeSelectedFlight(squadron.getCompanyId());
        this.add(BorderLayout.CENTER, createCenterPanel());
    }

    public int getSelectedCrewMemberSerialNumber()
    {
        return selectedCrewMemberSerialNumber;
    }

    public void setSelectedCrewMemberSerialNumber(int selectedCrewMemberSerialNumber)
    {
        this.selectedCrewMemberSerialNumber = selectedCrewMemberSerialNumber;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event)
    {
        try
        {
            int notches = event.getWheelRotation();
            if (notches < 0)
            {
                moveCrewMemberUp();
            }
            else
            {
                moveCrewMemberDown();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}
