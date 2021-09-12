package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.control.Button;
import javax.swing.CheckBox;
import javax.swing.JComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.Role;
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
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class EquipmentRequestScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign = null;
    private Pane centerPanel = null;
    private Map<Integer, CheckBox> aircraftChecklist = new TreeMap<>();
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

    private Pane makeNavPanel() throws PWCGException
    {
        Pane navPanel = new Pane(new BorderLayout());
        navPanel.setOpaque(false);

        Pane equipmentManagementButtonPanel = new Pane(new GridLayout(0, 1));
        equipmentManagementButtonPanel.setOpaque(false);

        Button acceptButton = ButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished with equipment management", this);
        equipmentManagementButtonPanel.add(acceptButton);

        navPanel.add(equipmentManagementButtonPanel, BorderLayout.NORTH);

        return navPanel;
    }

    private Pane makeCenterPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel equipmentManagementSelectionPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        equipmentManagementSelectionPanel.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());

        equipmentManagementSelectionPanel.setLayout(new BorderLayout());
        equipmentManagementSelectionPanel.setOpaque(false);

        Pane equipmentManagementSelectionGrid = makeAircraftSelectionGrid();
        Pane replacementAircraftSelectionPanel = makeReplacementAircraftSelectionPanel();
        Pane equipmentReplaceConfirmationPanel = makeEquipmentSelectionConfirmationPanel();

        equipmentManagementSelectionPanel.add(equipmentManagementSelectionGrid, BorderLayout.NORTH);
        equipmentManagementSelectionPanel.add(replacementAircraftSelectionPanel, BorderLayout.CENTER);
        equipmentManagementSelectionPanel.add(equipmentReplaceConfirmationPanel, BorderLayout.SOUTH);

        return equipmentManagementSelectionPanel;
    }

    private Pane makeAircraftSelectionGrid() throws PWCGException
    {
        Pane equipmentSelectionGrid = new Pane();
        equipmentSelectionGrid.setOpaque(false);
        equipmentSelectionGrid.setLayout(new GridLayout(0, 1));
        
        SquadronMember referencePlayer = campaign.getReferencePlayer();
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(referencePlayer.getSquadronId());

        Label titleLabel = ButtonFactory.makePaperLabelLarge("Select Planes To Change");
        equipmentSelectionGrid.add(titleLabel);

        for (int serialNumber : equipment.getActiveEquippedPlanes().keySet())
        {
            EquippedPlane plane = equipment.getEquippedPlane(serialNumber);
            CheckBox aircraftCheckBox = makeCheckBox(plane.getDisplayName());
            aircraftChecklist.put(plane.getSerialNumber(), aircraftCheckBox);
            equipmentSelectionGrid.add(aircraftCheckBox);
        }
        
        for (int i = 0; i < 3; ++i)
        {
            Label spacer = new Label("     ");
            spacer.setOpaque(false);
            equipmentSelectionGrid.add(spacer);
        }

        Pane equipmentSelectionPanel = new Pane(new BorderLayout());
        equipmentSelectionPanel.setOpaque(false);
        equipmentSelectionPanel.add(equipmentSelectionGrid, BorderLayout.NORTH);

        return equipmentSelectionPanel;
    }

    private Pane makeReplacementAircraftSelectionPanel() throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        SquadronMember referencePlayer = campaign.getReferencePlayer();
        ICountry country = CountryFactory.makeCountryByCountry(referencePlayer.getCountry());
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(referencePlayer.getSquadronId());
        Role role = squadron.getSquadronRoles().selectSquadronPrimaryRole(campaign.getDate());
        List<PlaneType> availablePlaneTypes = planeTypeFactory.getAvailablePlaneTypes(country, role, campaign.getDate());        

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
        

        Label titleLabel = ButtonFactory.makePaperLabelLarge("Select Plane Type To Convert To");
        
        Pane replacementDropDownGrid = new Pane(new GridLayout(0,1));
        replacementDropDownGrid.setOpaque(false);
        replacementDropDownGrid.add(titleLabel);
        replacementDropDownGrid.add(replacementAircraftTypeSelector);

        Pane replacementDropDownPanel = new Pane(new BorderLayout());
        replacementDropDownPanel.setOpaque(false);
        replacementDropDownPanel.add(replacementDropDownGrid, BorderLayout.NORTH);

        return replacementDropDownPanel;
    }

    private CheckBox makeCheckBox(String buttonText) throws PWCGException 
    {
        Color fgColor = Color.BLACK;
        CheckBox checkBox = ButtonFactory.makeCheckBox(buttonText, fgColor);
        return checkBox;
    }

    private Pane makeEquipmentSelectionConfirmationPanel() throws PWCGException
    {
        Pane equipmentChaneConfirmationPanel = new Pane();
        equipmentChaneConfirmationPanel.setOpaque(false);
        equipmentChaneConfirmationPanel.setLayout(new GridLayout(0, 3));

        Label spacerLeft = new Label("     ");
        spacerLeft.setOpaque(false);
        equipmentChaneConfirmationPanel.add(spacerLeft);

        Button finishedButton = ButtonFactory.makePaperButtonWithBorder("Change Equipment", "ChangeEquipment", this);
        equipmentChaneConfirmationPanel.add(finishedButton);

        Label spacerRight = new Label("     ");
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
            CheckBox checkBox = aircraftChecklist.get(serialNumber);
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
