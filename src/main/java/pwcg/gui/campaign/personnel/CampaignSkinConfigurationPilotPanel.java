package pwcg.gui.campaign.personnel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class CampaignSkinConfigurationPilotPanel extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    public static String NO_SKIN = "None";

    private Campaign campaign;
    private CampaignSkinConfigurationScreen parent;

    private JPanel skinsPlanePanel = null;

    private JCheckBox squadronCheckBox = null;
    private JCheckBox nonSquadronCheckBox = null;
    private JCheckBox looseCheckBox = null;

    private JPanel skinsPilotInfoPanel = null;
    private JPanel infoContainerPanel = new JPanel(new BorderLayout());

    private ButtonGroup aircraftButtonGroup = new ButtonGroup();
    List<ButtonModel> aircraftButtonModels = new ArrayList<>();

    public CampaignSkinConfigurationPilotPanel(Campaign campaign, CampaignSkinConfigurationScreen parent) throws PWCGException
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        this.setImageFromName(imagePath);
        this.setBorder(BorderFactory.createEmptyBorder(30, 30, 50, 70));

        this.campaign = campaign;
        this.parent = parent;
    }

    public void makePanels() throws PWCGException
    {
        removeAll();

        skinsPlanePanel = makePlanePanel();
        skinsPilotInfoPanel = makePilotDescPanel();

        infoContainerPanel.setOpaque(false);

        infoContainerPanel.add(skinsPlanePanel, BorderLayout.NORTH);
        infoContainerPanel.add(skinsPilotInfoPanel, BorderLayout.SOUTH);

        add(infoContainerPanel, BorderLayout.NORTH);

        revalidate();
        repaint();
    }

    public void resetPilotInfoPanel() throws PWCGException
    {
        if (skinsPilotInfoPanel != null)
        {
            infoContainerPanel.remove(skinsPilotInfoPanel);
        }

        skinsPilotInfoPanel = makePilotDescPanel();

        infoContainerPanel.add(skinsPilotInfoPanel, BorderLayout.SOUTH);

        infoContainerPanel.revalidate();
        infoContainerPanel.repaint();
    }

    private void setSessionManagerSelectionsFromButtons() throws PWCGException
    {
        SkinSessionManager skinSessionManager = parent.getSkinSessionManager();

        skinSessionManager.setSquadronSkinsSelected(false);
        skinSessionManager.setNonSquadronSkinsSelected(false);
        skinSessionManager.setLooseSkinsSelected(false);

        if (squadronCheckBox.isSelected())
        {
            skinSessionManager.setSquadronSkinsSelected(true);
        }

        if (nonSquadronCheckBox.isSelected())
        {
            skinSessionManager.setNonSquadronSkinsSelected(true);
        }

        if (looseCheckBox.isSelected())
        {
            skinSessionManager.setLooseSkinsSelected(true);
        }
    }

    private void setButtonsFromSessionManagerSelections() throws PWCGException
    {
        squadronCheckBox.setSelected(false);
        nonSquadronCheckBox.setSelected(false);
        looseCheckBox.setSelected(false);

        SkinSessionManager skinSessionManager = parent.getSkinSessionManager();
        if (skinSessionManager.isSquadronSkinsSelected())
        {
            squadronCheckBox.setSelected(true);
        }

        if (skinSessionManager.isNonSquadronSkinsSelected())
        {
            nonSquadronCheckBox.setSelected(true);
        }

        if (skinSessionManager.isLooseSkinsSelected())
        {
            looseCheckBox.setSelected(true);
        }
    }

    private JPanel makePlanePanel() throws PWCGException
    {
        JPanel skinSetSelectionPanel = new JPanel(new BorderLayout());
        skinSetSelectionPanel.setOpaque(false);

        JPanel skinGrid = new JPanel(new GridLayout(0, 1));
        skinGrid.setOpaque(false);

        skinSetSelectionPanel.add(skinGrid, BorderLayout.NORTH);

        JPanel aircraftSelectPanel = makeAircraftSelectPanel();
        skinGrid.add(aircraftSelectPanel);

        JPanel skinsetPanel = makeSkinSetPanel();
        skinGrid.add(skinsetPanel);

        return skinSetSelectionPanel;
    }

    private JPanel makeSkinSetPanel() throws PWCGException
    {
        JPanel skinSetPanel = new JPanel(new BorderLayout());
        skinSetPanel.setOpaque(false);

        JPanel skinCategoryButtonGrid = new JPanel(new GridLayout(0, 1));
        skinCategoryButtonGrid.setOpaque(false);

        skinSetPanel.add(skinCategoryButtonGrid, BorderLayout.NORTH);

        String labelText = InternationalizationManager.getTranslation("Skin Sets");
        JLabel label = PWCGLabelFactory.makeTransparentLabel(labelText, ColorMap.PAPER_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
        skinCategoryButtonGrid.add(label);

        Color fgColor = ColorMap.PAPER_FOREGROUND;

        // Add a radio button for each skin set
        squadronCheckBox = makeCheckBox("Squadron", "SelectSkinSet:" + "Squadron", fgColor);
        skinCategoryButtonGrid.add(squadronCheckBox);

        nonSquadronCheckBox = makeCheckBox("Non Squadron", "SelectSkinSet:" + "Non Squadron", fgColor);
        skinCategoryButtonGrid.add(nonSquadronCheckBox);

        looseCheckBox = makeCheckBox("Loose", "SelectSkinSet:" + "Loose", fgColor);
        skinCategoryButtonGrid.add(looseCheckBox);

        JPanel spacePanel = createSpaceGridEntry(2);
        skinSetPanel.add(spacePanel, BorderLayout.SOUTH);

        // Set the value of the check boxes based on last values from the
        // session manager
        setButtonsFromSessionManagerSelections();

        return skinSetPanel;
    }

    private JCheckBox makeCheckBox(String buttonText, String actionCommand, Color fgColor) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBox(buttonText, actionCommand, font, fgColor, this);
        return checkBox;
    }

    private JPanel makeAircraftSelectPanel() throws PWCGException
    {
        // Make a panel to select the aircraft
        JPanel aircraftButtonPanel = new JPanel(new BorderLayout());
        aircraftButtonPanel.setOpaque(false);

        JPanel aircraftButtonGrid = new JPanel(new GridLayout(0, 1));
        aircraftButtonGrid.setOpaque(false);

        String labelText = InternationalizationManager.getTranslation("Squadron Aircraft");
        JLabel label = PWCGLabelFactory.makeTransparentLabel(labelText, ColorMap.PAPER_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
        aircraftButtonGrid.add(label);

        SquadronMember referencePlayer = campaign.findReferencePlayer();
        Squadron squad = referencePlayer.determineSquadron();

        List<PlaneType> squadronPlanes = squad.determineCurrentAircraftList(campaign.getDate());

        List<PlaneType> squadronPlanesByBest = PlaneSorter.sortPlanesByGoodness(squadronPlanes);

        boolean planeSelected = false;
        for (PlaneType plane : squadronPlanesByBest)
        {
            // Add this aircraft to the aircraft button group and panel
            JRadioButton aircraftButton = makeRadioButton(plane.getDisplayName(), "SelectPlane:" + plane.getType(), ColorMap.PAPER_FOREGROUND);
            aircraftButton.setOpaque(false);
            aircraftButtonGrid.add(aircraftButton);
            aircraftButtonGroup.add(aircraftButton);
            aircraftButtonModels.add(aircraftButton.getModel());

            // Initialize selected plane and the radio button
            if (!planeSelected)
            {
                String selectedPlane = plane.getType();
                CampaignSkinConfigurationSelectionPanel skinSelectionPanel = parent.getSkinSelectionPanel();
                skinSelectionPanel.setSelectedPlane(selectedPlane);
                aircraftButton.setSelected(true);
                planeSelected  = true;
            }
        }

        aircraftButtonPanel.add(aircraftButtonGrid, BorderLayout.NORTH);

        return aircraftButtonPanel;
    }

    private JPanel makePilotDescPanel() throws PWCGException
    {
        JPanel skinSummaryInfoPanel = new JPanel(new BorderLayout());
        skinSummaryInfoPanel.setOpaque(false);

        JPanel pilotInfoPanel = makePilotInfoPanel();
        skinSummaryInfoPanel.add(pilotInfoPanel, BorderLayout.NORTH);

        JPanel spacePanel = createSpaceGridEntry(2);
        skinSummaryInfoPanel.add(spacePanel, BorderLayout.SOUTH);

        return skinSummaryInfoPanel;
    }

    private JPanel makePilotInfoPanel() throws PWCGException
    {
        JPanel pilotInfoPanel = new JPanel(new BorderLayout());
        pilotInfoPanel.setOpaque(false);

        // Pic in north
        SkinSessionManager skinSessionManager = parent.getSkinSessionManager();
        SquadronMember pilot = skinSessionManager.getPilot();
        ImageIcon imageIcon = pilot.getPilotPictureAsImageIcon();

        JPanel pilotInteriorInfoPanel = new JPanel(new BorderLayout());
        pilotInteriorInfoPanel.setOpaque(false);

        String displayText = InternationalizationManager.getTranslation("Assigned Skins For Pilot");
        displayText += ": " + pilot.getNameAndRank();
        JLabel label = PWCGLabelFactory.makeTransparentLabel(displayText, ColorMap.PAPER_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
        pilotInteriorInfoPanel.add(label, BorderLayout.NORTH);

        // Picture label
        JLabel pilotPicLabel = PWCGLabelFactory.makeIconLabel(imageIcon);
        pilotPicLabel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        pilotInteriorInfoPanel.add(pilotPicLabel, BorderLayout.WEST);

        // The skin summary
        JPanel pilotSkinInfoPanel = makePilotSkinInfoPanel();
        pilotInteriorInfoPanel.add(pilotSkinInfoPanel, BorderLayout.CENTER);

        // embed the panel into the border layout
        pilotInfoPanel.add(pilotInteriorInfoPanel, BorderLayout.NORTH);

        return pilotInfoPanel;
    }

    private JPanel makePilotSkinInfoPanel() throws PWCGException
    {

        JPanel pilotSkinAssignmentPanel = new JPanel(new BorderLayout());
        pilotSkinAssignmentPanel.setOpaque(false);

        JPanel pilotSkinAssignmentGrid = new JPanel(new GridLayout(0, 1));
        pilotSkinAssignmentGrid.setOpaque(false);

        SkinSessionManager skinSessionManager = parent.getSkinSessionManager();
        for (String planeName : skinSessionManager.getPilotSkinSet().getAllSkins().keySet())
        {
            Skin skin = skinSessionManager.getSkinForPilotAndPlane(planeName);
            String skinName = NO_SKIN;
            String skinGroup = "";
            if (skin != null)
            {
                skinName = skin.getSkinName();

                skinGroup = PWCGContext.getInstance().getSkinManager().getSkinCategory(planeName, skinName);
            }

            JPanel skinInfoGrid = createSkinEntry(planeName, skinName, skinGroup);
            pilotSkinAssignmentGrid.add(skinInfoGrid);
        }

        ScrollBarWrapper.createScrollPaneWithMax(pilotSkinAssignmentPanel, pilotSkinAssignmentGrid, skinSessionManager.getPilotSkinSet().getAllSkins().size(),
                10);

        return pilotSkinAssignmentPanel;
    }

    private JPanel createSkinEntry(String planeName, String skinName, String skinGroup) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();

        JPanel skinInfoGrid = new JPanel(new GridLayout(0, 1));
        skinInfoGrid.setOpaque(false);

        String planeDisplayName = "";

        PlaneType plane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeName);
        if (plane != null)
        {
            planeDisplayName = plane.getDisplayName();
        }

        String description = "     " + planeDisplayName + ": " + skinName;
        if (!skinGroup.isEmpty())
        {
            description += "   (" + skinGroup + ")";
        }

        JLabel skinPlaneLabel = PWCGLabelFactory.makeTransparentLabel(description, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        skinInfoGrid.add(skinPlaneLabel);
        skinInfoGrid.add(PWCGLabelFactory.makeDummyLabel());

        return skinInfoGrid;
    }

    private JPanel createSpaceGridEntry(int numSpaces) throws PWCGException
    {
        JPanel spaceGrid = new JPanel(new GridLayout(0, 1));
        spaceGrid.setOpaque(false);

        for (int i = 0; i < numSpaces; ++i)
        {
            spaceGrid.add(PWCGLabelFactory.makeDummyLabel());
        }

        return spaceGrid;
    }

    private JRadioButton makeRadioButton(String buttonText, String actionCommand, Color fgColor) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setActionCommand(actionCommand);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);

        return button;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();

            if (action.contains("SelectPlane:"))
            {
                int index = action.indexOf(":");
                String selectedPlane = action.substring(index + 1);
                CampaignSkinConfigurationSelectionPanel skinSelectionPanel = parent.getSkinSelectionPanel();
                skinSelectionPanel.setSelectedPlane(selectedPlane);
                skinSelectionPanel.resetSkinSelectionPanel();
            }
            else if (action.contains("SelectSkinSet:"))
            {
                setSessionManagerSelectionsFromButtons();
                CampaignSkinConfigurationSelectionPanel skinSelectionPanel = parent.getSkinSelectionPanel();
                skinSelectionPanel.resetSkinSelectionPanel();
            }
            else
            {
            }

        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}
