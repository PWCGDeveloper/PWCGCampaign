package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.helper.BriefingMissionFlight;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.Mission;
import pwcg.mission.utils.MissionTime;

/**
 * 1. Start - initialize mission parameters and editors
 * 2. Move waypoint
 *      Update waypoint list
 *      Change distances in edit field0
 *      Update map
 * 3. Add waypoint
 *      Clone next WP
 *      Add new WaypointEditorCouplet
 *      Update map
 * 3. Remove waypoint
 *      Remove WaypointEditorCouplet
 *      Update map
 * 4. Edit altitude
 *      Update WaypointEditorCouplet
 * 
 * @author Admin
 *
 */
public class BriefingMapGUI extends MapGUI implements ActionListener, IFlightChanged, IBriefingSquadronSelectedCallback
{
	private static final long serialVersionUID = 1L;
    private CampaignHomeGUI campaignHomeGui;

    private JComboBox<String> cbFuel;
    private JComboBox<String> cbMissionTime;
    private JPanel editorPanel;
    private Mission mission;
    private BriefingContext briefingContext;
    private BriefingFlightChooser briefingFlightChooser;
    private Map<Integer, String> selectedSquadrons = new HashMap<>();

	public BriefingMapGUI(CampaignHomeGUI campaignHomeGui, Mission mission, BriefingContext briefingContext, Date mapDate) throws PWCGException  
	{
		super(mapDate);
		
        this.campaignHomeGui =  campaignHomeGui;
        this.mission =  mission;
        this.briefingContext =  briefingContext;

		setLayout(new BorderLayout());		
	}    

	public void makePanels() 
	{
		try
		{
            briefingFlightChooser = new BriefingFlightChooser(mission, this);
            briefingFlightChooser.createBriefingSquadronSelectPanel();

			Color bg = ColorMap.MAP_BACKGROUND;
			setSize(200, 200);
			setOpaque(false);
			setBackground(bg);
			
            setCenterPanel(createCenterPanel());
            createMissionEditPanel();
            setRightPanel(editorPanel);
            setLeftPanel(makeLeftPanel());           
            
            updateWaypointsOnMap();

		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel createCenterPanel() throws PWCGException
    {
        JPanel briefingMapCenterPanel = new JPanel(new BorderLayout());

        createMapPanel();
        briefingMapCenterPanel.add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);
        
        return briefingMapCenterPanel;
    }

    private void createMapPanel() throws PWCGException
    {
        BriefingMissionFlight activeMissionHandler = briefingContext.getActiveBriefingHandler();
        
        BriefingMapPanel mapPanel = new BriefingMapPanel(this, activeMissionHandler.getBriefingFlightParameters(), mission, selectedSquadrons);
        mapScroll = new MapScroll(mapPanel);  
        mapPanel.setMapBackground(100);

        add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);

        // Center the map at the first WP. 
        Point initialPosition = null;
        for  (EditorWaypointGroup editorWaypointGroup : activeMissionHandler.getBriefingFlightParameters().getWaypointEditorGroups())
        {
        	if (editorWaypointGroup.getWaypointInBriefing() != null)
        	{
        		initialPosition = mapPanel.coordinateToPoint(editorWaypointGroup.getWaypointInBriefing().getPosition());
        		break;
        	}
        }

        BriefingMapFlightMapper flightMapper = new BriefingMapFlightMapper(activeMissionHandler, mapPanel);
        flightMapper.mapRequestedFlights();
        
        centerMapAt(initialPosition);
    }

    private JPanel makeLeftPanel() throws PWCGException 
    {
        String imagePath = getSideImage(campaignHomeGui.getCampaign(), "BriefingNav.jpg");
        ImageResizingPanel leftPanel = new ImageResizingPanel(imagePath);
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setOpaque(false);

        JPanel buttonPanel = makeButtonPanel();
        leftPanel.add(buttonPanel, BorderLayout.NORTH);
        leftPanel.add(briefingFlightChooser.getFlightChooserPanel(), BorderLayout.CENTER);
        return leftPanel;
    }
    
