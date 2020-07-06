package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.Mission;

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
    private CampaignHomeScreen campaignHomeGui;

    private Mission mission;
    private BriefingData briefingData;
    private BriefingMapEditorPanel editorPanel;
    private BriefingFlightChooser briefingFlightChooser;
    private BriefingMapPanel mapPanel;

	public BriefingMapGUI(Campaign campaign, CampaignHomeScreen campaignHomeGui) throws PWCGException  
	{
		super(campaign.getDate());
		
        this.campaignHomeGui =  campaignHomeGui;
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
			setSize(200, 200);
			setOpaque(false);
			setBackground(bg);
			
            this.add(BorderLayout.CENTER, createCenterPanel());
            this.add(BorderLayout.EAST, createMissionEditPanel());
            this.add(BorderLayout.WEST, makeLeftPanel());           
            
            Point initialPosition = findCenterPosition();
            centerMapAt(initialPosition);
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

    private JPanel makeLeftPanel() throws PWCGException 
    {
        JPanel leftPanel = new JPanel(new BorderLayout());
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
		
        BriefingMapSquadronSelector squadronSelector = new BriefingMapSquadronSelector(mission, this, briefingData);
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

	private JPanel createMissionEditPanel() throws PWCGException 
	{
		editorPanel = new BriefingMapEditorPanel(mission, briefingData);
		editorPanel.makePanels();
        return editorPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{		
		try
		{
			String action = arg0.getActionCommand();
			
            if (action.equals("Briefing Description"))
            {
                backToBriefingDescription();
            }
            else if (action.equals("Pilot Selection"))
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
        campaignHomeGui.createCampaignHomeContext();
        CampaignGuiContextManager.getInstance().backToCampaignHome();
    }
    
    private void backToCampaign() throws PWCGException
    {
        campaignHomeGui.createCampaignHomeContext();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void backToBriefingDescription() throws PWCGException
    {
        pushEditsToModel();
        CampaignGuiContextManager.getInstance().popFromContextStack();
        return;
    }

    private void forwardToPilotSelection() throws PWCGException
    {
        pushEditsToModel();
        BriefingPilotSelectionScreen pilotSelection = new BriefingPilotSelectionScreen(campaignHomeGui.getCampaign(), campaignHomeGui,  briefingData, mission);
        pilotSelection.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(pilotSelection);
    }

    @Override
    public void flightChanged(Squadron squadron) throws PWCGException
    {
        pushEditsToModel();
        if (!isChangedSquadronSameSide(briefingData.getSelectedFlight().getSquadron(), squadron))
        {
            briefingData.clearAiFlightsToDisplay();
        }
        
        briefingData.changeSelectedFlight(squadron.getSquadronId());
        refreshAllPanels();           

    }

    private void pushEditsToModel()
    {
        for (BriefingMapPoint briefingMapPoint : briefingData.getActiveBriefingFlight().getBriefingFlightParameters().getBriefingMapMapPoints())
        {
            WaypointEditor editor = editorPanel.getWaypointEditors().getWaypointEditorByid(briefingMapPoint.getWaypointID());
            if (editor != null)
            {
                int altitude = editor.getAltitudeValue();
                briefingMapPoint.setAltitude(altitude);
            }
        }
    }

    private void refreshAllPanels() throws PWCGException
    {
        this.add(BorderLayout.EAST, createMissionEditPanel());
        this.add(BorderLayout.CENTER, createCenterPanel());
        this.add(BorderLayout.WEST, makeLeftPanel());
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
        
        this.revalidate();
        this.repaint();
    }

    public void waypointChangedNotification()
    {
        // TODO trigger refresh here ... ?
    }
}
