package pwcg.gui.rofmap.editmap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import javax.swing.SwingConstants;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.utils.CampaignTransitionDates;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class EditorMapGUI extends MapGUI implements ActionListener
{
    private static String MAP_DELIMITER = "Map: ";
    
    private static final long serialVersionUID = 1L;

    private EditorMapPanel editorMapPanel = null;
    private JComboBox<String> cbDate = new JComboBox<String>();
    private ButtonGroup mapButtonGroup = new ButtonGroup();
    private ButtonGroup editModeButtonGroup = new ButtonGroup();
    private FrontLineEditor frontLineCreator = null;

    private JCheckBox displayAirfields = null;
    private JCheckBox displayCities = null;

    public EditorMapGUI(Date mapDate) throws PWCGException  
    {        
        super(mapDate);

        // Default to static front.France map
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
            
            setLeftPanel(makeNavigationPanel());            
            setCenterPanel(createMapPanel());
            setRightPanel(makeSelectionPanel());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError (e.getMessage());
        }
    }

    private JPanel createMapPanel() throws PWCGException, PWCGException
    {
        JPanel infoMapPanel = new JPanel(new BorderLayout());

        editorMapPanel = new EditorMapPanel(this);
        
        mapScroll = new MapScroll(editorMapPanel);  
        editorMapPanel.setMapBackground(100);
                
        infoMapPanel.add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);
        
        editorMapPanel.setData();
        
        frontLineCreator = new FrontLineEditor(editorMapPanel);
        editorMapPanel.setFrontLineCreator(frontLineCreator);
        
        return infoMapPanel;
    }

    private JPanel makeNavigationPanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("CampaignInfoRight.jpg");

        ImageResizingPanel intelNavPanel = new ImageResizingPanel(imagePath);
        intelNavPanel.setLayout(new BorderLayout());
        intelNavPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        // Finished button
        JButton finished = PWCGButtonFactory.makeMenuButton("Finished", "Finished", this);
        buttonPanel.add(finished);
        
        // Editor buttons for using the UI to create new front lines
        makeFrontEditActionButtons(buttonPanel);

        // Spacer
        JLabel spacer1 = PWCGButtonFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer1);

        // Map chooser
        JPanel radioButtonPanel = new JPanel( new GridLayout(0,1));
        radioButtonPanel.setOpaque(false);
        
        // Spacer
        JLabel spacer2 = PWCGButtonFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer2);

        intelNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return intelNavPanel;
    }

    private void makeFrontEditActionButtons(JPanel buttonPanel) throws PWCGException
    {
        JButton cancel = PWCGButtonFactory.makeMenuButton("Cancel", "Cancel", this);
        buttonPanel.add(cancel);
        
        JButton write = PWCGButtonFactory.makeMenuButton("Write", "Write", this);
        buttonPanel.add(write);
        
        JButton refresh = PWCGButtonFactory.makeMenuButton("Refresh", "Refresh", this);
        buttonPanel.add(refresh);
        
        JButton mirror = PWCGButtonFactory.makeMenuButton("Mirror", "Mirror", this);
        buttonPanel.add(mirror);
    }

    public JPanel makeSelectionPanel() throws PWCGException 
    {
        String imagePath = getSideImageMain("CampaignInfoLeft.jpg");

        ImageResizingPanel selectionPanel = new ImageResizingPanel(imagePath);
        selectionPanel.setLayout(new GridLayout(0,1));
        selectionPanel.setOpaque(false);

        // The date selection box
        JPanel datePanel = createDateSelection(selectionPanel);
        selectionPanel.add(datePanel);

        // Map buttons
        JPanel buttonPanelGrid = makeMapCheckBoxes();
        selectionPanel.add(buttonPanelGrid);

        JPanel groupButtonPanel = makeGroundStructureCheckBoxes();
        selectionPanel.add(groupButtonPanel);
        
        makeFrontEditSelectionButtons(selectionPanel);

        return selectionPanel;
    }

    /**
     * @param selectionPanel
     * @throws PWCGException
     */
    private void makeFrontEditSelectionButtons(ImageResizingPanel selectionPanel) throws PWCGException
    {
        JPanel editButtonPanel = new JPanel( new GridLayout(0,1));
        editButtonPanel.setOpaque(false);
        editButtonPanel.add(makeRadioButton("Edit Front", "Edit Mode:EditFront", editModeButtonGroup));
        editButtonPanel.add(makeRadioButton("Add Front", "Edit Mode:AddFront", editModeButtonGroup));
        editButtonPanel.add(makeRadioButton("Delete Front", "Edit Mode:DeleteFront", editModeButtonGroup));
        editButtonPanel.add(makeRadioButton("Create Front", "Edit Mode:CreateFront", editModeButtonGroup));
        editButtonPanel.add(makeRadioButton("Edit City", "Edit Mode:EditCity", editModeButtonGroup));
        editButtonPanel.add(makeRadioButton("No Edit", "Edit Mode:EditNone", editModeButtonGroup));
        selectionPanel.add(editButtonPanel);
    }

    /**
     * @param selectionPanel
     * @throws PWCGException
     * @throws PWCGException
     */
    private JPanel createDateSelection(JPanel selectionPanel) throws PWCGException, PWCGException
    {
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setOpaque(false);
        
        JPanel dateGrid = new JPanel( new GridLayout(0,1));
        dateGrid.setOpaque(false);
        
        datePanel.add(dateGrid, BorderLayout.NORTH);
        
        JLabel dateLabel = PWCGButtonFactory.makeMenuLabelLarge("Choose Date");
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

        JLabel mapLabel = PWCGButtonFactory.makeMenuLabelLarge("Choose Map");
        mapGrid.add(mapLabel);
        
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            mapGrid.add(makeRadioButton(PWCGMap.MOSCOW_MAP_NAME, MAP_DELIMITER + PWCGMap.MOSCOW_MAP_NAME, mapButtonGroup));
            mapGrid.add(makeRadioButton(PWCGMap.STALINGRAD_MAP_NAME, MAP_DELIMITER + PWCGMap.STALINGRAD_MAP_NAME, mapButtonGroup));
            mapGrid.add(makeRadioButton(PWCGMap.KUBAN_MAP_NAME, MAP_DELIMITER + PWCGMap.KUBAN_MAP_NAME, mapButtonGroup));
            mapGrid.add(makeRadioButton(PWCGMap.BODENPLATTE_MAP_NAME, MAP_DELIMITER + PWCGMap.BODENPLATTE_MAP_NAME, mapButtonGroup));
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            mapGrid.add(makeRadioButton(PWCGMap.ARRAS_MAP_NAME, MAP_DELIMITER + PWCGMap.ARRAS_MAP_NAME, mapButtonGroup));
        }
        else
        {
            throw new PWCGException("No valid product selected");
        }

        return mapPanel;
    }

    private void setDateSelectionsByPossibleStartDatesAndMovingFront() throws PWCGException, PWCGException
    {
        cbDate.removeAll();        

        CampaignTransitionDates campaignTransitionDates = new CampaignTransitionDates();
        List<String> newDateStrings = campaignTransitionDates.getCampaignTransitionDates();
        
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>((String[])newDateStrings.toArray( new String[newDateStrings.size()] ));
        
        cbDate.setModel(model);
    }

    private JPanel makeGroundStructureCheckBoxes() throws PWCGException
    {
        JPanel groundStructurePanel = new JPanel(new BorderLayout());
        groundStructurePanel.setOpaque(false);
         
        JPanel groundStructureGrid = new JPanel( new GridLayout(0,1));
        groundStructureGrid.setOpaque(false);

        groundStructurePanel.add(groundStructureGrid, BorderLayout.NORTH);

        JLabel spaceLabel1 = PWCGButtonFactory.makePaperLabelMedium(" ");
        groundStructureGrid.add(spaceLabel1);
        
        JLabel groundStructureLabel = PWCGButtonFactory.makeMenuLabelLarge("Choose Category");
        groundStructureGrid.add(groundStructureLabel);

        displayAirfields = makeCheckBoxButton("Airfields", "Airfields");
        groundStructureGrid.add(displayAirfields);

        displayCities = makeCheckBoxButton("Cities", "Cities");
        groundStructureGrid.add(displayCities);        

        JLabel spaceLabel2 = PWCGButtonFactory.makePaperLabelMedium(" ");
        groundStructureGrid.add(spaceLabel2);
        
        return groundStructurePanel;
    }

    private JRadioButton makeRadioButton(String buttonText, String commandString, ButtonGroup buttonGroup) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Color bgColor = ColorMap.CHALK_BACKGROUND;

        Font font = MonitorSupport.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFont(font);
        button.setActionCommand(commandString);

        buttonGroup.add(button);

        return button;
    }	

    private JCheckBox makeCheckBoxButton(String buttonText, String commandString) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Color bgColor = ColorMap.CHALK_BACKGROUND;

        Font font = MonitorSupport.getPrimaryFont();

        JCheckBox button = new JCheckBox(buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFont(font);
        button.setActionCommand(commandString);

        return button;
    }   

    private void writeData() throws PWCGException 
    {
        FrontLineWriter frontLineWriter = new FrontLineWriter(frontLineCreator.getUserCreatedFrontLines());
        frontLineWriter.finished();
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
            else if (action.contains("Cancel"))
            {				
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }	
            else if (action.contains("Write"))
            {               
                writeData();
            }   
            else if (action.contains("Refresh"))
            {               
                PWCGContext.getInstance().initializeMap();
                editorMapPanel.resetFromActual();
            }   
            else if (action.contains("Mirror"))
            {               
                editorMapPanel.mirrorAlliedFrontLinesForAxis();
            }   
            else if (action.equalsIgnoreCase("ComboBoxChanged"))
            {
                String dateStr = (String)cbDate.getSelectedItem();
                
                Date newMapDate = DateUtils.getDateWithValidityCheck(dateStr);
                setMapDate(newMapDate);
                editorMapPanel.setData();
                
                buildFrontLines();
            }
            else if (action.startsWith(MAP_DELIMITER))
            {
                int indexOfMapName = MAP_DELIMITER.length();
                String mapName = action.substring(indexOfMapName);
                FrontMapIdentifier mapIdentifier = PWCGMap.getFrontMapIdentifierForName(mapName);
                PWCGContext.getInstance().changeContext(mapIdentifier);
                
                setDateSelectionsByPossibleStartDatesAndMovingFront();
                
                Date newMapDate = PWCGContext.getInstance().getCurrentMap().getFrontDatesForMap().getEarliestMapDate();
                setMapDate(newMapDate);
                editorMapPanel.setData();
                
                buildFrontLines();

                centerMapAt(null);
            }
            else if (action.startsWith("Edit Mode:"))
            {
                if (action.contains("CreateFront"))
                {
                    editorMapPanel.setEditMode(EditorMapPanel.EDIT_MODE_CREATE_NEW_FRONT);
                }
                else if (action.contains("Mode:AddFront"))
                {
                    editorMapPanel.setEditMode(EditorMapPanel.EDIT_MODE_ADD_FRONT);
                }
                else if (action.contains("Mode:DeleteFront"))
                {
                    editorMapPanel.setEditMode(EditorMapPanel.EDIT_MODE_DELETE_FRONT);
                }
                else if (action.contains("EditFront"))
                {
                    editorMapPanel.setEditMode(EditorMapPanel.EDIT_MODE_EDIT_FRONT);
                }
                else if (action.contains("EditCity"))
                {
                    editorMapPanel.setEditMode(EditorMapPanel.EDIT_MODE_ADD_MAP_LOCATIONS);
                }
                else if (action.contains("EditNone"))
                {
                    editorMapPanel.setEditMode(EditorMapPanel.EDIT_MODE_NONE);
                }
            }
            
            editorMapPanel.setWhatToDisplay(EditorMapPanel.DISPLAY_AIRFIELDS, displayAirfields.isSelected());
            editorMapPanel.setWhatToDisplay(EditorMapPanel.DISPLAY_CITIES, displayCities.isSelected());
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

    private void buildFrontLines() throws PWCGException
    {
        List<FrontLinePoint> alliedLines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(mapDate).getFrontLines(Side.ALLIED);
        List<FrontLinePoint> axisLines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(mapDate).getFrontLines(Side.AXIS);
        List<FrontLinePoint> allFrontLines = new ArrayList<>();
        allFrontLines.addAll(alliedLines);
        allFrontLines.addAll(axisLines);
        
        frontLineCreator.setFromMap(allFrontLines);
    }
}