	private JPanel makeButtonPanel() throws PWCGException 
	{
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setOpaque(false);

		JPanel buttonGrid = new JPanel();
		buttonGrid.setLayout(new GridLayout(0,1));
		buttonGrid.setOpaque(false);
	    
        if (mission.isFinalized())
        {
            buttonGrid.add(PWCGButtonFactory.makeDummy());
            makeButton(buttonGrid, "Back to Campaign");
        }

		buttonGrid.add(PWCGButtonFactory.makeDummy());
		makeButton(buttonGrid, "Scrub Mission");

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Briefing Description");

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Pilot Selection");

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        buttonGrid.add(PWCGButtonFactory.makeDummy());
        buttonGrid.add(PWCGButtonFactory.makeDummy());

		buttonPanel.add(buttonGrid, BorderLayout.NORTH);
		
        BriefingMapSquadronSelector squadronSelector = new BriefingMapSquadronSelector(mission, this, briefingContext);
        JPanel friendlySquadronSelectorPanel = squadronSelector.makeComboBox();
		buttonPanel.add(friendlySquadronSelectorPanel, BorderLayout.CENTER);
		
		return buttonPanel;
	}

    private JButton makeButton(JPanel buttonPanel, String buttonText) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, buttonText, this);
        buttonPanel.add(button);
		
		return button;
    }

	public void waypointChangedNotification() throws PWCGException  
    {       
        createMissionEditPanel();
        
        setRightPanel(editorPanel);
        
        updateWaypointsOnMap();
    }

    private void updateWaypointsOnMap() throws PWCGException
    {
        BriefingMissionFlight briefingMissionHandler = briefingContext.getActiveBriefingHandler();
    	briefingMissionHandler.loadMissionParams(briefingMissionHandler.getFlight());
    }


	private void createMissionEditPanel() throws PWCGException 
	{
        String imagePath = ContextSpecificImages.imagesMisc() + "PaperPart.jpg";

		editorPanel = new ImageResizingPanel(imagePath);
		editorPanel.setLayout(new BorderLayout());
		editorPanel.setOpaque(false);

		JPanel editablePanel = createEditablePanel();

        JPanel editableLabelPanel = createEditableLabelPanel();

        editorPanel.add(editableLabelPanel, BorderLayout.NORTH);
        editorPanel.add(editablePanel, BorderLayout.CENTER);
	}

    private JPanel createEditableLabelPanel() throws PWCGException
    {
        JPanel editableLabelPanel = new JPanel(new GridLayout(0,1));
        editableLabelPanel.setOpaque(false);
        
        JLabel summaryLabel = PWCGButtonFactory.makePaperLabelLarge("Mission Summary");
        editableLabelPanel.add(summaryLabel);
        
        JLabel spacer = PWCGButtonFactory.makePaperLabelLarge("  ");
        editableLabelPanel.add(spacer);
        
        return editableLabelPanel;
    }

    private JPanel createEditablePanel() throws PWCGException
    {
        JPanel editablePanel = new JPanel(new BorderLayout());
        editablePanel.setOpaque(false);

        JPanel waypointPanel = createWaypointPanel();
        editablePanel.add(waypointPanel, BorderLayout.CENTER);

        JPanel dropDownPanel = createDropDownPanel();
        editablePanel.add(dropDownPanel, BorderLayout.NORTH);
        
        return editablePanel;
    }

    private JPanel createDropDownPanel() throws PWCGException
    {
        JPanel dropDownPanel = new JPanel(new GridLayout(0,1));
        dropDownPanel.setOpaque(false);
        
        createFuelDisplay();
        dropDownPanel.add(cbFuel);

        createTimeDisplay();
        dropDownPanel.add(cbMissionTime);
        
        return dropDownPanel;
    }

    private JPanel createWaypointPanel() throws PWCGException
    {        
        JPanel waypointBorderPanel = new JPanel(new BorderLayout());
        waypointBorderPanel.setOpaque(false);
        
        GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.ipadx = 3;
		constraints.ipady = 3;
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		JPanel waypointPanel = new JPanel(gridBagLayout);
		waypointPanel.setOpaque(false);

		createMissionParametersHeader(constraints, waypointPanel);
	    
        BriefingMissionFlight activeMissionHandler = briefingContext.getActiveBriefingHandler();
	    for (WaypointEditor wpEditor : activeMissionHandler.getBriefingFlightParameters().getWaypointEditorsInBriefing())
	    {
			if (mission.isFinalized())
			{
			    wpEditor.setEnabled(false);
			}
	    	
	        constraints.gridy = constraints.gridy + 1;

			constraints.gridx = 0;
			waypointPanel.add(wpEditor.getDesc(), constraints);
			
			constraints.gridx = 1;
			waypointPanel.add(wpEditor.getAltitudeSetting(), constraints);
            
            constraints.gridx = 2;
            waypointPanel.add(wpEditor.getDistance(), constraints);
        
            constraints.gridx = 3;
            waypointPanel.add(wpEditor.getHeading(), constraints);
	    }	    
	    
        JScrollPane waypointScrollPane = ScrollBarWrapper.makeScrollPane(waypointPanel);
        
        if (activeMissionHandler.getBriefingFlightParameters().getNumWaypoints() > 15)
        {
            waypointBorderPanel.add(waypointScrollPane, BorderLayout.CENTER);
        }
        else
        {
            waypointBorderPanel.add(waypointScrollPane, BorderLayout.NORTH);
        }
        
	    return waypointBorderPanel;
    }

    private void createTimeDisplay()
    {
        MissionTime missionTime = PWCGContext.getInstance().getCurrentMap().getMissionOptions().getMissionTime();
		
        cbMissionTime = new JComboBox<String>();
        cbMissionTime.setActionCommand("ChangeTime");

        for (String time : missionTime.getMissionTimes())
        {
            cbMissionTime.addItem(time);
        }
        cbMissionTime.setOpaque(false);
        cbMissionTime.setSelectedIndex(missionTime.getIndexForTime());
        String selectedTime = (String)cbMissionTime.getSelectedItem();

        briefingContext.getBriefingMissionParameters().setSelectedTime(selectedTime);
        
        cbMissionTime.addActionListener(this);
        if (mission.isFinalized())
        {
            cbMissionTime.setEnabled(false);
        }
    }

    private void createFuelDisplay()
    {
		cbFuel = new JComboBox<String>();
		cbFuel.addItem("Fuel 100%");
		cbFuel.addItem("Fuel 95%");
		cbFuel.addItem("Fuel 90%");
		cbFuel.addItem("Fuel 85%");
		cbFuel.addItem("Fuel 80%");
		cbFuel.addItem("Fuel 75%");
		cbFuel.addItem("Fuel 70%");
		cbFuel.addItem("Fuel 65%");
        cbFuel.addItem("Fuel 60%");
        cbFuel.addItem("Fuel 55%");
        cbFuel.addItem("Fuel 50%");
        cbFuel.addItem("Fuel 45%");
        cbFuel.addItem("Fuel 40%");
		cbFuel.setOpaque(false);
		cbFuel.setSelectedIndex(getIndexForFuel());
		cbFuel.setActionCommand("ChangeFuel");
		cbFuel.addActionListener(this);
		if (mission.isFinalized())
		{
			cbFuel.setEnabled(false);
		}
    }

    private void createMissionParametersHeader(GridBagConstraints constraints, JPanel panel) throws PWCGException
    {
        Font font = MonitorSupport.getPrimaryFontSmall();

        JLabel wpName = new JLabel ("WP");		
        wpName.setFont(font);
        
		wpName.setHorizontalAlignment(JLabel.CENTER);
	    constraints.weightx = 0.15;
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(wpName, constraints);
		
		JLabel altLabel = new JLabel ("Alt (Meters)");
		altLabel.setFont(font);

        altLabel.setHorizontalAlignment(JLabel.CENTER);
	    constraints.weightx = 0.2;
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(altLabel, constraints);
        
        JLabel distLabel = new JLabel ("Dist (Km)");
        distLabel.setFont(font);

        distLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.weightx = 0.2;
        constraints.gridx = 2;
        constraints.gridy = 0;
        panel.add(distLabel, constraints);
        
        JLabel headingLabel = new JLabel ("Heading");
        headingLabel.setFont(font);
        
        headingLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.weightx = 0.2;
        constraints.gridx = 3;
        constraints.gridy = 0;
        panel.add(headingLabel, constraints);
    }

	private int getIndexForFuel()
	{
	    int index = 0;
	    
        BriefingMissionFlight activeMissionHandler = briefingContext.getActiveBriefingHandler();
	    double selectedFuel = activeMissionHandler.getBriefingFlightParameters().getSelectedFuel();
	    
        if (selectedFuel > .95)
        {
            index = 0;
        }
        else if (selectedFuel > .90)
        {
            index = 1;
        }
        else if (selectedFuel > .85)
        {
            index = 2;
        }
        else if (selectedFuel > .80)
        {
            index = 3;
        }
        else if (selectedFuel > .75)
        {
            index = 4;
        }
        else if (selectedFuel > .70)
        {
            index = 5;
        }
        else if (selectedFuel > .65)
        {
            index = 6;
        }
        else if (selectedFuel > .60)
        {
            index = 7;
        }
        else if (selectedFuel > .55)
        {
            index = 8;
        }
        else if (selectedFuel > .50)
        {
            index = 9;
        }
        else if (selectedFuel > .45)
        {
            index = 10;
        }
        else if (selectedFuel > .40)
        {
            index = 11;
        }
        else
        {
            index = 12;
        }
		
		return index;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{		
		try
		{
			String action = arg0.getActionCommand();
			
            if (action.equals("Pilot Selection"))
            {
                forwardToPilotSelection();
            }
            else if (action.equals("Back to Campaign"))
            {
                backToCampaign();
            }
            else if (action.equals("Scrub Mission"))
            {
                scrubMission();
            }
            else if (action.equals("Briefing Description"))
            {
                backToBriefingDescription();
            }
            else if (action.equalsIgnoreCase("ChangeFuel"))
            {
                changeFuel();
            }
            else if (action.equalsIgnoreCase("ChangeTime"))
            {
                String selectedTime = (String)cbMissionTime.getSelectedItem();
                briefingContext.getBriefingMissionParameters().setSelectedTime(selectedTime);
            }
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void scrubMission() throws PWCGException
    {
        Campaign campaign  = PWCGContext.getInstance().getCampaign();
        campaign.setCurrentMission(null);
        
        campaignHomeGui.clean();
        campaignHomeGui.createPilotContext();

        campaignHomeGui.enableButtonsAsNeeded();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }
    
    private void backToCampaign() throws PWCGException
    {
        campaignHomeGui.clean();
        campaignHomeGui.createPilotContext();

        campaignHomeGui.enableButtonsAsNeeded();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void backToBriefingDescription() throws PWCGException
    {
        BriefingMissionFlight activeMissionHandler = briefingContext.getActiveBriefingHandler();
        activeMissionHandler.getBriefingFlightParameters().synchronizeAltitudeEdits();
        briefingContext.updateMissionBriefingParameters();

        CampaignGuiContextManager.getInstance().popFromContextStack();
        return;
    }

    private void forwardToPilotSelection() throws PWCGException
    {
        BriefingMissionFlight activeMissionHandler = briefingContext.getActiveBriefingHandler();
        activeMissionHandler.getBriefingFlightParameters().synchronizeAltitudeEdits();
        briefingContext.updateMissionBriefingParameters();
        
        BriefingPilotPanelSet pilotSelection = new BriefingPilotPanelSet(campaignHomeGui.getCampaign(), campaignHomeGui,  briefingContext, mission);
        pilotSelection.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(pilotSelection);
    }

    private void changeFuel()
    {
        String fuelString = (String)cbFuel.getSelectedItem();
        int beginIndex = fuelString.indexOf(' ');
        int endIndex = fuelString.indexOf('%');
        String valueString = fuelString.substring(beginIndex+1, endIndex);
        int valueAsInt = Integer.valueOf (valueString);
        Double selectedFuel = Double.valueOf (valueAsInt).doubleValue() / 100.0;
        
        BriefingMissionFlight activeMissionHandler = briefingContext.getActiveBriefingHandler();
        activeMissionHandler.getBriefingFlightParameters().setSelectedFuel(selectedFuel);
    }

    @Override
    public void flightChanged(Squadron squadron) throws PWCGException
    {
        if (!isChangedSquadronSameSide(briefingContext.getSelectedFlight().getSquadron(), squadron))
        {
            selectedSquadrons.clear();
        }
        
        briefingContext.changeSelectedFlight(squadron);
        refreshAllPanels();           

    }

    private void refreshAllPanels() throws PWCGException
    {
        createMissionEditPanel();
        setRightPanel(editorPanel);
        setCenterPanel(createCenterPanel());
        setLeftPanel(makeLeftPanel());
    }
    
    private boolean isChangedSquadronSameSide(Squadron before, Squadron after) throws PWCGException
    {
        if (before.determineSide() == after.determineSide())
        {
            return true;
        }
        return false;
    }

    @Override
    public void squadronsSelectedChanged(Map<Integer, String> selectedSquadrons) throws PWCGException
    {
        this.selectedSquadrons = selectedSquadrons;
        setCenterPanel(createCenterPanel());
    }
    
    @Override
    public void refreshScreen() throws PWCGException
    {
        briefingFlightChooser.setSelectedButton(briefingContext.getSelectedFlight().getSquadron().getSquadronId());
        refreshAllPanels();
    }
}
