package pwcg.gui.campaign.skins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
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
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;


public class CampaignSkinManagerForPilotPanel extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    public static String NO_SKIN = "None";
    
    private SkinSessionManager skinSessionManager = null;
        
    private Campaign campaign;
    private String selectedPlane = "";
    
    private JPanel skinsPlanePanel = null;
    private JPanel skinsSelectionPanel = null;

    private JCheckBox squadronCheckBox = null;
    private JCheckBox nonSquadronCheckBox = null;
    private JCheckBox looseCheckBox = null;

    private JPanel skinsPilotInfoPanel = null;
    private JPanel infoContainerPanel = new JPanel(new BorderLayout());

    private ButtonGroup aircraftButtonGroup = new ButtonGroup();
    List<ButtonModel> aircraftButtonModels = new ArrayList<ButtonModel>();

    private ButtonGroup skinButtonGroup = new ButtonGroup();
    private List<ButtonModel> skinButtonModels = new ArrayList<ButtonModel>();

	public CampaignSkinManagerForPilotPanel(Campaign campaign, SkinSessionManager skinSessionManager)
	{
        super(ContextSpecificImages.imagesMisc() + "paperHalf.jpg");
        
        this.campaign = campaign;
        this.skinSessionManager = skinSessionManager;

        setLayout(new BorderLayout());
        Insets margins = PWCGMonitorBorders.calculateBorderMargins(10,10,10,10);
        setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right)); 
	}

    public void makePanels() throws PWCGException  
    {
        removeAll();

        skinsPlanePanel = makePlanePanel();
        skinsPilotInfoPanel = makePilotDescPanel();
        skinsSelectionPanel = makeSkinSelectionPanel();
        
        infoContainerPanel.setOpaque(false);
        
        infoContainerPanel.add(skinsPlanePanel, BorderLayout.NORTH);
        infoContainerPanel.add(skinsPilotInfoPanel, BorderLayout.SOUTH);
        
        add(infoContainerPanel, BorderLayout.NORTH);
        add(skinsSelectionPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private void resetSkinSelectionPanel() throws PWCGException  
    {
        if (skinsSelectionPanel != null)
        {
            this.remove(skinsSelectionPanel);
        }
   
        skinsSelectionPanel = makeSkinSelectionPanel();
        
        add(skinsSelectionPanel, BorderLayout.CENTER);
        
        skinsSelectionPanel.revalidate();
        skinsSelectionPanel.repaint();
    }

    private void setSessionManagerSelectionsFromButtons() throws PWCGException  
    {
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

     private void resetPilotInfoPanel() throws PWCGException  
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

    private JPanel makePlanePanel() throws PWCGException 
    {
        // Make a panel to select the aircraft
        JPanel skinSetSelectionPanel = new JPanel(new BorderLayout());
        skinSetSelectionPanel.setOpaque(false);

        JPanel skinGrid = new JPanel(new GridLayout(0,1));
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
        // Make a panel to select the aircraft
        JPanel skinSetPanel = new JPanel(new BorderLayout());
        skinSetPanel.setOpaque(false);

        JPanel skinCategoryButtonGrid = new JPanel(new GridLayout(0,1));
        skinCategoryButtonGrid.setOpaque(false);
        
        skinSetPanel.add(skinCategoryButtonGrid, BorderLayout.NORTH);
        
        JLabel label = makeLabel ("Skin Sets:");
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

        // Set the value of the check boxes based on last values from the session manager
        setButtonsFromSessionManagerSelections();
        
        return skinSetPanel;
    }

    private JCheckBox makeCheckBox(String buttonText, String actionCommand, Color fgColor) throws PWCGException 
    {
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBox(buttonText, actionCommand, fgColor, this);
        return checkBox;
    }

    private JPanel makeAircraftSelectPanel() throws PWCGException 
    {
        // Make a panel to select the aircraft
        JPanel aircraftButtonPanel = new JPanel(new BorderLayout());
        aircraftButtonPanel.setOpaque(false);

        JPanel aircraftButtonGrid = new JPanel(new GridLayout(0,1));
        aircraftButtonGrid.setOpaque(false);
                
        JLabel label = makeLabel ("Squadron Aircraft:");
        aircraftButtonGrid.add(label);

        SquadronMember referencePlayer = campaign.findReferencePlayer();
        Squadron squad = referencePlayer.determineSquadron();

        List<PlaneType> squadronPlanes = squad.determineCurrentAircraftList(campaign.getDate());

        List<PlaneType> squadronPlanesByBest = PlaneSorter.sortPlanesByGoodness(squadronPlanes);
        
        for (PlaneType plane : squadronPlanesByBest)
        {
            // Add this aircraft to the aircraft button group and panel
            JRadioButton aircraftButton = makeRadioButton(plane.getDisplayName(), "SelectPlane:" + plane.getType(), ColorMap.PAPER_FOREGROUND);
            aircraftButton.setOpaque(false);
            aircraftButtonGrid.add(aircraftButton);
            aircraftButtonGroup.add(aircraftButton);
            aircraftButtonModels.add(aircraftButton.getModel());
            
            // Initialize selected plane and the radio button
            if (selectedPlane.isEmpty())
            {
                selectedPlane = plane.getType();
                aircraftButton.setSelected(true);
            }
        }
        
        aircraftButtonPanel.add(aircraftButtonGrid, BorderLayout.NORTH);
       
        return aircraftButtonPanel;
    }

	public JPanel makeSkinSelectionPanel() throws PWCGException 
	{
        JPanel skinSelectPanel = new JPanel(new BorderLayout());
        skinSelectPanel.setOpaque(false);
		        
        JPanel skinSelectGrid = new JPanel(new GridLayout(0,3));
        skinSelectGrid.setOpaque(false);

        List<String> skinNames = new ArrayList<String>();
        addNoSkinListToSelection(skinNames);
        addSquadronSkinsToSelection(skinNames);
        addNonSquadronSkinsToSelection(skinNames);
        addLooseSkinsToSelection(skinNames);
        
        skinButtonGroup = new ButtonGroup();
        skinButtonModels = new ArrayList<ButtonModel>();
        String currentSkinNameForSelectedPlane = getSkinSelection();
        makeSkinButtons(skinSelectGrid, skinNames, currentSkinNameForSelectedPlane);
        
        ScrollBarWrapper.createScrollPaneWithMax(skinSelectPanel, skinSelectGrid, skinNames.size(), 20);
        
        return skinSelectPanel;
	}

    private void addNoSkinListToSelection(List<String> skinNames)
    {
        List<String>noSkinList = new ArrayList<String>();
        noSkinList.add(NO_SKIN);
        skinNames.addAll(noSkinList);
    }

    private void addSquadronSkinsToSelection(List<String> skinNames) throws PWCGException
    {
        if (skinSessionManager.isSquadronSkinsSelected())
        {
            List<Skin> squadronSkins = skinSessionManager.getSquadronSkins(selectedPlane);
            List<String> squadronSkinNames = getStringNamesFromSkins(squadronSkins);
            skinNames.addAll(squadronSkinNames);
        }
    }

    private void addNonSquadronSkinsToSelection(List<String> skinNames) throws PWCGException
    {
        if (skinSessionManager.isNonSquadronSkinsSelected())
        {
            List<Skin> nonSquadronSkins = skinSessionManager.getNonSquadronSkins(selectedPlane);
            List<String> nonSquadronSkinNames = getStringNamesFromSkins(nonSquadronSkins);
            skinNames.addAll(nonSquadronSkinNames);
        }
    }

    private void addLooseSkinsToSelection(List<String> skinNames) throws PWCGException
    {
        if (skinSessionManager.isLooseSkinsSelected())
        {
            List<Skin> looseSkins = skinSessionManager.getLooseSkins(selectedPlane);
            List<String> looseSkinNames = getStringNamesFromSkins(looseSkins);
            skinNames.addAll(looseSkinNames);
        }
    }

	private List<String> getStringNamesFromSkins(List<Skin> skins)
	{
	    List<String> skinNames = new ArrayList<>();
	    for (Skin skin : skins)
	    {
	        skinNames.add(skin.getSkinName());
	    }
	    
	    return skinNames;
	}

    private String getSkinSelection()
    {
        Skin skinForPlane = skinSessionManager.getSkinForPilotAndPlane(selectedPlane);
        String currentSkinNameForSelectedPlane = "";
        if (skinForPlane != null)
        {
            currentSkinNameForSelectedPlane = skinForPlane.getSkinName();
        }
        else
        {
            currentSkinNameForSelectedPlane = NO_SKIN;
        }
        return currentSkinNameForSelectedPlane;
    }


    private void makeSkinButtons(JPanel skinSelectGrid, List<String> skinNames, String currentSkinNameForSelectedPlane) throws PWCGException
    {
        for (String skinName : skinNames)
        {
            // Add this skin to the skin button group and panel
            JRadioButton skinButton = makeRadioButton(skinName, "SelectSkin:" + skinName, ColorMap.PAPER_FOREGROUND);
            skinSelectGrid.add(skinButton);
            
            // Select the skin if the pilot has one assigned and it is in this group
            if (skinName.equals(currentSkinNameForSelectedPlane))
            {
                skinButton.setSelected(true);
                
                skinButton.setForeground(ColorMap.BRITISH_RED);
            }
            
            skinButtonGroup.add(skinButton);
            skinButtonModels.add(skinButton.getModel());
        }
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
         JPanel pilotInfoPanel = new JPanel (new BorderLayout());
         pilotInfoPanel.setOpaque(false);

         Color bgColor = ColorMap.PAPER_BACKGROUND;

         // Pic in north
         SquadronMember pilot = skinSessionManager.getPilot();
         ImageIcon imageIcon = pilot.determinePilotPicture();  

         JPanel pilotInteriorInfoPanel = new JPanel (new BorderLayout());
         pilotInteriorInfoPanel.setOpaque(false);

         JLabel label = makeLabel ("Assigned Skins For Pilot:" + pilot.getNameAndRank());
         pilotInteriorInfoPanel.add(label, BorderLayout.NORTH);

         // Picture label
         JLabel pilotPicLabel = new JLabel(imageIcon);
         pilotPicLabel.setOpaque(false);
         pilotPicLabel.setBackground(bgColor);
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

         JPanel pilotSkinAssignmentPanel = new JPanel (new BorderLayout());
         pilotSkinAssignmentPanel.setOpaque(false);

         JPanel pilotSkinAssignmentGrid = new JPanel (new GridLayout(0,1));
         pilotSkinAssignmentGrid.setOpaque(false);

         // Add a dummy row for space
         for (String planeName  : skinSessionManager.getPilotSkinSet().getAllSkins().keySet())
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
         
         // Wrap the grid in a scroll pane
         ScrollBarWrapper.createScrollPaneWithMax(pilotSkinAssignmentPanel, pilotSkinAssignmentGrid, skinSessionManager.getPilotSkinSet().getAllSkins().size(), 10);

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
         
         JLabel skinPlaneLabel = new JLabel(description);
         skinPlaneLabel.setFont(font);

         JLabel spaceLabel = new JLabel("");
         spaceLabel.setFont(font);

         skinInfoGrid.add(skinPlaneLabel);
         skinInfoGrid.add(spaceLabel);
         
         return skinInfoGrid;
     }

     private JPanel createSpaceGridEntry(int numSpaces) throws PWCGException
     {
         Font font = PWCGMonitorFonts.getPrimaryFont();

         JPanel spaceGrid = new JPanel(new GridLayout(0, 1));
         spaceGrid.setOpaque(false);

         for (int i = 0; i < numSpaces; ++i)
         {
             JLabel spaceLabel = new JLabel("   ");
             spaceLabel.setFont(font);
             spaceGrid.add(spaceLabel);
         }
         
         return spaceGrid;
     }

    private JLabel makeLabel(String labelText) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();
        Color fgColor = ColorMap.PAPER_FOREGROUND;

        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.LEFT );
        label.setOpaque(false);
        label.setForeground(fgColor);
        label.setFont(font);

        return label;
    }

    
    /**
     * @param buttonText
     * @return
     * @throws PWCGException 
     */
    private JRadioButton makeRadioButton(String buttonText, String actionCommand, Color fgColor) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setActionCommand(actionCommand);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
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
                selectedPlane = action.substring(index + 1);
                
                resetSkinSelectionPanel();
            }
            else if (action.contains("SelectSkinSet:"))
            {
                setSessionManagerSelectionsFromButtons();
                resetSkinSelectionPanel();
            }
            else if (action.contains("SelectSkin:"))
            {
                int index = action.indexOf(":");
                String selectedSkinName = action.substring(index + 1);

                assignSkinToPilot(selectedSkinName);
                resetPilotInfoPanel();
                resetSkinSelectionPanel();
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

    private void assignSkinToPilot(String selectedSkinName) throws PWCGException
    {
        Skin skin = null;
        if (!selectedSkinName.equals(NO_SKIN))
        {
            skin = PWCGContext.getInstance().getSkinManager().getConfiguredSkinByName(selectedPlane, selectedSkinName);
            if (skin == null)
            {
                skin = new Skin();
                skin.setSkinName(selectedSkinName);
                ICountry country = CountryFactory.makeCountryByCountry(skinSessionManager.getPilot().getCountry());
                skin.setCountry(country.getCountryName());
                skin.setPlane(selectedPlane);
            }
        }

        skinSessionManager.updateSkinForPlane(selectedPlane, skin);
    }
}
