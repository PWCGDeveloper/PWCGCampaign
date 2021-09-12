package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.rofmap.brief.model.BriefingFlightParameters;
import pwcg.gui.utils.ButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingMapGUI extends MapGUI implements ActionListener, IFlightChanged, IBriefingSquadronSelectedCallback
{
	private static final long serialVersionUID = 1L;

    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private Mission mission;
    private BriefingData briefingData;
    private BriefingFlightChooser briefingFlightChooser;
    private BriefingMapPanel mapPanel;

	public BriefingMapGUI(Campaign campaign, CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper) throws PWCGException  
	{
		super(campaign.getDate());
		
        this.campaignHomeGuiBriefingWrapper =  campaignHomeGuiBriefingWrapper;
        this.briefingData =  BriefingContext.getInstance().getBriefingData();
        this.mission =  briefingData.getMission();

		setLayout(new BorderLayout());		
	}    

	public void makePanels() 
	{
		try
		{
            briefingFlightChooser = new BriefingFlightChooser(mission, this);
            briefingFlightChooser.createBriefingSquadronSelectPanel();

			Color bg = ColorMap.MAP_BACKGROUND;
			setOpaque(false);
			setBackground(bg);
			
            this.add(BorderLayout.WEST, makeNavPanel());           
            this.add(BorderLayout.CENTER, createCenterPanel());
            
            Point initialPosition = findCenterPosition();
            centerMapAt(initialPosition);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private Pane createCenterPanel() throws PWCGException
    {
        Pane briefingMapCenterPanel = new Pane(new BorderLayout());
        createMapPanel();
        briefingMapCenterPanel.add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);

        return briefingMapCenterPanel;
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

    private Point findCenterPosition()
    {
        Coordinate initialPosition = briefingData.getActiveBriefingFlight().getBriefingFlightParameters().getBriefingMapMapPoints().get(0).getPosition();
        Point mapPoint = mapPanel.coordinateToPoint(initialPosition);
        return mapPoint;
    }

    private Pane makeNavPanel() throws PWCGException 
    {
        Pane leftPanel = new Pane(new BorderLayout());
        leftPanel.setOpaque(false);

        Pane buttonPanel = makeButtonPanel();
        leftPanel.add(buttonPanel, BorderLayout.NORTH);
        leftPanel.add(briefingFlightChooser.getFlightChooserPanel(), BorderLayout.CENTER);
        return leftPanel;
    }
    
	private Pane makeButtonPanel() throws PWCGException 
	{
		Pane buttonPanel = new Pane(new BorderLayout());
		buttonPanel.setOpaque(false);

		Pane buttonGrid = new Pane();
		buttonGrid.setLayout(new GridLayout(0,1));
		buttonGrid.setOpaque(false);
	    
        if (mission.isFinalized())
        {
            buttonGrid.add(ButtonFactory.makeDummy());
            Button backToCampaignButton = makeButton("Back to Campaign", "Back to Campaign", "Return to campaign home screen");
            buttonGrid.add(backToCampaignButton);
        }

		buttonGrid.add(ButtonFactory.makeDummy());
        Button scrubMissionButton = makeButton("Scrub Mission", "Scrub Mission", "Scrub this mission and return to campaign home screen");
        buttonGrid.add(scrubMissionButton);

        buttonGrid.add(ButtonFactory.makeDummy());
        Button goBackToBriefingDescriptionButton = makeButton("Back: Briefing", "Back: Briefing", "Go back to briefing description screen");
        buttonGrid.add(goBackToBriefingDescriptionButton);

        buttonGrid.add(ButtonFactory.makeDummy());
        Button goToWaypointEditButton = makeButton("Next: Waypoint", "Next: Waypoint", "Progress to waypoint editor screen");
        buttonGrid.add(goToWaypointEditButton);

        buttonGrid.add(ButtonFactory.makeDummy());
        buttonGrid.add(ButtonFactory.makeDummy());

		buttonPanel.add(buttonGrid, BorderLayout.NORTH);
		
        BriefingMapSquadronSelector squadronSelector = new BriefingMapSquadronSelector(mission, this, briefingData);
        Pane friendlySquadronSelectorPanel = squadronSelector.makeComboBox();
		buttonPanel.add(friendlySquadronSelectorPanel, BorderLayout.CENTER);
		
		return buttonPanel;
	}

    private Button makeButton(String buttonText, String command, String toolTipText) throws PWCGException
    {
        return ButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
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
                scrubMission();
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
        campaignHomeGuiBriefingWrapper.refreshCampaignPage();
        CampaignGuiContextManager.getInstance().backToCampaignHome();
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
        refreshAllPanels();           

    }

    private void refreshAllPanels() throws PWCGException
    {
        this.add(BorderLayout.CENTER, createCenterPanel());
        this.add(BorderLayout.WEST, makeNavPanel());
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
        briefingData.setAiFlightsToDisplay(aiFlightsToDisplay);
        this.add(BorderLayout.CENTER, createCenterPanel());
        
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
            briefingFlightParameters.addBriefingMapMapPointsAtPosition();
            
            refreshMapScreen();
        }
    }

    private void refreshMapScreen()
    {
        this.revalidate();
        this.repaint();
    }
}
