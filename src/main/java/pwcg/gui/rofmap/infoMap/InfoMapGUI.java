package pwcg.gui.rofmap.infoMap;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javafx.scene.control.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javafx.scene.control.Button;
import javax.swing.CheckBox;
import javax.swing.JComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;
import javax.swing.SwingConstants;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.utils.CampaignTransitionDates;
import pwcg.gui.utils.ButtonFactory;

public class InfoMapGUI extends MapGUI implements ActionListener
{
    private static String MAP_DELIMITER = "Map: ";
    
    private static final long serialVersionUID = 1L;

    private InfoMapPanel infoMapPanel = null;
    private JComboBox<String> cbDate = new JComboBox<String>();
    private ButtonGroup mapButtonGroup = new ButtonGroup();

    private CheckBox displayFighterSquadrons = null;
    private CheckBox displayAttackSquadrons = null;
    private CheckBox displayBomberSquadrons = null;
    private CheckBox displayReconSquadrons = null;
    private CheckBox displayAirfields = null;
    private CheckBox displayTowns = null;
    private CheckBox displayRailroadStations = null;
    private CheckBox displayBridges = null;

    public InfoMapGUI(Date mapDate) throws PWCGException  
    {        
        super(mapDate);

        PWCGContext.getInstance().initializeMap();
        PWCGContext.getInstance().setCampaign(null);
    }

    public void makeGUI() 
    {
        try
        {
            Color bg = ColorMap.MAP_BACKGROUND;
            setSize(200, 200);
            setOpaque(false);
            setBackground(bg);
            
            this.add(BorderLayout.WEST, makeNavigationPanel());            
            this.add(BorderLayout.EAST, makeSelectionPanel());
            this.add(BorderLayout.CENTER, createMapPanel());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError (e.getMessage());
        }
    }

    private Pane createMapPanel() throws PWCGException, PWCGException
    {
        Pane infoMapPanelCenter = new Pane(new BorderLayout());

        infoMapPanel = new InfoMapPanel(this);
        
        mapScroll = new MapScroll(infoMapPanel);  
        infoMapPanel.setMapBackground(100);
                
        infoMapPanelCenter.add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);
        
        infoMapPanel.setData();
        

