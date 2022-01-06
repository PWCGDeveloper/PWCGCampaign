package pwcg.gui.rofmap.infoMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
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
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class InfoMapGUI extends MapGUI implements ActionListener
{
    private static String MAP_DELIMITER = "Map: ";
    
    private static final long serialVersionUID = 1L;

    private InfoMapPanel infoMapPanel = null;
    private JComboBox<String> cbDate = new JComboBox<String>();
    private ButtonGroup mapButtonGroup = new ButtonGroup();

    private JCheckBox displayFighterSquadrons = null;
    private JCheckBox displayAttackSquadrons = null;
    private JCheckBox displayBomberSquadrons = null;
    private JCheckBox displayReconSquadrons = null;
    private JCheckBox displayAirfields = null;
    private JCheckBox displayTowns = null;
    private JCheckBox displayRailroadStations = null;
    private JCheckBox displayBridges = null;

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

    private JPanel createMapPanel() throws PWCGException, PWCGException
    {
        JPanel infoMapPanelCenter = new JPanel(new BorderLayout());

        infoMapPanel = new InfoMapPanel(this);
        
        mapScroll = new MapScroll(infoMapPanel);  
        infoMapPanel.setMapBackground(100);
                
        infoMapPanelCenter.add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);
        
        infoMapPanel.setData();
        

        return infoMapPanelCenter;
    }

    private JPanel makeNavigationPanel() throws PWCGException  
    {
        JPanel infoMapNavPanel = new JPanel(new BorderLayout());
        infoMapNavPanel.setLayout(new BorderLayout());
        infoMapNavPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished with PWCG information map", this);
        buttonPanel.add(finishedButton);
        
        JLabel spacer1 = PWCGLabelFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer1);

        JPanel radioButtonPanel = new JPanel( new GridLayout(0,1));
        radioButtonPanel.setOpaque(false);
        
        JLabel spacer2 = PWCGLabelFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer2);

        infoMapNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return infoMapNavPanel;
    }

    public JPanel makeSelectionPanel() throws PWCGException 
    {
        JPanel selectionPanel = new JPanel(new GridLayout(0,1));
        selectionPanel.setOpaque(false);

        JPanel datePanel = createDateSelection(selectionPanel);
        selectionPanel.add(datePanel);

        JPanel buttonPanelGrid = makeMapCheckBoxes();
        selectionPanel.add(buttonPanelGrid);

        JPanel squadronButtonPanel = makeSquadronTypeCheckBoxes();
        selectionPanel.add(squadronButtonPanel);
        
        JPanel groupButtonPanel = makeGroundStructureCheckBoxes();
        selectionPanel.add(groupButtonPanel);
        
        return selectionPanel;
    }

    private JPanel createDateSelection(JPanel selectionPanel) throws PWCGException, PWCGException
    {
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setOpaque(false);
        
        JPanel dateGrid = new JPanel( new GridLayout(0,1));
        dateGrid.setOpaque(false);
        
        datePanel.add(dateGrid, BorderLayout.NORTH);
        
        JLabel dateLabel = PWCGLabelFactory.makeMenuLabelLarge("Choose Date");
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

    private JPanel makeMapCheckBoxes() throws PWCGException
    {
        JPanel mapPanel = new JPanel(new BorderLayout());
        mapPanel.setOpaque(false);
                
        JPanel mapGrid = new JPanel( new GridLayout(0,1));
        mapGrid.setOpaque(false);
        
        mapPanel.add(mapGrid, BorderLayout.NORTH);

        JLabel mapLabel = PWCGLabelFactory.makeMenuLabelLarge("Choose Map");
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
        else if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            addToMapGrid(mapGrid, FrontMapIdentifier.STALINGRAD_MAP);
        }
        else
        {
            throw new PWCGException("No valid product selected");
        }

        return mapPanel;
    }

    private void addToMapGrid(JPanel mapGrid, FrontMapIdentifier mapIdentifier) throws PWCGException
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

    private JPanel makeGroundStructureCheckBoxes() throws PWCGException
    {
        JPanel groundStructurePanel = new JPanel(new BorderLayout());
        groundStructurePanel.setOpaque(false);
         
        JPanel groundStructureGrid = new JPanel( new GridLayout(0,1));
        groundStructureGrid.setOpaque(false);

        groundStructurePanel.add(groundStructureGrid, BorderLayout.NORTH);

        JLabel spaceLabel1 = PWCGLabelFactory.makePaperLabelMedium(" ");
        groundStructureGrid.add(spaceLabel1);
        
        JLabel groundStructureLabel = PWCGLabelFactory.makeMenuLabelLarge("Choose Category");
        groundStructureGrid.add(groundStructureLabel);

        displayAirfields = makeCheckBoxButton("Airfields", "Airfields");
        groundStructureGrid.add(displayAirfields);

        displayTowns = makeCheckBoxButton("Towns", "Towns");
        groundStructureGrid.add(displayTowns);

        displayRailroadStations = makeCheckBoxButton("Railroad Stations", "Railroad");
        groundStructureGrid.add(displayRailroadStations);

        displayBridges = makeCheckBoxButton("Bridges", "Bridges");
        groundStructureGrid.add(displayBridges);

        JLabel spaceLabel2 = PWCGLabelFactory.makePaperLabelMedium(" ");
        groundStructureGrid.add(spaceLabel2);
        
        return groundStructurePanel;
    }

    private JPanel makeSquadronTypeCheckBoxes() throws PWCGException
    {
        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setOpaque(false);
         
        JPanel roleGrid = new JPanel( new GridLayout(0,1));
        roleGrid.setOpaque(false);

        rolePanel.add(roleGrid, BorderLayout.NORTH);

        JLabel spaceLabel1 = PWCGLabelFactory.makePaperLabelMedium(" ");
        roleGrid.add(spaceLabel1);
        
        JLabel roleLabel = PWCGLabelFactory.makeMenuLabelLarge("Choose Aircraft Role");
        roleGrid.add(roleLabel);

        displayFighterSquadrons = makeCheckBoxButton("Fighter", "Fighter");
        roleGrid.add(displayFighterSquadrons);

        displayAttackSquadrons = makeCheckBoxButton("Attack", "Attack");
        roleGrid.add(displayAttackSquadrons);

        displayBomberSquadrons = makeCheckBoxButton("Bomber", "Bomber");
        roleGrid.add(displayBomberSquadrons);

        displayReconSquadrons = makeCheckBoxButton("Recon", "Recon");
        roleGrid.add(displayReconSquadrons);

        JLabel spaceLabel2 = PWCGLabelFactory.makePaperLabelMedium(" ");
        roleGrid.add(spaceLabel2);
        
        return rolePanel;
    }

    private JRadioButton makeRadioButton(String buttonText, String commandText, ButtonGroup buttonGroup) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JRadioButton button = PWCGButtonFactory.makeRadioButton(buttonText, commandText, "", font, fgColor, false, this);
        buttonGroup.add(button);
        return button;
    }	

    private JCheckBox makeCheckBoxButton(String buttonText, String commandString) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBox(buttonText, commandString, font, ColorMap.CHALK_FOREGROUND, this);
        return checkBox;
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
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        squadronManager.initialize();
        infoMapPanel.setData();
    }
}
