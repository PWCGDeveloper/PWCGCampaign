package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javafx.scene.control.ButtonGroup;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;
import javax.swing.SwingConstants;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingPilotChalkboard extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;

    private Pane pilotPanel;
    private BriefingData briefingContext;
    private BriefingPilotSelectionScreen parent;
    private ButtonGroup assignedPilotButtonGroup = new ButtonGroup();
    private Map<Integer, RadioButton > activePilotRadioButtons = new HashMap<>();

    private static final double pilotWeightx = 0.1;
    private static final double planeNameWeightx = 0.1;
    private static final double payloadWeightx = 0.1;
    private static final double modificationsWeightx = 0.1;
    private static final double pilotWeighty = 0.1;
    private static final double planeNameWeighty = 0.1;
    private static final double payloadWeighty = 0.1;
    private static final double modificationsWeighty = 0.1;
    
    public BriefingPilotChalkboard(BriefingData briefingContext, BriefingPilotSelectionScreen parent)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.briefingContext = briefingContext;
        this.parent = parent;
    }
    
    public void makePanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "Chalkboard.png";
        Pane briefingPilotPanel = new ImageResizingPanel(imagePath);
        briefingPilotPanel.setLayout(new BorderLayout());
        briefingPilotPanel.setOpaque(false);
        briefingPilotPanel.setBorder(BorderFactory.createEmptyBorder(75,100,50,50));

        makePilotPanel();
        briefingPilotPanel.add(pilotPanel, BorderLayout.CENTER);
        
        this.add(briefingPilotPanel, BorderLayout.CENTER);
    }


    private Pane makePilotPanel() throws PWCGException
    {
        if (pilotPanel != null)
        {
            remove(pilotPanel);
        }

        pilotPanel = new Pane(new BorderLayout());
        pilotPanel.setOpaque(false);

        Pane assignedPilotPanel = createAssignedPilots();

        Pane unassignedPilotPanel = createUnassignedPilots();

        pilotPanel.add(BorderLayout.NORTH, assignedPilotPanel);
        pilotPanel.add(BorderLayout.CENTER, unassignedPilotPanel);
        pilotPanel.add(BorderLayout.SOUTH, SpacerPanelFactory.makeSpacerPercentPanel(25));

        return pilotPanel;
    }


    private Pane createAssignedPilots() throws PWCGException
    {
        Pane assignedPilotPanel = new Pane(new GridBagLayout());
        assignedPilotPanel.setOpaque(false);

        makeLabelsForChalkboard(assignedPilotPanel);
        addDataForChalkboard(assignedPilotPanel);
        return assignedPilotPanel;
    }

    private void makeLabelsForChalkboard(Pane assignedPilotPanel) throws PWCGException
    {
        GridBagConstraints pilotConstraints = makeGridBagConstraints(1, 1, pilotWeightx, pilotWeighty);
        Label assignedPilotLabel = ButtonFactory.makeBriefingChalkBoardLabel("Assigned Pilots:");
        assignedPilotPanel.add(assignedPilotLabel, pilotConstraints);

        GridBagConstraints planeConstraints = makeGridBagConstraints(1, 2, planeNameWeightx, planeNameWeighty);
        Label assignedAircraftLabel = ButtonFactory.makeBriefingChalkBoardLabel("Aircraft:");
        assignedPilotPanel.add(assignedAircraftLabel, planeConstraints);

        GridBagConstraints payloadConstraints = makeGridBagConstraints(1, 3, payloadWeightx, payloadWeighty);
        Label payloadLabel = ButtonFactory.makeBriefingChalkBoardLabel("Payload:");
        assignedPilotPanel.add(payloadLabel, payloadConstraints);

        GridBagConstraints modificationsConstraints = makeGridBagConstraints(1, 4, modificationsWeightx, modificationsWeighty);
        Label modificationsLabel = ButtonFactory.makeBriefingChalkBoardLabel("Modifications:");
        assignedPilotPanel.add(modificationsLabel, modificationsConstraints);
    }

    private void addDataForChalkboard(Pane assignedPilotPanel) throws PWCGException
    {
        activePilotRadioButtons.clear();
        BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
        int row = 2;
        for (CrewPlanePayloadPairing crewPlane : briefingMissionHandler.getCrews())
        {
            addPilotColumn(assignedPilotPanel, crewPlane, row);
            addPlaneColumn(assignedPilotPanel, crewPlane, row);
            addPayloadColumn(assignedPilotPanel, crewPlane, row);
            addModificationsColumn(assignedPilotPanel, crewPlane, row);
            ++row;
        }
        setCurrentlySelectedPilot();
    }

    private void setCurrentlySelectedPilot()
    {
        int selectedPilotSerialNumber = parent.getSelectedPilotSerialNumber();
        if (activePilotRadioButtons.containsKey(selectedPilotSerialNumber))
        {
            RadioButton  radioButton = activePilotRadioButtons.get(selectedPilotSerialNumber);
            radioButton.setSelected(true);
        }
        else
        {
            BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
            if (briefingMissionHandler.getBriefingAssignmentData().getCrews().size() > 0)
            {
                CrewPlanePayloadPairing crewPlane = briefingMissionHandler.getBriefingAssignmentData().getCrews().get(0);
                if (activePilotRadioButtons.containsKey(crewPlane.getPilot().getSerialNumber()))
                {
                    RadioButton  radioButton = activePilotRadioButtons.get(crewPlane.getPilot().getSerialNumber());
                    radioButton.setSelected(true);
                    parent.setSelectedPilotSerialNumber(crewPlane.getPilot().getSerialNumber());
                }
            }
        }
    }

    private void addPilotColumn(Pane assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {

        String pilotNameText = crewPlane.getPilot().getNameAndRank();
        Button assignedPilotButton = ButtonFactory.makeBriefingChalkBoardButton(pilotNameText,
                "Unassign Pilot:" + crewPlane.getPilot().getSerialNumber(), "Remove " + pilotNameText + " from flight", parent);

        RadioButton  assignedPilotRadioButton = ButtonFactory.makeBriefingChalkBoardRadioButton("Select Pilot:" + crewPlane.getPilot().getSerialNumber(), parent);
        assignedPilotRadioButton.setVerticalAlignment(SwingConstants.TOP);
        assignedPilotRadioButton.setAlignment(SwingConstants.LEFT);
        
        Pane assignedPilotGroupingPanel = new Pane(new BorderLayout());
        assignedPilotGroupingPanel.setOpaque(false);
        assignedPilotGroupingPanel.add(assignedPilotRadioButton, BorderLayout.WEST);
        assignedPilotGroupingPanel.add(assignedPilotButton, BorderLayout.CENTER);

        GridBagConstraints constraints = makeGridBagConstraints(row, 1, pilotWeightx,pilotWeighty);
        assignedPilotPanel.add(assignedPilotGroupingPanel, constraints);
        
        assignedPilotButtonGroup.add(assignedPilotRadioButton);
        activePilotRadioButtons.put( crewPlane.getPilot().getSerialNumber(), assignedPilotRadioButton);
    }

    private void addPlaneColumn(Pane assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        String planeName = formPlaneName(crewPlane.getPlane().getDisplayName(), crewPlane.getPlane().getDisplayMarkings());
        Button planeButton = ButtonFactory.makeBriefingChalkBoardButton(planeName, 
                "Change Plane:" + crewPlane.getPilot().getSerialNumber(), "Change aircraft for  " + crewPlane.getPilot().getNameAndRank(), parent);
        planeButton.setVerticalAlignment(SwingConstants.TOP);
        planeButton.setAlignment(SwingConstants.LEFT);        
        
        Pane assignedPlaneGroupingPanel = new Pane(new BorderLayout());
        assignedPlaneGroupingPanel.setOpaque(false);
        assignedPlaneGroupingPanel.add(planeButton, BorderLayout.NORTH);

        GridBagConstraints constraints = makeGridBagConstraints(row, 2, planeNameWeightx, planeNameWeighty);
        assignedPilotPanel.add(assignedPlaneGroupingPanel, constraints);
    }

    private String formPlaneName(String planeDisplayName, String planeMarkings) throws PWCGException
    {
        String planeName = planeDisplayName;
        if (planeMarkings != null && !planeMarkings.isEmpty())
        {
            planeName += " (" + planeMarkings + ")";
        }
        return planeName;
    }

    private void addPayloadColumn(Pane assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        PayloadDesignation payloadDesignation = payloadFactory.getPlanePayloadDesignation(crewPlane.getPlane().getType(), crewPlane.getPayloadId());
        String planePayloadDescription = payloadDesignation.getPayloadDescription();
        Button payloadButton = ButtonFactory.makeBriefingChalkBoardButton(planePayloadDescription,
                "Change Payload:" + crewPlane.getPilot().getSerialNumber(), "Change payload for  " + crewPlane.getPilot().getNameAndRank(), parent);
        payloadButton.setVerticalAlignment(SwingConstants.TOP);
        payloadButton.setAlignment(SwingConstants.LEFT);
        
        Pane assignedPayloadGroupingPanel = new Pane(new BorderLayout());
        assignedPayloadGroupingPanel.setOpaque(false);
        assignedPayloadGroupingPanel.add(payloadButton, BorderLayout.NORTH);

        GridBagConstraints constraints = makeGridBagConstraints(row, 3, payloadWeightx, payloadWeighty);
        assignedPilotPanel.add(assignedPayloadGroupingPanel, constraints);
    }

    private void addModificationsColumn(Pane assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        BriefingPlaneModificationsPicker planeModification = new BriefingPlaneModificationsPicker(parent, crewPlane);
        parent.addPlaneModification(crewPlane.getPilot().getSerialNumber(), planeModification);
        Pane extrasPanel = planeModification.makePlaneModifications();
        
        Pane assignedMofidicationsGroupingPanel = new Pane(new BorderLayout());
        assignedMofidicationsGroupingPanel.setOpaque(false);
        assignedMofidicationsGroupingPanel.add(extrasPanel, BorderLayout.NORTH);

        GridBagConstraints constraints = makeGridBagConstraints(row, 4, modificationsWeightx, modificationsWeighty);
        assignedPilotPanel.add(assignedMofidicationsGroupingPanel, constraints);
    }

    private Pane createUnassignedPilots() throws PWCGException
    {
        Pane unassignedPilotGrid = new Pane(new GridLayout(0, 4));
        unassignedPilotGrid.setOpaque(false);
        
        for (int i = 0; i < 1; ++i)
        {
            for (int j = 0; j < 4; ++j)
            {
                Label planeSpaceLabel = ButtonFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotGrid.add(planeSpaceLabel);
            }
        }

        Label unassignedLabel = ButtonFactory.makeBriefingChalkBoardLabel("Unassigned Pilots:");
        unassignedPilotGrid.add(unassignedLabel);

        Label assignedAircraftLabel = ButtonFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotGrid.add(assignedAircraftLabel);

        Label payloadLabel = ButtonFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotGrid.add(payloadLabel);

        Label modificationsLabel = ButtonFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotGrid.add(modificationsLabel);

        BriefingFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
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
                Button unassignedPilotButton = ButtonFactory.makeBriefingChalkBoardButton(pilotNameText,
                        "Assign Pilot:" + unassignedSquadronMember.getSerialNumber(), "Add " + pilotNameText + " to flight", parent);
                unassignedPilotGrid.add(unassignedPilotButton);
            }
            else
            {
                Label planeSpaceLabel = ButtonFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotGrid.add(planeSpaceLabel);
            }

            if (sortedUnassignedPlanes.size() > i)
            {
                EquippedPlane unassignedPlane = sortedUnassignedPlanes.get(i);
                String planeNameText = formPlaneName(unassignedPlane.getDisplayName(), unassignedPlane.getDisplayMarkings());
                Label planeLabel = ButtonFactory.makeBriefingChalkBoardLabel(planeNameText);
                unassignedPilotGrid.add(planeLabel);
            }
            else
            {
                Label planeSpaceLabel = ButtonFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotGrid.add(planeSpaceLabel);
            }

            Label payloadSpaceLabel = ButtonFactory.makeBriefingChalkBoardLabel("   ");
            unassignedPilotGrid.add(payloadSpaceLabel);

            Label modificationsSpaceLabel = ButtonFactory.makeBriefingChalkBoardLabel("   ");
            unassignedPilotGrid.add(modificationsSpaceLabel);
        }
        
        Pane unassignedPilotPanel = new Pane(new BorderLayout());
        unassignedPilotPanel.setOpaque(false);
        unassignedPilotPanel.add(unassignedPilotGrid, BorderLayout.NORTH);
        
        return unassignedPilotPanel;
    }
        
    private GridBagConstraints makeGridBagConstraints(int row, int column, double weightx, double weighty)
    {
        GridBagConstraints pilotRowConstraints = new GridBagConstraints();
        pilotRowConstraints.fill = GridBagConstraints.HORIZONTAL;
        pilotRowConstraints.weightx = weightx;
        pilotRowConstraints.weighty = weighty;
        pilotRowConstraints.ipadx = 1;
        pilotRowConstraints.ipady = 1;
        pilotRowConstraints.gridx = column;
        pilotRowConstraints.gridy = row;

        return pilotRowConstraints;
    }
}
