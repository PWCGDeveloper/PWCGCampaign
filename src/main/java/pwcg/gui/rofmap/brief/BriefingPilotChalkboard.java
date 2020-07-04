package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.helper.BriefingMissionFlight;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingPilotChalkboard extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;

    private JPanel pilotPanel;
    private BriefingContext briefingContext;
    private BriefingPilotSelectionScreen parent;

    private static final double pilotWeightx = 0.1;
    private static final double planeNameWeightx = 0.1;
    private static final double payloadWeightx = 0.1;
    private static final double modificationsWeightx = 0.1;
    private static final double placeInFormationWeightx = 0.1;

    
    public BriefingPilotChalkboard(BriefingContext briefingContext, BriefingPilotSelectionScreen parent)
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
        GridBagConstraints pilotConstraints = makeGridBagConstraints(1, 1, pilotWeightx);
        JLabel assignedPilotLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Assigned Pilots:");
        assignedPilotPanel.add(assignedPilotLabel, pilotConstraints);

        GridBagConstraints planeConstraints = makeGridBagConstraints(1, 2, planeNameWeightx);
        JLabel assignedAircraftLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Aircraft:");
        assignedPilotPanel.add(assignedAircraftLabel, planeConstraints);

        GridBagConstraints payloadConstraints = makeGridBagConstraints(1, 3, payloadWeightx);
        JLabel payloadLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Payload:");
        assignedPilotPanel.add(payloadLabel, payloadConstraints);

        GridBagConstraints modificationsConstraints = makeGridBagConstraints(1, 4, modificationsWeightx);
        JLabel modificationsLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Modifications:");
        assignedPilotPanel.add(modificationsLabel, modificationsConstraints);
        
        GridBagConstraints placeInFormationConstraints = makeGridBagConstraints(1, 5, placeInFormationWeightx);
        JLabel placeInFormationLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Place:");
        assignedPilotPanel.add(placeInFormationLabel, placeInFormationConstraints);
    }

    private void addDataForChalkboard(JPanel assignedPilotPanel) throws PWCGException
    {
        BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
        int row = 2;
        for (CrewPlanePayloadPairing crewPlane : briefingMissionHandler.getCrews())
        {
            addPilotColumn(assignedPilotPanel, crewPlane, row);
            addPlaneColumn(assignedPilotPanel, crewPlane, row);
            addPayloadColumn(assignedPilotPanel, crewPlane, row);
            addModificationsColumn(assignedPilotPanel, crewPlane, row);
            addPlaceInFormationColumn(assignedPilotPanel, crewPlane, row);
            ++row;
        }
    }

    private void addPilotColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        String pilotNameText = crewPlane.getPilot().getNameAndRank();
        JButton assignedPilotButton = PWCGButtonFactory.makeBriefingChalkBoardButton(pilotNameText,
                "Unassign Pilot:" + crewPlane.getPilot().getSerialNumber(), parent);
        assignedPilotButton.setVerticalAlignment(SwingConstants.TOP);
        assignedPilotButton.setHorizontalAlignment(SwingConstants.LEFT);

        GridBagConstraints constraints = makeGridBagConstraints(row, 1, pilotWeightx);
        assignedPilotPanel.add(assignedPilotButton, constraints);
    }

    private void addPlaneColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        String planeName = crewPlane.getPlane().getDisplayName() + " (" + crewPlane.getPlane().getSerialNumber() + ")";
        JButton planeButton = PWCGButtonFactory.makeBriefingChalkBoardButton(planeName, 
                "Change Plane:" + crewPlane.getPilot().getSerialNumber(), parent);
        planeButton.setVerticalAlignment(SwingConstants.TOP);
        planeButton.setHorizontalAlignment(SwingConstants.LEFT);
        
        GridBagConstraints constraints = makeGridBagConstraints(row, 2, planeNameWeightx);
        assignedPilotPanel.add(planeButton, constraints);
    }

    private void addPayloadColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        PayloadDesignation payloadDesignation = payloadFactory.getPlanePayloadDesignation(crewPlane.getPlane().getType(), crewPlane.getPayloadId());
        String planePayloadDescription = payloadDesignation.getPayloadDescription();
        JButton payloadButton = PWCGButtonFactory.makeBriefingChalkBoardButton(planePayloadDescription,
                "Change Payload:" + crewPlane.getPilot().getSerialNumber(), parent);
        payloadButton.setVerticalAlignment(SwingConstants.TOP);
        payloadButton.setHorizontalAlignment(SwingConstants.LEFT);
        
        GridBagConstraints constraints = makeGridBagConstraints(row, 3, payloadWeightx);
        assignedPilotPanel.add(payloadButton, constraints);
    }

    private void addModificationsColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        BriefingPlaneModificationsPicker planeModification = new BriefingPlaneModificationsPicker(parent, crewPlane);
        parent.addPlaneModification(crewPlane.getPilot().getSerialNumber(), planeModification);
        JPanel extrasPanel = planeModification.makePlaneModifications();

        GridBagConstraints constraints = makeGridBagConstraints(row, 4, modificationsWeightx);
        assignedPilotPanel.add(extrasPanel, constraints);
    }

    private void addPlaceInFormationColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane, int row) throws PWCGException
    {
        JPanel placeInFormationPanel = makePlaceInFormation(crewPlane);
        GridBagConstraints constraints = makeGridBagConstraints(row, 5, placeInFormationWeightx);
        assignedPilotPanel.add(placeInFormationPanel, constraints);
    }

    private JPanel makePlaceInFormation(CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        JPanel placeInFormationPanel = new JPanel(new BorderLayout());
        placeInFormationPanel.setOpaque(false);

        JPanel placeInFormationGrid = new JPanel(new GridLayout(0, 1));
        placeInFormationGrid.setOpaque(false);
        
        String imagePathUp = ContextSpecificImages.imagesMisc() + "ArrowUp.gif";   
        ImageIcon upImageIcon = ImageIconCache.getInstance().getImageIcon(imagePathUp);
        JButton upButton = makeUpDownButton(upImageIcon, crewPlane.getPilot());
        upButton.setActionCommand("Move Pilot Up:" + crewPlane.getPilot().getSerialNumber());
        placeInFormationGrid.add(upButton);
        
        String imagePathDown = ContextSpecificImages.imagesMisc() + "ArrowDown.gif";
        ImageIcon downImageIcon = ImageIconCache.getInstance().getImageIcon(imagePathDown);
        JButton downButton = makeUpDownButton(downImageIcon, crewPlane.getPilot());
        downButton.setActionCommand("Move Pilot Down:" + crewPlane.getPilot().getSerialNumber());
        placeInFormationGrid.add(downButton);
        
        placeInFormationPanel.add(placeInFormationGrid, BorderLayout.WEST);
        
        return placeInFormationPanel;
    }
    
    private JButton makeUpDownButton(ImageIcon imageIcon, SquadronMember pilot) throws PWCGException
    {
        JButton upDownButton = PWCGButtonFactory.makeBriefingChalkBoardButton("", "", null);
        upDownButton.setIcon(imageIcon);
        upDownButton.setActionCommand("" + pilot.getSerialNumber());
        upDownButton.addActionListener(parent);
        return upDownButton;
    }

    private JPanel createUnassignedPilots() throws PWCGException
    {
        JPanel unassignedPilotGrid = new JPanel(new GridLayout(0, 4));
        unassignedPilotGrid.setOpaque(false);
        
        for (int i = 0; i < 1; ++i)
        {
            for (int j = 0; j < 4; ++j)
            {
                JLabel planeSpaceLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotGrid.add(planeSpaceLabel);
            }
        }

        JLabel unassignedLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("Unassigned Pilots:");
        unassignedPilotGrid.add(unassignedLabel);

        JLabel assignedAircraftLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotGrid.add(assignedAircraftLabel);

        JLabel payloadLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotGrid.add(payloadLabel);

        JLabel modificationsLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
        unassignedPilotGrid.add(modificationsLabel);

        BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingFlight();
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
                        "Assign Pilot:" + unassignedSquadronMember.getSerialNumber(), parent);
                unassignedPilotGrid.add(unassignedPilotButton);
            }
            else
            {
                JLabel planeSpaceLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotGrid.add(planeSpaceLabel);
            }

            if (sortedUnassignedPlanes.size() > i)
            {
                EquippedPlane unassignedPlane = sortedUnassignedPlanes.get(i);
                String planeNameText = unassignedPlane.getDisplayName() + " (" + unassignedPlane.getSerialNumber() + ")";
                JLabel planeLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel(planeNameText);
                unassignedPilotGrid.add(planeLabel);
            }
            else
            {
                JLabel planeSpaceLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
                unassignedPilotGrid.add(planeSpaceLabel);
            }

            JLabel payloadSpaceLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
            unassignedPilotGrid.add(payloadSpaceLabel);

            JLabel modificationsSpaceLabel = PWCGButtonFactory.makeBriefingChalkBoardLabel("   ");
            unassignedPilotGrid.add(modificationsSpaceLabel);
        }
        
        JPanel unassignedPilotPanel = new JPanel(new BorderLayout());
        unassignedPilotPanel.setOpaque(false);
        unassignedPilotPanel.add(unassignedPilotGrid, BorderLayout.NORTH);
        
        return unassignedPilotPanel;
    }
        
    private GridBagConstraints makeGridBagConstraints(int row, int column, double weightx)
    {
        GridBagConstraints pilotRowConstraints = new GridBagConstraints();
        pilotRowConstraints.fill = GridBagConstraints.HORIZONTAL;
        pilotRowConstraints.weightx = weightx;
        pilotRowConstraints.ipadx = 1;
        pilotRowConstraints.ipady = 0;
        pilotRowConstraints.gridx = column;
        pilotRowConstraints.gridy = row;

        return pilotRowConstraints;
    }
}
