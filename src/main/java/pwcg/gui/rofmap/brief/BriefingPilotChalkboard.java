package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.SpacerPanelFactory;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingPilotChalkboard extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;

    private JPanel pilotPanel;
    private BriefingData briefingContext;
    private Campaign campaign;
    private BriefingPilotSelectionScreen parent;
    private ButtonGroup assignedPilotButtonGroup = new ButtonGroup();
    private Map<Integer, JRadioButton> activePilotRadioButtons = new HashMap<>();

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
        this.campaign = briefingContext.getMission().getCampaign();
    }
    
    public void makePanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "Chalkboard.png";
        JPanel briefingPilotPanel = new ImageResizingPanel(imagePath);
        briefingPilotPanel.setLayout(new BorderLayout());
        briefingPilotPanel.setOpaque(false);
        briefingPilotPanel.setBorder(BorderFactory.createEmptyBorder(75,100,50,50));

        makePilotPanel();
        briefingPilotPanel.add(pilotPanel, BorderLayout.CENTER);
        
        this.add(briefingPilotPanel, BorderLayout.CENTER);
    }


    private JPanel makePilotPanel() throws PWCGException
    {
        if (pilotPanel != null)
        {
            remove(pilotPanel);
        }

        pilotPanel = new JPanel(new BorderLayout());
        pilotPanel.setOpaque(false);

        JPanel assignedPilotPanel = createAssignedPilots();

        JPanel unassignedPilotPanel = createUnassignedPilots();

        pilotPanel.add(BorderLayout.NORTH, assignedPilotPanel);
        pilotPanel.add(BorderLayout.CENTER, unassignedPilotPanel);
        pilotPanel.add(BorderLayout.SOUTH, SpacerPanelFactory.makeSpacerPercentPanel(25));

        return pilotPanel;
    }


    private JPanel createAssignedPilots() throws PWCGException
    {
        JPanel assignedPilotPanel = new JPanel(new GridBagLayout());
        assignedPilotPanel.setOpaque(false);

        makeLabelsForChalkboard(assignedPilotPanel);
        addDataForChalkboard(assignedPilotPanel);
        return assignedPilotPanel;
    }

    private void makeLabelsForChalkboard(JPanel assignedPilotPanel) throws PWCGException
    {
        GridBagConstraints pilotConstraints = makeGridBagConstraints(1, 1, pilotWeightx, pilotWeighty);
        JLabel assignedPilotLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Assigned Pilots");
        assignedPilotPanel.add(assignedPilotLabel, pilotConstraints);

        GridBagConstraints planeConstraints = makeGridBagConstraints(1, 2, planeNameWeightx, planeNameWeighty);
        JLabel assignedAircraftLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Aircraft");
        assignedPilotPanel.add(assignedAircraftLabel, planeConstraints);

        GridBagConstraints payloadConstraints = makeGridBagConstraints(1, 3, payloadWeightx, payloadWeighty);
        JLabel payloadLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Payload");
        assignedPilotPanel.add(payloadLabel, payloadConstraints);

        GridBagConstraints modificationsConstraints = makeGridBagConstraints(1, 4, modificationsWeightx, modificationsWeighty);
        JLabel modificationsLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Modifications");
        assignedPilotPanel.add(modificationsLabel, modificationsConstraints);
    }

    private void addDataForChalkboard(JPanel assignedPilotPanel) throws PWCGException
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
            JRadioButton radioButton = activePilotRadioButtons.get(selectedPilotSerialNumber);
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
                    JRadioButton radioButton = activePilotRadioButtons.get(crewPlane.getPilot().getSerialNumber());
                    radioButton.setSelected(true);
                    parent.setSelectedPilotSerialNumber(crewPlane.getPilot().getSerialNumber());
                }
            }
        }
    }

    private void addPilotColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {

        String pilotNameText = crewPlane.getPilot().getNameAndRank();
        JButton assignedPilotButton = PWCGButtonFactory.makeBriefingChalkBoardButton(pilotNameText,
                "Unassign Pilot:" + crewPlane.getPilot().getSerialNumber(), "Remove " + pilotNameText + " from flight", parent);

        JRadioButton assignedPilotRadioButton = makeBriefingChalkBoardRadioButton("Select Pilot:" + crewPlane.getPilot().getSerialNumber(), parent);
        assignedPilotRadioButton.setVerticalAlignment(SwingConstants.TOP);
        assignedPilotRadioButton.setHorizontalAlignment(SwingConstants.LEFT);
        
        JPanel assignedPilotGroupingPanel = new JPanel(new BorderLayout());
        assignedPilotGroupingPanel.setOpaque(false);
        assignedPilotGroupingPanel.add(assignedPilotRadioButton, BorderLayout.WEST);
        assignedPilotGroupingPanel.add(assignedPilotButton, BorderLayout.CENTER);

        GridBagConstraints constraints = makeGridBagConstraints(row, 1, pilotWeightx,pilotWeighty);
        assignedPilotPanel.add(assignedPilotGroupingPanel, constraints);
        
        assignedPilotButtonGroup.add(assignedPilotRadioButton);
        activePilotRadioButtons.put( crewPlane.getPilot().getSerialNumber(), assignedPilotRadioButton);
    }

    private JRadioButton makeBriefingChalkBoardRadioButton(String commandText, ActionListener actionListener) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();
        return PWCGButtonFactory.makeRadioButton("", commandText, "", font, ColorMap.CHALK_FOREGROUND, false, actionListener);
    }

    private void addPlaneColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        String planeName = formPlaneName(crewPlane.getPlane().getDisplayName(), crewPlane.getPlane().getDisplayMarkings(campaign));
        JButton planeButton = PWCGButtonFactory.makeBriefingChalkBoardButton(planeName, 
                "Change Plane:" + crewPlane.getPilot().getSerialNumber(), "Change aircraft for  " + crewPlane.getPilot().getNameAndRank(), parent);
        planeButton.setVerticalAlignment(SwingConstants.TOP);
        planeButton.setHorizontalAlignment(SwingConstants.LEFT);        
        
        JPanel assignedPlaneGroupingPanel = new JPanel(new BorderLayout());
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

    private void addPayloadColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        Date date = briefingContext.getMission().getCampaign().getDate();
        
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        PayloadDesignation payloadDesignation = payloadFactory.getPlanePayloadDesignation(crewPlane.getPlane().getType(), crewPlane.getPayloadId(), date);
        String planePayloadDescription = payloadDesignation.getPayloadDescription();
        JButton payloadButton = PWCGButtonFactory.makeBriefingChalkBoardButton(planePayloadDescription,
                "Change Payload:" + crewPlane.getPilot().getSerialNumber(), "Change payload for  " + crewPlane.getPilot().getNameAndRank(), parent);
        payloadButton.setVerticalAlignment(SwingConstants.TOP);
        payloadButton.setHorizontalAlignment(SwingConstants.LEFT);
        
        JPanel assignedPayloadGroupingPanel = new JPanel(new BorderLayout());
        assignedPayloadGroupingPanel.setOpaque(false);
        assignedPayloadGroupingPanel.add(payloadButton, BorderLayout.NORTH);

        GridBagConstraints constraints = makeGridBagConstraints(row, 3, payloadWeightx, payloadWeighty);
        assignedPilotPanel.add(assignedPayloadGroupingPanel, constraints);
    }

    private void addModificationsColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        Date date = briefingContext.getMission().getCampaign().getDate();

        BriefingPlaneModificationsPicker planeModification = new BriefingPlaneModificationsPicker(parent, crewPlane, date);
        parent.addPlaneModification(crewPlane.getPilot().getSerialNumber(), planeModification);
        JPanel extrasPanel = planeModification.makePlaneModifications();
        
        JPanel assignedMofidicationsGroupingPanel = new JPanel(new BorderLayout());
        assignedMofidicationsGroupingPanel.setOpaque(false);
        assignedMofidicationsGroupingPanel.add(extrasPanel, BorderLayout.NORTH);

        GridBagConstraints constraints = makeGridBagConstraints(row, 4, modificationsWeightx, modificationsWeighty);
        assignedPilotPanel.add(assignedMofidicationsGroupingPanel, constraints);
    }

    private JPanel createUnassignedPilots() throws PWCGException
    {
        JPanel unassignedPilotGrid = new JPanel(new GridLayout(0, 4));
        unassignedPilotGrid.setOpaque(false);
        
        for (int i = 0; i < 1; ++i)
        {
            for (int j = 0; j < 4; ++j)
            {
                JLabel planeSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotGrid.add(planeSpaceLabel);
            }
        }

        JLabel unassignedLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Unassigned Pilots");
        unassignedPilotGrid.add(unassignedLabel);

        JLabel assignedAircraftLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotGrid.add(assignedAircraftLabel);

        JLabel payloadLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotGrid.add(payloadLabel);

        JLabel modificationsLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
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
                JButton unassignedPilotButton = PWCGButtonFactory.makeBriefingChalkBoardButton(pilotNameText,
                        "Assign Pilot:" + unassignedSquadronMember.getSerialNumber(), "Add " + pilotNameText + " to flight", parent);
                unassignedPilotGrid.add(unassignedPilotButton);
            }
            else
            {
                JLabel planeSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotGrid.add(planeSpaceLabel);
            }

            if (sortedUnassignedPlanes.size() > i)
            {
                EquippedPlane unassignedPlane = sortedUnassignedPlanes.get(i);
                String planeNameText = formPlaneName(unassignedPlane.getDisplayName(), unassignedPlane.getDisplayMarkings(campaign));
                JLabel planeLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel(planeNameText);
                unassignedPilotGrid.add(planeLabel);
            }
            else
            {
                JLabel planeSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotGrid.add(planeSpaceLabel);
            }

            JLabel payloadSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
            unassignedPilotGrid.add(payloadSpaceLabel);

            JLabel modificationsSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
            unassignedPilotGrid.add(modificationsSpaceLabel);
        }
        
        JPanel unassignedPilotPanel = new JPanel(new BorderLayout());
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
