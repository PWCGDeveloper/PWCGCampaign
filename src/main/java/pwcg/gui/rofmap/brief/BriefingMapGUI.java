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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.Mission;
import pwcg.mission.Unit;
import pwcg.mission.briefing.BriefingMissionHandler;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.escort.VirtualEscortFlight;
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
public class BriefingMapGUI extends MapGUI implements ActionListener
{
	private static final long serialVersionUID = 1L;
    private CampaignHomeGUI campaignHomeGui = null;

    private JComboBox<String> cbFuel = null;
    private JComboBox<String> cbMissionTime = null;
    private JPanel editorPanel = null;

    private BriefingMissionHandler briefingMissionHandler = null;

	public BriefingMapGUI(CampaignHomeGUI campaignHomeGui, BriefingMissionHandler briefingMissionHandler, Date mapDate) throws PWCGException  
	{
		super(mapDate);
		
        this.campaignHomeGui =  campaignHomeGui;
        this.briefingMissionHandler =  briefingMissionHandler;
        briefingMissionHandler.loadMissionParams();

		setLayout(new BorderLayout());		
	}    

	public void makePanels() 
	{
		try
		{
			Color bg = ColorMap.MAP_BACKGROUND;
			setSize(200, 200);
			setOpaque(false);
			setBackground(bg);
			
            setCenterPanel(createCenterPanel());
            createMissionEditPanel();
            setRightPanel(editorPanel);
            setLeftPanel(makeButtonPanel());           
            
            updateWaypointsOnMap();

		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel createCenterPanel() throws PWCGException
    {
        JPanel briefingMapCenterPanel = new JPanel(new BorderLayout());

        createMapPanel();
        
        briefingMapCenterPanel.add(mapScroll.getMapScrollPane());
        
        return briefingMapCenterPanel;
    }

    private void createMapPanel() throws PWCGException
    {
        BriefingMapPanel mapPanel = new BriefingMapPanel(this, briefingMissionHandler.getBriefParametersContext());
        mapScroll = new MapScroll(mapPanel);  
        mapPanel.setMapBackground(100);


        add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);

        // Center the map at the first WP. 
        Point initialPosition = null;
        for  (EditorWaypointGroup editorWaypointGroup : briefingMissionHandler.getBriefParametersContext().getWaypointEditorGroups())
        {
        	if (editorWaypointGroup.getWaypointInBriefing() != null)
        	{
        		initialPosition = mapPanel.coordinateToPoint(editorWaypointGroup.getWaypointInBriefing().getPosition());
        		break;
        	}
        }
        
        // For debug: plot likely encounter points
        ConfigManagerCampaign configManager = PWCGContextManager.getInstance().getCampaign().getCampaignConfigManager();
        int showAllFlightsInBreifingKey = configManager.getIntConfigParam(ConfigItemKeys.ShowAllFlightsInBreifingKey);
        if (showAllFlightsInBreifingKey == 1)
        {
            mapFlights();
            mapFlightBox();
        }
        
        centerMapAt(initialPosition);
    }

	private JPanel makeButtonPanel() throws PWCGException 
	{
        String imagePath = getSideImage("BriefingNav.jpg");

		ImageResizingPanel buttonPanel = new ImageResizingPanel(imagePath);
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setOpaque(false);

		JPanel buttonGrid = new JPanel();
		buttonGrid.setLayout(new GridLayout(0,1));
		buttonGrid.setOpaque(false);
		    
        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Scrub Mission");

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Briefing Description");

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Pilot Selection");
		
		buttonPanel.add(buttonGrid, BorderLayout.NORTH);
		
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
    	//briefingMissionHandler.loadMissionParams();
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
        // JPanel panel = new JPanel(gridLayout);
        JPanel dropDownPanel = new JPanel(new GridLayout(0,1));
        dropDownPanel.setOpaque(false);
        
        createFuelDisplay();
        dropDownPanel.add(cbFuel);

        // Time CB
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
		
		// JPanel panel = new JPanel(gridLayout);
		JPanel waypointPanel = new JPanel(gridBagLayout);
		waypointPanel.setOpaque(false);

		createMissionParametersHeader(constraints, waypointPanel);
	    
	    for (WaypointEditor wpEditor : briefingMissionHandler.getBriefParametersContext().getWaypointEditorsInBriefing())
	    {
			if (briefingMissionHandler.getMission().isFinalized())
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
        
        // Looks nicer NORTH but need CENTER for scroll bars
        if (briefingMissionHandler.getBriefParametersContext().getNumWaypoints() > 15)
        {
            waypointBorderPanel.add(waypointScrollPane, BorderLayout.CENTER);
        }
        else
        {
            waypointBorderPanel.add(waypointScrollPane, BorderLayout.NORTH);
        }
        
	    return waypointBorderPanel;
    }


    /**
     * @param constraints
     */
    private void createTimeDisplay()
    {
        MissionTime missionTime = PWCGContextManager.getInstance().getCurrentMap().getMissionOptions().getMissionTime();
		
        cbMissionTime = new JComboBox<String>();
        cbMissionTime.setActionCommand("ChangeTime");

        for (String time : missionTime.getMissionTimes())
        {
            cbMissionTime.addItem(time);
        }
        cbMissionTime.setOpaque(false);
        cbMissionTime.setSelectedIndex(missionTime.getIndexForTime());
        String selectedTime = (String)cbMissionTime.getSelectedItem();
        briefingMissionHandler.getBriefParametersContext().setSelectedTime(selectedTime);
        
        cbMissionTime.addActionListener(this);
        if (briefingMissionHandler.getMission().isFinalized())
        {
            cbMissionTime.setEnabled(false);
        }
    }


    private void createFuelDisplay()
    {
        // Fuel CB
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
		cbFuel.setOpaque(false);
		cbFuel.setSelectedIndex(getIndexForFuel());
		cbFuel.setActionCommand("ChangeFuel");
		cbFuel.addActionListener(this);
		if (briefingMissionHandler.getMission().isFinalized())
		{
			cbFuel.setEnabled(false);
		}
    }


    /**
     * @param constraints
     * @param panel
     * @throws PWCGException 
     */
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
	
	/**
	 * @return
	 */
	private int getIndexForFuel()
	{
	    int index = 0;
	    
	    double selectedFuel = briefingMissionHandler.getBriefParametersContext().getSelectedFuel();
	    
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
        else
        {
            index = 8;
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
                briefingMissionHandler.getBriefParametersContext().setSelectedTime(selectedTime);
            }
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void scrubMission() throws PWCGException
    {
        Campaign campaign  = PWCGContextManager.getInstance().getCampaign();
        campaign.setCurrentMission(null);
        
        campaignHomeGui.clean();
        campaignHomeGui.createPilotContext();

        campaignHomeGui.enableButtonsAsNeeded();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void backToBriefingDescription() throws PWCGException
    {
        briefingMissionHandler.getBriefParametersContext().synchronizeAltitudeEdits();
        briefingMissionHandler.updateMissionBriefingParameters();

        CampaignGuiContextManager.getInstance().popFromContextStack();
        return;
    }

    private void forwardToPilotSelection() throws PWCGException
    {
        briefingMissionHandler.getBriefParametersContext().synchronizeAltitudeEdits();
        briefingMissionHandler.updateMissionBriefingParameters();
        
        BriefingPilotPanelSet pilotSelection = new BriefingPilotPanelSet(campaignHomeGui,  briefingMissionHandler);
        pilotSelection.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(pilotSelection);
    }

    private void changeFuel()
    {
        String fuelString = (String)cbFuel.getSelectedItem();
        int beginIndex = fuelString.indexOf(' ');
        int endIndex = fuelString.indexOf('%');
        String valueString = fuelString.substring(beginIndex+1, endIndex);
        int valueAsInt = new Integer (valueString);
        Double selectedFuel = new Double (valueAsInt).doubleValue() / 100.0;
        
        briefingMissionHandler.getBriefParametersContext().setSelectedFuel(selectedFuel);
    }

    private void mapFlights() throws PWCGException  
    {    
        Mission mission = briefingMissionHandler.getMission();
        
        BriefingMapPanel mapPanel = (BriefingMapPanel)mapScroll.getMapPanel();
        
        mapPanel.clearVirtualPoints();
        
        Flight myFlight = mission.getMissionFlightBuilder().getPlayerFlight();
        for (Unit unit : myFlight.getLinkedUnits())
        {
            if (unit instanceof Flight)
            {
                mapPanel.makeMapPanelVirtualPoints ((Flight)unit);
            }
        }
        
        for (Flight flight : mission.getMissionFlightBuilder().getMissionFlights())
        {
            mapPanel.makeMapPanelVirtualPoints (flight);
            
            for (Unit unit : flight.getLinkedUnits())
            {
                if (unit instanceof Flight)
                {
                    if (!(unit instanceof VirtualEscortFlight))
                    {
                        mapPanel.makeMapPanelVirtualPoints ((Flight)unit);
                    }
                }
            }
        }
    }


    private void mapFlightBox() throws PWCGException  
    {    
        CoordinateBox missionBorders = briefingMissionHandler.getMission().getMissionFlightBuilder().getMissionBorders(5000);
        BriefingMapPanel mapPanel = (BriefingMapPanel)mapScroll.getMapPanel();
        mapPanel.setMissionBorders(missionBorders);
    }

    public BriefingMissionHandler getBriefingMissionHandler()
    {
        return briefingMissionHandler;
    }
}
