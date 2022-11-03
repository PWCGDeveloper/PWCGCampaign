package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class EquipmentRequestScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign = null;
    private Map<Integer, JCheckBox> aircraftRetireChecklist = new TreeMap<>();
    private Map<Integer, JCheckBox> aircraftChangeChecklist = new TreeMap<>();
    private JComboBox<String> replacementAircraftTypeSelector;

    public EquipmentRequestScreen(Campaign campaign)
    {
        super();
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }

    public void makePanels() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignLeaveScreen);
        this.setThemedImageFromName(campaign, imagePath);

        GridBagConstraints constraints = initializeGridbagConstraints();

        constraints.weightx = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(makeNavPanel(), constraints);

        constraints.weightx = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.add(makeCenterPanel(), constraints);
        
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridy = 0;
        this.add(SpacerPanelFactory.makeDocumentSpacerPanel(1400), constraints);

    }

    private JPanel makeNavPanel() throws PWCGException
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel equipmentManagementButtonPanel = new JPanel(new GridLayout(0, 1));
        equipmentManagementButtonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished with equipment management", this);
        equipmentManagementButtonPanel.add(acceptButton);

        navPanel.add(equipmentManagementButtonPanel, BorderLayout.NORTH);

        return navPanel;
    }

    private JPanel makeCenterPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel equipmentManagementPanel = new ImageResizingPanel(campaign, imagePath);
        equipmentManagementPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        JPanel equipmentSelectionPanel = makeEquipmentSelectionPanel();
        equipmentManagementPanel.add(equipmentSelectionPanel, BorderLayout.NORTH);

        JPanel equipmentReplaceConfirmationPanel = makeEquipmentSelectionConfirmationPanel();
        equipmentManagementPanel.add(equipmentReplaceConfirmationPanel, BorderLayout.CENTER);

        ImageToDisplaySizer.setDocumentSize(equipmentManagementPanel);

        return equipmentManagementPanel;
    }


    private JPanel makeEquipmentSelectionPanel() throws PWCGException
    {
        JPanel equipmentSelectionPanel = new JPanel();
        equipmentSelectionPanel.setLayout(new BorderLayout());
        equipmentSelectionPanel.setOpaque(false);
        
        JPanel equipmentRetirementSelectionPanel = makeEquipmentRetirementSelectionPanel();
        equipmentSelectionPanel.add(equipmentRetirementSelectionPanel, BorderLayout.NORTH);

        JPanel equipmentChangeSelectionPanel = makeEquipmentChangeSelectionPanel();
        equipmentSelectionPanel.add(equipmentChangeSelectionPanel, BorderLayout.SOUTH);
        
        return equipmentSelectionPanel;
    }
    
    private JPanel makeEquipmentRetirementSelectionPanel() throws PWCGException
    {
        JPanel equipmentRetirementSelectionPanel = new JPanel();
        equipmentRetirementSelectionPanel.setOpaque(false);
        equipmentRetirementSelectionPanel.setLayout(new GridLayout(0, 1));
        equipmentRetirementSelectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        SquadronMember referencePlayer = campaign.getReferencePlayer();
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(referencePlayer.getSquadronId());

        JLabel titleLabel = PWCGLabelFactory.makePaperLabelLarge("Select Planes To Retire (Requested Planes)");
        equipmentRetirementSelectionPanel.add(titleLabel);

        for (int serialNumber : equipment.getActiveEquippedPlanes().keySet())
        {
            EquippedPlane plane = equipment.getEquippedPlane(serialNumber);
            if (!plane.isEquipmentRequest())
            {
                continue;
            }
            
            JCheckBox aircraftCheckBox = makeCheckBox(plane.getDisplayName());
            aircraftRetireChecklist.put(plane.getSerialNumber(), aircraftCheckBox);
            equipmentRetirementSelectionPanel.add(aircraftCheckBox);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            equipmentRetirementSelectionPanel.add(PWCGLabelFactory.makeDummyLabel());
        }

        return equipmentRetirementSelectionPanel;
    }

    private JPanel makeEquipmentChangeSelectionPanel() throws PWCGException
    {
        JPanel equipmentChangeSelectionPanel = new JPanel();
        equipmentChangeSelectionPanel.setOpaque(false);
        equipmentChangeSelectionPanel.setLayout(new BorderLayout());
        equipmentChangeSelectionPanel.setOpaque(false);

        JPanel equipmentManagementSelectionGrid = makeAircraftSelectionGrid();
        JPanel replacementAircraftSelectionPanel = makeReplacementAircraftSelectionPanel();

        equipmentChangeSelectionPanel.add(equipmentManagementSelectionGrid, BorderLayout.NORTH);
        equipmentChangeSelectionPanel.add(replacementAircraftSelectionPanel, BorderLayout.SOUTH);

        return equipmentChangeSelectionPanel;
    }

    private JPanel makeAircraftSelectionGrid() throws PWCGException
    {
        JPanel equipmentChangeSelectionGrid = new JPanel();
        equipmentChangeSelectionGrid.setOpaque(false);
        equipmentChangeSelectionGrid.setLayout(new GridLayout(0, 1));
        equipmentChangeSelectionGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        SquadronMember referencePlayer = campaign.getReferencePlayer();
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(referencePlayer.getSquadronId());

        JLabel titleLabel = PWCGLabelFactory.makePaperLabelLarge("Select Planes To Change (Assigned Planes)");
        equipmentChangeSelectionGrid.add(titleLabel);

        for (int serialNumber : equipment.getActiveEquippedPlanes().keySet())
        {
            EquippedPlane plane = equipment.getEquippedPlane(serialNumber);
            if (plane.isEquipmentRequest())
            {
                continue;
            }
            
            JCheckBox aircraftCheckBox = makeCheckBox(plane.getDisplayName());
            aircraftChangeChecklist.put(plane.getSerialNumber(), aircraftCheckBox);
            equipmentChangeSelectionGrid.add(aircraftCheckBox);
        }
        
        for (int i = 0; i < 1; ++i)
        {
            equipmentChangeSelectionGrid.add(PWCGLabelFactory.makeDummyLabel());
        }

        JPanel equipmentChangePanel = new JPanel(new BorderLayout());
        equipmentChangePanel.setOpaque(false);
        equipmentChangePanel.add(equipmentChangeSelectionGrid, BorderLayout.NORTH);

        return equipmentChangePanel;
    }

    private JPanel makeReplacementAircraftSelectionPanel() throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        SquadronMember referencePlayer = campaign.getReferencePlayer();
        ICountry country = CountryFactory.makeCountryByCountry(referencePlayer.getCountry());
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(referencePlayer.getSquadronId());
        PwcgRoleCategory roleCategory = squadron.getSquadronRoles().selectSquadronPrimaryRoleCategory(campaign.getDate());
        List<PlaneType> availablePlaneTypes = planeTypeFactory.getAvailablePlaneTypes(country, roleCategory, campaign.getDate());        

        replacementAircraftTypeSelector = new JComboBox<String>();
        replacementAircraftTypeSelector.setOpaque(false);
        if (!availablePlaneTypes.isEmpty())
        {
            for (PlaneType planeType : availablePlaneTypes)
            {
                replacementAircraftTypeSelector.addItem(planeType.getDisplayName());
            }
            replacementAircraftTypeSelector.setSelectedIndex(availablePlaneTypes.size()-1);
        }
        

        JLabel titleLabel = PWCGLabelFactory.makePaperLabelLarge("Select Plane Type To Convert To");
        
        JPanel replacementDropDownGrid = new JPanel(new GridLayout(0,1));
        replacementDropDownGrid.setOpaque(false);

        replacementDropDownGrid.add(titleLabel);
        replacementDropDownGrid.add(replacementAircraftTypeSelector);
                
        for (int i = 0; i < 2; ++i)
        {
            replacementDropDownGrid.add(PWCGLabelFactory.makeDummyLabel());
        }

        JPanel replacementDropDownPanel = new JPanel(new BorderLayout());
        replacementDropDownPanel.setOpaque(false);
        replacementDropDownPanel.add(replacementDropDownGrid, BorderLayout.NORTH);
        return replacementDropDownPanel;
    }

    private JCheckBox makeCheckBox(String buttonText) throws PWCGException 
    {
        Color fgColor = Color.BLACK;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBox(buttonText, "", font, fgColor, null); 
        return checkBox;
    }

    private JPanel makeEquipmentSelectionConfirmationPanel() throws PWCGException
    {
        JPanel equipmentChaneConfirmationPanel = new JPanel();
        equipmentChaneConfirmationPanel.setOpaque(false);
        equipmentChaneConfirmationPanel.setLayout(new GridLayout(0, 3));

        equipmentChaneConfirmationPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton finishedButton = PWCGButtonFactory.makePaperButtonWithBorder("Process Equipment Requests", "ChangeEquipment", "Show change equipment screen",  this);
        equipmentChaneConfirmationPanel.add(finishedButton);

        equipmentChaneConfirmationPanel.add(PWCGLabelFactory.makeDummyLabel());

        return equipmentChaneConfirmationPanel;
    }

    private GridBagConstraints initializeGridbagConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipadx = 3;
        constraints.ipady = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        Insets margins = new Insets(0, 50, 50, 0);
        constraints.insets = margins;
        return constraints;
    }


    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("Finished"))
            {
                campaign.write();                
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("ChangeEquipment"))
            {
                processEquipmentRequests();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void processEquipmentRequests() throws PWCGException
    {
        changeRequestedPlanes();        
        retireRequestedPlanes();        
        campaign.write();
        updateEquipmentChangeUI();
    }

    private void changeRequestedPlanes() throws PWCGException
    {
        List<Integer> serialNumbersOfChangedPlanes = new ArrayList<>();
        for (int serialNumber : aircraftChangeChecklist.keySet())
        {
            JCheckBox checkBox = aircraftChangeChecklist.get(serialNumber);
            if (checkBox.isSelected())
            {
                serialNumbersOfChangedPlanes.add(serialNumber);
            }
        }
        
        if (!serialNumbersOfChangedPlanes.isEmpty())
        {
            String planeTypeToChangeTo = (String) replacementAircraftTypeSelector.getSelectedItem();
            
            SquadronMember referencePlayer = campaign.getReferencePlayer();
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(referencePlayer.getSquadronId());
    
            campaign.getEquipmentManager().actOnEquipmentRequest(squadron, serialNumbersOfChangedPlanes, planeTypeToChangeTo);
        }
    }

    private void retireRequestedPlanes() throws PWCGException
    {
        for (int serialNumber : aircraftRetireChecklist.keySet())
        {
            JCheckBox checkBox = aircraftRetireChecklist.get(serialNumber);
            if (!checkBox.isSelected())
            {
                continue;
            }
            
            campaign.getEquipmentManager().destroyPlane(serialNumber, campaign.getDate());
        }
    }

    private void updateEquipmentChangeUI() throws PWCGException
    {
        aircraftRetireChecklist.clear();
        aircraftChangeChecklist.clear();
        replacementAircraftTypeSelector = null;

        this.removeAll();
        makePanels();
        
        this.setVisible(false);
        this.setVisible(true);
    }
}
