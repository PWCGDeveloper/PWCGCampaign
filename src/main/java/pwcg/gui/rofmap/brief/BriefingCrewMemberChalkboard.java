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

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.payload.TankPayloadDesignation;
import pwcg.campaign.tank.payload.TankPayloadFactory;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.SpacerPanelFactory;
import pwcg.mission.playerunit.crew.CrewTankPayloadPairing;

public class BriefingCrewMemberChalkboard extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;

    private JPanel crewMemberPanel;
    private BriefingData briefingContext;
    private BriefingCrewMemberSelectionScreen parent;
    private ButtonGroup assignedCrewMemberButtonGroup = new ButtonGroup();
    private Map<Integer, JRadioButton> activeCrewMemberRadioButtons = new HashMap<>();

    private static final double crewMemberWeightx = 0.1;
    private static final double planeNameWeightx = 0.1;
    private static final double payloadWeightx = 0.1;
    private static final double modificationsWeightx = 0.1;
    private static final double crewMemberWeighty = 0.1;
    private static final double planeNameWeighty = 0.1;
    private static final double payloadWeighty = 0.1;
    private static final double modificationsWeighty = 0.1;
    
    public BriefingCrewMemberChalkboard(BriefingData briefingContext, BriefingCrewMemberSelectionScreen parent)
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
        JPanel briefingCrewMemberPanel = new ImageResizingPanel(imagePath);
        briefingCrewMemberPanel.setLayout(new BorderLayout());
        briefingCrewMemberPanel.setOpaque(false);
        briefingCrewMemberPanel.setBorder(BorderFactory.createEmptyBorder(75,100,50,50));

        makeCrewMemberPanel();
        briefingCrewMemberPanel.add(crewMemberPanel, BorderLayout.CENTER);
        
        this.add(briefingCrewMemberPanel, BorderLayout.CENTER);
    }


    private JPanel makeCrewMemberPanel() throws PWCGException
    {
        if (crewMemberPanel != null)
        {
            remove(crewMemberPanel);
        }

        crewMemberPanel = new JPanel(new BorderLayout());
        crewMemberPanel.setOpaque(false);

        JPanel assignedCrewMemberPanel = createAssignedCrewMembers();

        JPanel unassignedCrewMemberPanel = createUnassignedCrewMembers();

        crewMemberPanel.add(BorderLayout.NORTH, assignedCrewMemberPanel);
        crewMemberPanel.add(BorderLayout.CENTER, unassignedCrewMemberPanel);
        crewMemberPanel.add(BorderLayout.SOUTH, SpacerPanelFactory.makeSpacerPercentPanel(25));

        return crewMemberPanel;
    }


    private JPanel createAssignedCrewMembers() throws PWCGException
    {
        JPanel assignedCrewMemberPanel = new JPanel(new GridBagLayout());
        assignedCrewMemberPanel.setOpaque(false);

        makeLabelsForChalkboard(assignedCrewMemberPanel);
        addDataForChalkboard(assignedCrewMemberPanel);
        return assignedCrewMemberPanel;
    }

    private void makeLabelsForChalkboard(JPanel assignedCrewMemberPanel) throws PWCGException
    {
        GridBagConstraints crewMemberConstraints = makeGridBagConstraints(1, 1, crewMemberWeightx, crewMemberWeighty);
        JLabel assignedCrewMemberLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Assigned CrewMembers");
        assignedCrewMemberPanel.add(assignedCrewMemberLabel, crewMemberConstraints);

        GridBagConstraints planeConstraints = makeGridBagConstraints(1, 2, planeNameWeightx, planeNameWeighty);
        JLabel assignedAircraftLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Aircraft");
        assignedCrewMemberPanel.add(assignedAircraftLabel, planeConstraints);

        GridBagConstraints payloadConstraints = makeGridBagConstraints(1, 3, payloadWeightx, payloadWeighty);
        JLabel payloadLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Payload");
        assignedCrewMemberPanel.add(payloadLabel, payloadConstraints);

        GridBagConstraints modificationsConstraints = makeGridBagConstraints(1, 4, modificationsWeightx, modificationsWeighty);
        JLabel modificationsLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Modifications");
        assignedCrewMemberPanel.add(modificationsLabel, modificationsConstraints);
    }

    private void addDataForChalkboard(JPanel assignedCrewMemberPanel) throws PWCGException
    {
        activeCrewMemberRadioButtons.clear();
        BriefingUnit briefingMissionHandler = briefingContext.getActiveBriefingUnit();
        int row = 2;
        for (CrewTankPayloadPairing crewTank : briefingMissionHandler.getCrews())
        {
            addCrewMemberColumn(assignedCrewMemberPanel, crewTank, row);
            addTankColumn(assignedCrewMemberPanel, crewTank, row);
            addPayloadColumn(assignedCrewMemberPanel, crewTank, row);
            addModificationsColumn(assignedCrewMemberPanel, crewTank, row);
            ++row;
        }
        setCurrentlySelectedCrewMember();
    }

    private void setCurrentlySelectedCrewMember()
    {
        int selectedCrewMemberSerialNumber = parent.getSelectedCrewMemberSerialNumber();
        if (activeCrewMemberRadioButtons.containsKey(selectedCrewMemberSerialNumber))
        {
            JRadioButton radioButton = activeCrewMemberRadioButtons.get(selectedCrewMemberSerialNumber);
            radioButton.setSelected(true);
        }
        else
        {
            BriefingUnit briefingMissionHandler = briefingContext.getActiveBriefingUnit();
            if (briefingMissionHandler.getBriefingAssignmentData().getCrews().size() > 0)
            {
                CrewTankPayloadPairing crewTank = briefingMissionHandler.getBriefingAssignmentData().getCrews().get(0);
                if (activeCrewMemberRadioButtons.containsKey(crewTank.getCrewMember().getSerialNumber()))
                {
                    JRadioButton radioButton = activeCrewMemberRadioButtons.get(crewTank.getCrewMember().getSerialNumber());
                    radioButton.setSelected(true);
                    parent.setSelectedCrewMemberSerialNumber(crewTank.getCrewMember().getSerialNumber());
                }
            }
        }
    }

    private void addCrewMemberColumn(JPanel assignedCrewMemberPanel, CrewTankPayloadPairing crewTank, int row) throws PWCGException
    {

        String crewMemberNameText = crewTank.getCrewMember().getNameAndRank();
        JButton assignedCrewMemberButton = PWCGButtonFactory.makeBriefingChalkBoardButton(crewMemberNameText,
                "Unassign CrewMember:" + crewTank.getCrewMember().getSerialNumber(), "Remove " + crewMemberNameText + " from flight", parent);

        JRadioButton assignedCrewMemberRadioButton = makeBriefingChalkBoardRadioButton("Select CrewMember:" + crewTank.getCrewMember().getSerialNumber(), parent);
        assignedCrewMemberRadioButton.setVerticalAlignment(SwingConstants.TOP);
        assignedCrewMemberRadioButton.setHorizontalAlignment(SwingConstants.LEFT);
        
        JPanel assignedCrewMemberGroupingPanel = new JPanel(new BorderLayout());
        assignedCrewMemberGroupingPanel.setOpaque(false);
        assignedCrewMemberGroupingPanel.add(assignedCrewMemberRadioButton, BorderLayout.WEST);
        assignedCrewMemberGroupingPanel.add(assignedCrewMemberButton, BorderLayout.CENTER);

        GridBagConstraints constraints = makeGridBagConstraints(row, 1, crewMemberWeightx,crewMemberWeighty);
        assignedCrewMemberPanel.add(assignedCrewMemberGroupingPanel, constraints);
        
        assignedCrewMemberButtonGroup.add(assignedCrewMemberRadioButton);
        activeCrewMemberRadioButtons.put( crewTank.getCrewMember().getSerialNumber(), assignedCrewMemberRadioButton);
    }

    private JRadioButton makeBriefingChalkBoardRadioButton(String commandText, ActionListener actionListener) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();
        return PWCGButtonFactory.makeRadioButton("", commandText, "", font, ColorMap.CHALK_FOREGROUND, false, actionListener);
    }

    private void addTankColumn(JPanel assignedCrewMemberPanel, CrewTankPayloadPairing crewTank, int row) throws PWCGException
    {
        String planeName = formTankName(crewTank.getTank().getDisplayName());
        JButton planeButton = PWCGButtonFactory.makeBriefingChalkBoardButton(planeName, 
                "Change Tank:" + crewTank.getCrewMember().getSerialNumber(), "Change aircraft for  " + crewTank.getCrewMember().getNameAndRank(), parent);
        planeButton.setVerticalAlignment(SwingConstants.TOP);
        planeButton.setHorizontalAlignment(SwingConstants.LEFT);        
        
        JPanel assignedTankGroupingPanel = new JPanel(new BorderLayout());
        assignedTankGroupingPanel.setOpaque(false);
        assignedTankGroupingPanel.add(planeButton, BorderLayout.NORTH);

        GridBagConstraints constraints = makeGridBagConstraints(row, 2, planeNameWeightx, planeNameWeighty);
        assignedCrewMemberPanel.add(assignedTankGroupingPanel, constraints);
    }

    private String formTankName(String planeDisplayName) throws PWCGException
    {
        String planeName = planeDisplayName;
        return planeName;
    }

    private void addPayloadColumn(JPanel assignedCrewMemberPanel, CrewTankPayloadPairing crewTank, int row) throws PWCGException
    {
        Date date = briefingContext.getMission().getCampaign().getDate();
        
        TankPayloadFactory payloadFactory = new TankPayloadFactory();        
        TankPayloadDesignation payloadDesignation = payloadFactory.getTankPayloadDesignation(crewTank.getTank().getType(), crewTank.getPayloadId(), date);
        String planePayloadDescription = payloadDesignation.getPayloadDescription();
        JButton payloadButton = PWCGButtonFactory.makeBriefingChalkBoardButton(planePayloadDescription,
                "Change Payload:" + crewTank.getCrewMember().getSerialNumber(), "Change payload for  " + crewTank.getCrewMember().getNameAndRank(), parent);
        payloadButton.setVerticalAlignment(SwingConstants.TOP);
        payloadButton.setHorizontalAlignment(SwingConstants.LEFT);
        
        JPanel assignedPayloadGroupingPanel = new JPanel(new BorderLayout());
        assignedPayloadGroupingPanel.setOpaque(false);
        assignedPayloadGroupingPanel.add(payloadButton, BorderLayout.NORTH);

        GridBagConstraints constraints = makeGridBagConstraints(row, 3, payloadWeightx, payloadWeighty);
        assignedCrewMemberPanel.add(assignedPayloadGroupingPanel, constraints);
    }

    private void addModificationsColumn(JPanel assignedCrewMemberPanel, CrewTankPayloadPairing crewTank, int row) throws PWCGException
    {
        Date date = briefingContext.getMission().getCampaign().getDate();

        BriefingTankModificationsPicker planeModification = new BriefingTankModificationsPicker(parent, crewTank, date);
        parent.addTankModification(crewTank.getCrewMember().getSerialNumber(), planeModification);
        JPanel extrasPanel = planeModification.makeTankModifications();
        
        JPanel assignedMofidicationsGroupingPanel = new JPanel(new BorderLayout());
        assignedMofidicationsGroupingPanel.setOpaque(false);
        assignedMofidicationsGroupingPanel.add(extrasPanel, BorderLayout.NORTH);

        GridBagConstraints constraints = makeGridBagConstraints(row, 4, modificationsWeightx, modificationsWeighty);
        assignedCrewMemberPanel.add(assignedMofidicationsGroupingPanel, constraints);
    }

    private JPanel createUnassignedCrewMembers() throws PWCGException
    {
        JPanel unassignedCrewMemberGrid = new JPanel(new GridLayout(0, 4));
        unassignedCrewMemberGrid.setOpaque(false);
        
        for (int i = 0; i < 1; ++i)
        {
            for (int j = 0; j < 4; ++j)
            {
                JLabel planeSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
                unassignedCrewMemberGrid.add(planeSpaceLabel);
            }
        }

        JLabel unassignedLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("Unassigned CrewMembers");
        unassignedCrewMemberGrid.add(unassignedLabel);

        JLabel assignedAircraftLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
        unassignedCrewMemberGrid.add(assignedAircraftLabel);

        JLabel payloadLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
        unassignedCrewMemberGrid.add(payloadLabel);

        JLabel modificationsLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
        unassignedCrewMemberGrid.add(modificationsLabel);

        BriefingUnit briefingMissionHandler = briefingContext.getActiveBriefingUnit();
        List<CrewMember> sortedUnassignedCrewMembers = briefingMissionHandler.getSortedUnassignedCrewMembers();
        List<EquippedTank> sortedUnassignedTanks = briefingMissionHandler.getSortedUnassignedTanks();

        int numRows = sortedUnassignedCrewMembers.size();
        if (sortedUnassignedTanks.size() > numRows)
        {
            numRows = sortedUnassignedTanks.size();
        }
        
        for (int i = 0; i < numRows; ++i)
        {
            if (sortedUnassignedCrewMembers.size() > i)
            {
                CrewMember unassignedCrewMember = sortedUnassignedCrewMembers.get(i);
                String crewMemberNameText = unassignedCrewMember.getNameAndRank();
                JButton unassignedCrewMemberButton = PWCGButtonFactory.makeBriefingChalkBoardButton(crewMemberNameText,
                        "Assign CrewMember:" + unassignedCrewMember.getSerialNumber(), "Add " + crewMemberNameText + " to flight", parent);
                unassignedCrewMemberGrid.add(unassignedCrewMemberButton);
            }
            else
            {
                JLabel planeSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
                unassignedCrewMemberGrid.add(planeSpaceLabel);
            }

            if (sortedUnassignedTanks.size() > i)
            {
                EquippedTank unassignedTank = sortedUnassignedTanks.get(i);
                String planeNameText = formTankName(unassignedTank.getDisplayName());
                JLabel planeLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel(planeNameText);
                unassignedCrewMemberGrid.add(planeLabel);
            }
            else
            {
                JLabel planeSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
                unassignedCrewMemberGrid.add(planeSpaceLabel);
            }

            JLabel payloadSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
            unassignedCrewMemberGrid.add(payloadSpaceLabel);

            JLabel modificationsSpaceLabel = PWCGLabelFactory.makeBriefingChalkBoardLabel("   ");
            unassignedCrewMemberGrid.add(modificationsSpaceLabel);
        }
        
        JPanel unassignedCrewMemberPanel = new JPanel(new BorderLayout());
        unassignedCrewMemberPanel.setOpaque(false);
        unassignedCrewMemberPanel.add(unassignedCrewMemberGrid, BorderLayout.NORTH);
        
        return unassignedCrewMemberPanel;
    }
        
    private GridBagConstraints makeGridBagConstraints(int row, int column, double weightx, double weighty)
    {
        GridBagConstraints crewMemberRowConstraints = new GridBagConstraints();
        crewMemberRowConstraints.fill = GridBagConstraints.HORIZONTAL;
        crewMemberRowConstraints.weightx = weightx;
        crewMemberRowConstraints.weighty = weighty;
        crewMemberRowConstraints.ipadx = 1;
        crewMemberRowConstraints.ipady = 1;
        crewMemberRowConstraints.gridx = column;
        crewMemberRowConstraints.gridy = row;

        return crewMemberRowConstraints;
    }
}