        return infoMapPanelCenter;
    }

    private Pane makeNavigationPanel() throws PWCGException  
    {
        Pane infoMapNavPanel = new Pane(new BorderLayout());
        infoMapNavPanel.setLayout(new BorderLayout());
        infoMapNavPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        Button finishedButton = ButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished with PWCG information map", this);
        buttonPanel.add(finishedButton);
        
        Label spacer1 = ButtonFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer1);

        Pane radioButtonPanel = new Pane( new GridLayout(0,1));
        radioButtonPanel.setOpaque(false);
        
        Label spacer2 = ButtonFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer2);

        infoMapNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return infoMapNavPanel;
    }

    public Pane makeSelectionPanel() throws PWCGException 
    {
        Pane selectionPanel = new Pane(new GridLayout(0,1));
        selectionPanel.setOpaque(false);

        Pane datePanel = createDateSelection(selectionPanel);
        selectionPanel.add(datePanel);

        Pane buttonPanelGrid = makeMapCheckBoxes();
        selectionPanel.add(buttonPanelGrid);

        Pane squadronButtonPanel = makeSquadronTypeCheckBoxes();
        selectionPanel.add(squadronButtonPanel);
        
        Pane groupButtonPanel = makeGroundStructureCheckBoxes();
        selectionPanel.add(groupButtonPanel);
        
        return selectionPanel;
    }

    private Pane createDateSelection(Pane selectionPanel) throws PWCGException, PWCGException
    {
        Pane datePanel = new Pane(new BorderLayout());
        datePanel.setOpaque(false);
        
        Pane dateGrid = new Pane( new GridLayout(0,1));
        dateGrid.setOpaque(false);
        
        datePanel.add(dateGrid, BorderLayout.NORTH);
        
        Label dateLabel = ButtonFactory.makeMenuLabelLarge("Choose Date");
        dateGrid.add(dateLabel);

        setDateSelectionsByPossibleStartDatesAndMovingFront();
        cbDate.setOpaque(false);
        Color bgColor = ColorMap.PAPER_BACKGROUND;        
        cbDate.setBackground(bgColor);
        cbDate.setSelectedIndex(0);
        cbDate.addActionListener(this);
        dateGrid.add(cbDate);
        
        return datePanel;
    }

    private Pane makeMapCheckBoxes() throws PWCGException
    {
        Pane mapPanel = new Pane(new BorderLayout());
        mapPanel.setOpaque(false);
                
        Pane mapGrid = new Pane( new GridLayout(0,1));
        mapGrid.setOpaque(false);
        
        mapPanel.add(mapGrid, BorderLayout.NORTH);

        Label mapLabel = ButtonFactory.makeMenuLabelLarge("Choose Map");
        mapGrid.add(mapLabel);
        
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            addToMapGrid(mapGrid, FrontMapIdentifier.MOSCOW_MAP);
            addToMapGrid(mapGrid, FrontMapIdentifier.STALINGRAD_MAP);
            addToMapGrid(mapGrid, FrontMapIdentifier.KUBAN_MAP);
            addToMapGrid(mapGrid, FrontMapIdentifier.EAST1944_MAP);
            addToMapGrid(mapGrid, FrontMapIdentifier.EAST1945_MAP);
            addToMapGrid(mapGrid, FrontMapIdentifier.BODENPLATTE_MAP);
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            addToMapGrid(mapGrid, FrontMapIdentifier.ARRAS_MAP);
        }
        else
        {
            throw new PWCGException("No valid product selected");
        }

        return mapPanel;
    }

    private void addToMapGrid(Pane mapGrid, FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        mapGrid.add(makeRadioButton(mapIdentifier.getMapName(), MAP_DELIMITER + mapIdentifier.getMapName(), mapButtonGroup));
    }

    private void setDateSelectionsByPossibleStartDatesAndMovingFront() throws PWCGException, PWCGException
    {
        cbDate.removeAll();        
        CampaignTransitionDates campaignTransitionDates = new CampaignTransitionDates();
        List<String> newDateStrings = campaignTransitionDates.getCampaignTransitionDates();
        
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>((String[])newDateStrings.toArray( new String[newDateStrings.size()] ));
        
        cbDate.setModel(model);

        mapDate = PWCGContext.getInstance().getCurrentMap().getFrontDatesForMap().getEarliestMapDate();
    }

    private Pane makeGroundStructureCheckBoxes() throws PWCGException
    {
        Pane groundStructurePanel = new Pane(new BorderLayout());
        groundStructurePanel.setOpaque(false);
         
        Pane groundStructureGrid = new Pane( new GridLayout(0,1));
        groundStructureGrid.setOpaque(false);

        groundStructurePanel.add(groundStructureGrid, BorderLayout.NORTH);

        Label spaceLabel1 = ButtonFactory.makePaperLabelMedium(" ");
        groundStructureGrid.add(spaceLabel1);
        
        Label groundStructureLabel = ButtonFactory.makeMenuLabelLarge("Choose Category");
        groundStructureGrid.add(groundStructureLabel);

        displayAirfields = makeCheckBoxButton("Airfields", "Airfields");
        groundStructureGrid.add(displayAirfields);

        displayTowns = makeCheckBoxButton("Towns", "Towns");
        groundStructureGrid.add(displayTowns);

        displayRailroadStations = makeCheckBoxButton("Railroad Stations", "Railroad");
        groundStructureGrid.add(displayRailroadStations);

        displayBridges = makeCheckBoxButton("Bridges", "Bridges");
        groundStructureGrid.add(displayBridges);

        Label spaceLabel2 = ButtonFactory.makePaperLabelMedium(" ");
        groundStructureGrid.add(spaceLabel2);
        
        return groundStructurePanel;
    }

    private Pane makeSquadronTypeCheckBoxes() throws PWCGException
    {
        Pane rolePanel = new Pane(new BorderLayout());
        rolePanel.setOpaque(false);
         
        Pane roleGrid = new Pane( new GridLayout(0,1));
        roleGrid.setOpaque(false);

        rolePanel.add(roleGrid, BorderLayout.NORTH);

        Label spaceLabel1 = ButtonFactory.makePaperLabelMedium(" ");
        roleGrid.add(spaceLabel1);
        
        Label roleLabel = ButtonFactory.makeMenuLabelLarge("Choose Aircraft Role");
        roleGrid.add(roleLabel);

        displayFighterSquadrons = makeCheckBoxButton("Fighter", "Fighter");
        roleGrid.add(displayFighterSquadrons);

        displayAttackSquadrons = makeCheckBoxButton("Attack", "Attack");
        roleGrid.add(displayAttackSquadrons);

        displayBomberSquadrons = makeCheckBoxButton("Bomber", "Bomber");
        roleGrid.add(displayBomberSquadrons);

        displayReconSquadrons = makeCheckBoxButton("Recon", "Recon");
        roleGrid.add(displayReconSquadrons);

        Label spaceLabel2 = ButtonFactory.makePaperLabelMedium(" ");
        roleGrid.add(spaceLabel2);
        
        return rolePanel;
    }

    private RadioButton  makeRadioButton(String buttonText, String commandString, ButtonGroup buttonGroup) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Color bgColor = ColorMap.CHALK_BACKGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        RadioButton  button = new RadioButton (buttonText);
        button.setAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFont(font);
        button.setActionCommand(commandString);

        buttonGroup.add(button);

        return button;
    }	

    private CheckBox makeCheckBoxButton(String buttonText, String commandString) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Color bgColor = ColorMap.CHALK_BACKGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        CheckBox button = new CheckBox(buttonText);
        button.setAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFont(font);
        button.setActionCommand(commandString);

        return button;
    }   

    @Override
    public void actionPerformed(ActionEvent arg0) 
    {		
        try
        {
            String action = arg0.getActionCommand();
            if (action.contains("Finished"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }	
            else if (action.equalsIgnoreCase("ComboBoxChanged"))
            {
                String dateStr = (String)cbDate.getSelectedItem();
                Date newMapDate = DateUtils.getDateWithValidityCheck(dateStr);
                setMapDate(newMapDate);
                infoMapPanel.setData();
            }
            else if (action.startsWith(MAP_DELIMITER))
            {
                int indexOfMapName = MAP_DELIMITER.length();
                String mapName = action.substring(indexOfMapName);
                FrontMapIdentifier mapIdentifier = FrontMapIdentifier.getFrontMapIdentifierForName(mapName);
                PWCGContext.getInstance().changeContext(mapIdentifier);
                
                setDateSelectionsByPossibleStartDatesAndMovingFront();
                
                Date newMapDate = PWCGContext.getInstance().getCurrentMap().getFrontDatesForMap().getEarliestMapDate();
                setMapDate(newMapDate);
                infoMapPanel.setData();

                centerMapAt(null);
            }
            
            infoMapPanel.setWhatToDisplay(InfoMapPanel.DISPLAY_AIRFIELDS, displayAirfields.isSelected());
            infoMapPanel.setWhatToDisplay(InfoMapPanel.DISPLAY_TOWNS, displayTowns.isSelected());
            infoMapPanel.setWhatToDisplay(InfoMapPanel.DISPLAY_RAILROADS, displayRailroadStations.isSelected());
            infoMapPanel.setWhatToDisplay(InfoMapPanel.DISPLAY_BRIDGES, displayBridges.isSelected());
            
            infoMapPanel.setWhatToDisplay(InfoMapPanel.DISPLAY_FIGHTER, displayFighterSquadrons.isSelected());
            infoMapPanel.setWhatToDisplay(InfoMapPanel.DISPLAY_ATTACK, displayAttackSquadrons.isSelected());
            infoMapPanel.setWhatToDisplay(InfoMapPanel.DISPLAY_BOMBER, displayBomberSquadrons.isSelected());
            infoMapPanel.setWhatToDisplay(InfoMapPanel.DISPLAY_RECON, displayReconSquadrons.isSelected());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);

            try
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            catch (Exception e2)
            {
                PWCGLogger.logException(e2);
            }
        }
    }

    public void refreshSquadronPlacement() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        squadronManager.initialize();
        infoMapPanel.setData();
    }
}
