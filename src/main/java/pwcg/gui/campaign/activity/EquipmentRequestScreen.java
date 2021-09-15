package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
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
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class EquipmentRequestScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign = null;
    private JPanel centerPanel = null;
    private Map<Integer, JCheckBox> aircraftChecklist = new TreeMap<>();
    private JComboBox<String> replacementAircraftTypeSelector;

    public EquipmentRequestScreen(Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }

    public void makePanels() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignLeaveScreen);
        this.setImageFromName(imagePath);

        this.add(BorderLayout.WEST, makeNavPanel());
        centerPanel =  makeCenterPanel();
        this.add(BorderLayout.CENTER, centerPanel);
        this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1400));

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
        ImageResizingPanel equipmentManagementSelectionPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        equipmentManagementSelectionPanel.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());

        equipmentManagementSelectionPanel.setLayout(new BorderLayout());
        equipmentManagementSelectionPanel.setOpaque(false);

        JPanel equipmentManagementSelectionGrid = makeAircraftSelectionGrid();
        JPanel replacementAircraftSelectionPanel = makeReplacementAircraftSelectionPanel();
        JPanel equipmentReplaceConfirmationPanel = makeEquipmentSelectionConfirmationPanel();

        equipmentManagementSelectionPanel.add(equipmentManagementSelectionGrid, BorderLayout.NORTH);
        equipmentManagementSelectionPanel.add(replacementAircraftSelectionPanel, BorderLayout.CENTER);
        equipmentManagementSelectionPanel.add(equipmentReplaceConfirmationPanel, BorderLayout.SOUTH);

        return equipmentManagementSelectionPanel;
    }

    private JPanel makeAircraftSelectionGrid() throws PWCGException
    {
        JPanel equipmentSelectionGrid = new JPanel();
        equipmentSelectionGrid.setOpaque(false);
        equipmentSelectionGrid.setLayout(new GridLayout(0, 1));
        
        SquadronMember referencePlayer = campaign.getReferencePlayer();
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(referencePlayer.getSquadronId());

        JLabel titleLabel = PWCGButtonFactory.makePaperLabelLarge("Select Planes To Change");
        equipmentSelectionGrid.add(titleLabel);

        for (int serialNumber : equipment.getActiveEquippedPlanes().keySet())
        {
            EquippedPlane plane = equipment.getEquippedPlane(serialNumber);
            JCheckBox aircraftCheckBox = makeCheckBox(plane.getDisplayName());
            aircraftChecklist.put(plane.getSerialNumber(), aircraftCheckBox);
            equipmentSelectionGrid.add(aircraftCheckBox);
        }
        
        for (int i = 0; i < 3; ++i)
        {
            JLabel spacer = new JLabel("     ");
            spacer.setOpaque(false);
            equipmentSelectionGrid.add(spacer);
        }

        JPanel equipmentSelectionPanel = new JPanel(new BorderLayout());
        equipmentSelectionPanel.setOpaque(false);
        equipmentSelectionPanel.add(equipmentSelectionGrid, BorderLayout.NORTH);

        return equipmentSelectionPanel;
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
        

        JLabel titleLabel = PWCGButtonFactory.makePaperLabelLarge("Select Plane Type To Convert To");
        
        JPanel replacementDropDownGrid = new JPanel(new GridLayout(0,1));
        replacementDropDownGrid.setOpaque(false);
        replacementDropDownGrid.add(titleLabel);
        replacementDropDownGrid.add(replacementAircraftTypeSelector);

        JPanel replacementDropDownPanel = new JPanel(new BorderLayout());
        replacementDropDownPanel.setOpaque(false);
        replacementDropDownPanel.add(replacementDropDownGrid, BorderLayout.NORTH);

        return replacementDropDownPanel;
    }

    private JCheckBox makeCheckBox(String buttonText) throws PWCGException 
    {
        Color fgColor = Color.BLACK;
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBox(buttonText, fgColor);
        return checkBox;
    }

    private JPanel makeEquipmentSelectionConfirmationPanel() throws PWCGException
    {
        JPanel equipmentChaneConfirmationPanel = new JPanel();
        equipmentChaneConfirmationPanel.setOpaque(false);
        equipmentChaneConfirmationPanel.setLayout(new GridLayout(0, 3));

        JLabel spacerLeft = new JLabel("     ");
        spacerLeft.setOpaque(false);
        equipmentChaneConfirmationPanel.add(spacerLeft);

        JButton finishedButton = PWCGButtonFactory.makePaperButtonWithBorder("Change Equipment", "ChangeEquipment", this);
        equipmentChaneConfirmationPanel.add(finishedButton);

        JLabel spacerRight = new JLabel("     ");
        spacerRight.setOpaque(false);
        equipmentChaneConfirmationPanel.add(spacerRight);

        return equipmentChaneConfirmationPanel;
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
                changeEquipment();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void changeEquipment() throws PWCGException
    {
        List<Integer> serialNumbersOfChangedPlanes = new ArrayList<>();
        for (int serialNumber : aircraftChecklist.keySet())
        {
            JCheckBox checkBox = aircraftChecklist.get(serialNumber);
            if (checkBox.isSelected())
            {
                serialNumbersOfChangedPlanes.add(serialNumber);
            }
        }
        
        if (serialNumbersOfChangedPlanes.isEmpty())
        {
            return;
        }
        
        String planeTypeToChangeTo = (String) replacementAircraftTypeSelector.getSelectedItem();
        
        SquadronMember referencePlayer = campaign.getReferencePlayer();
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(referencePlayer.getSquadronId());

        campaign.getEquipmentManager().replaceAircraftForSquadron(squadron, serialNumbersOfChangedPlanes, planeTypeToChangeTo);
        campaign.write();

        aircraftChecklist.clear();
        replacementAircraftTypeSelector = null;
        this.remove(centerPanel);

        centerPanel =  makeCenterPanel();
        this.add(centerPanel, BorderLayout.CENTER);
        
        this.setVisible(false);
        this.setVisible(true);
    }
}
