package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.mission.MissionGeneratorHelper;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.rofmap.brief.model.BriefingFlightParameters;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.Mission;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingMapGUI extends MapGUI implements ActionListener, IFlightChanged, IBriefingSquadronSelectedCallback
{
	private static final long serialVersionUID = 1L;

    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private Mission mission;
    private BriefingData briefingData;
    private BriefingPlayerFlightChooser briefingFlightChooser;
    private BriefingMapPanel mapPanel;
    private JPanel centerPanel;
    private JPanel navPanel;
    private JPanel navPanelAiSquadronPanel;
    private Map<Integer, String> selectedAiFlights = new HashMap<>();

	public BriefingMapGUI(Campaign campaign, CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper) throws PWCGException  
	{
		super(campaign.getCampaignMap(), campaign.getDate());
		
        this.campaignHomeGuiBriefingWrapper =  campaignHomeGuiBriefingWrapper;
        this.briefingData =  BriefingContext.getInstance().getBriefingData();
        this.mission =  briefingData.getMission();
        this.mapIdentifier =  mission.getCampaignMap();

		setLayout(new BorderLayout());		
	}    

	public void makePanels() 
	{
		try
		{
            briefingFlightChooser = new BriefingPlayerFlightChooser(mission, this);
            briefingFlightChooser.createBriefingPlayerSquadronSelectPanel();

			Color bg = ColorMap.MAP_BACKGROUND;
			setOpaque(false);
			setBackground(bg);
			
			buildAllPanels();            
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void createCenterPanel() throws PWCGException
    {
        centerPanel = new JPanel(new BorderLayout());
        createMapPanel();
        centerPanel.add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);
    }

    private void createMapPanel() throws PWCGException
    {
        BriefingFlight activeMissionHandler = briefingData.getActiveBriefingFlight();
        
        if (mapPanel != null)
        {
            this.remove(mapPanel);
        }
        
        mapPanel = new BriefingMapPanel(this);
        mapScroll = new MapScroll(mapPanel);  
        mapPanel.setMapBackground(100);

        BriefingMapFlightMapper flightMapper = new BriefingMapFlightMapper(activeMissionHandler, mapPanel);
        flightMapper.mapRequestedFlights();
    }

    private void centerMapAtActiveFlight()
    {
        Coordinate activeFlightPositionPosition = briefingData.getActiveBriefingFlight().getBriefingFlightParameters().getBriefingMapMapPoints().get(0).getPosition();
        Point mapPoint = mapPanel.coordinateToPoint(activeFlightPositionPosition);
        centerMapAt(mapPoint);
    }

    private void makeNavPanel() throws PWCGException 
    {
	    navPanel = new JPanel(new BorderLayout());
	    navPanel.setOpaque(false);

		JPanel buttonGrid = buildMenuButtons();
		navPanel.add(buttonGrid, BorderLayout.NORTH);
		
        buildFriendlySquadronSelect();
        navPanel.add(navPanelAiSquadronPanel, BorderLayout.CENTER);
		
        navPanel.add(briefingFlightChooser.getFlightChooserPanel(), BorderLayout.SOUTH);        
	}

    private JPanel buildMenuButtons() throws PWCGException
    {
        JPanel buttonGrid = new JPanel();
		buttonGrid.setLayout(new GridLayout(0,1));
		buttonGrid.setOpaque(false);
	    
        if (mission.getFinalizer().isFinalized())
        {
            buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
            JButton backToCampaignButton = makeButton("Back to Campaign", "Back to Campaign", "Return to campaign home screen");
            buttonGrid.add(backToCampaignButton);
        }

		buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        JButton scrubMissionButton = makeButton("Scrub Mission", "Scrub Mission", "Scrub this mission and return to campaign home screen");
        buttonGrid.add(scrubMissionButton);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        JButton goBackToBriefingDescriptionButton = makeButton("Back: Briefing", "Back: Briefing", "Go back to briefing description screen");
        buttonGrid.add(goBackToBriefingDescriptionButton);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        JButton goToWaypointEditButton = makeButton("Next: Waypoint", "Next: Waypoint", "Progress to waypoint editor screen");
        buttonGrid.add(goToWaypointEditButton);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        return buttonGrid;
    }

    private void buildFriendlySquadronSelect() throws PWCGException
    {
        BriefingMapAiFlightDisplaySelector squadronSelector = new BriefingMapAiFlightDisplaySelector(mission, this, briefingData, selectedAiFlights);
        navPanelAiSquadronPanel = squadronSelector.makeComboBox();
    }

    private JButton makeButton(String buttonText, String command, String toolTipText) throws PWCGException
    {
        return PWCGButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
    }

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{		
		try
		{
			String action = arg0.getActionCommand();
			
            if (action.equals("Back: Briefing"))
            {
                backToBriefingDescription();
            }
            else if (action.equals("Next: Waypoint"))
            {
                forwardToWaypointEditor();
            }
            else if (action.equals("Back to Campaign"))
            {
                backToCampaign();
            }
            else if (action.equals("Scrub Mission"))
            {
                MissionGeneratorHelper.scrubMission(mission.getCampaign(), campaignHomeGuiBriefingWrapper);
            }
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
    
    private void backToCampaign() throws PWCGException
    {
        campaignHomeGuiBriefingWrapper.refreshCampaignPage();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void backToBriefingDescription() throws PWCGException
    {
        CampaignGuiContextManager.getInstance().popFromContextStack();
        return;
    }

    private void forwardToWaypointEditor() throws PWCGException 
    {
        BriefingEditorScreen waypointEditorScreen = new BriefingEditorScreen(campaignHomeGuiBriefingWrapper);
        waypointEditorScreen.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(waypointEditorScreen);
    }

    @Override
    public void flightChanged(Squadron squadron) throws PWCGException
    {
        if (!isChangedSquadronSameSide(briefingData.getSelectedFlight().getSquadron(), squadron))
        {
            briefingData.clearAiFlightsToDisplay();
        }
        
        briefingData.changeSelectedFlight(squadron.getSquadronId());
        buildAllPanels();           

    }

    private void buildAllPanels() throws PWCGException
    {
        if (navPanel == null)
        {
            makeNavPanel();
            this.add(BorderLayout.WEST, navPanel);           
        }
        else
        {
            navPanel.remove(navPanelAiSquadronPanel);
            buildFriendlySquadronSelect();
            navPanel.add(navPanelAiSquadronPanel, BorderLayout.CENTER);            
        }
        
        if (centerPanel != null)
        {
            this.remove(centerPanel);
        }
        createCenterPanel();
        this.add(BorderLayout.CENTER, centerPanel);

        refreshMapScreen();
        centerMapAtActiveFlight();
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
    public void squadronsSelectedChanged(Map<Integer, String> aiFlightsToDisplay) throws PWCGException
    {
        selectedAiFlights = aiFlightsToDisplay;
        briefingData.setAiFlightsToDisplay(aiFlightsToDisplay);
        buildAllPanels();
        
        refreshMapScreen();
    }

    public void waypointRemovedNotification(long waypointID) throws PWCGException
    {
        if (waypointID != McuWaypoint.NO_WAYPOINT_ID)
        {
            BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
            briefingFlightParameters.removeBriefingMapMapPointsAtPosition();
            
            refreshMapScreen();
        }
    }

    public void waypointAddedNotification(long waypointID) throws PWCGException
    {
        if (waypointID != McuWaypoint.NO_WAYPOINT_ID)
        {
            BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
            briefingFlightParameters.addBriefingMapMapPointsAtPosition(mapIdentifier);
            
            refreshMapScreen();
        }
    }

    private void refreshMapScreen()
    {
        this.revalidate();
        this.repaint();
    }
}
