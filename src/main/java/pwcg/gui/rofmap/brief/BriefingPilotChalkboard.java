package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
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
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingPilotChalkboard extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;

    private static final Integer NUM_COLUMNS = 4;

    private JPanel pilotPanel;
    private BriefingContext briefingContext;
    private BriefingPilotPanelSet parent;

    public BriefingPilotChalkboard(BriefingContext briefingContext, BriefingPilotPanelSet parent)
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
                "Unassign Pilot:" + crewPlane.getPilot().getSerialNumber(), parent);
        assignedPilotButton.setVerticalAlignment(SwingConstants.TOP);
        assignedPilotButton.setHorizontalAlignment(SwingConstants.LEFT);

        assignedPilotPanel.add(assignedPilotButton);
    }

    private void addPlaneColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        String planeName = crewPlane.getPlane().getDisplayName() + " (" + crewPlane.getPlane().getSerialNumber() + ")";
        JButton planeButton = PWCGButtonFactory.makeBriefingChalkBoardButton(planeName, 
                "Change Plane:" + crewPlane.getPilot().getSerialNumber(), parent);
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
                "Change Payload:" + crewPlane.getPilot().getSerialNumber(), parent);
        payloadButton.setVerticalAlignment(SwingConstants.TOP);
        payloadButton.setHorizontalAlignment(SwingConstants.LEFT);
        assignedPilotPanel.add(payloadButton);
    }

    private void addModificationsColumn(JPanel assignedPilotPanel, CrewPlanePayloadPairing crewPlane) throws PWCGException
    {
        BriefingPlaneModificationsPicker planeModification = new BriefingPlaneModificationsPicker(parent, crewPlane);
        parent.addPlaneModification(crewPlane.getPilot().getSerialNumber(), planeModification);
        JPanel extrasPanel = planeModification.makePlaneModifications();
        assignedPilotPanel.add(extrasPanel);
    }

    private JPanel createUnassignedPilots() throws PWCGException
    {
        JPanel unassignedPilotGrid = new JPanel(new GridLayout(0, NUM_COLUMNS));
        unassignedPilotGrid.setOpaque(false);
        
        for (int i = 0; i < 1; ++i)
        {
            for (int j = 0; j < NUM_COLUMNS; ++j)
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
}
