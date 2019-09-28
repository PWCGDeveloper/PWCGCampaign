package pwcg.gui.rofmap.brief;

import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuLanding;
import pwcg.mission.mcu.McuTakeoff;
import pwcg.mission.mcu.McuWaypoint;

public class BriefParametersContextBuilder
{
	private Mission mission;
	private BriefingFlightParameters briefParametersContext = new BriefingFlightParameters();

	public BriefParametersContextBuilder (Mission mission)
	{
		this.mission = mission;
	}
	
	public BriefingFlightParameters buildBriefParametersContext() throws PWCGException
	{
		addTakeoff();
		setWaypoints();
		addLanding();
		
		return briefParametersContext;
	}
	
	private void setWaypoints() throws PWCGException
	{
		Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight(PWCGContextManager.getInstance().getReferencePlayer());
		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint :  playerFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{				
		     addPlayerFlightWaypoint(prevWaypoint, waypoint);

		     if (playerFlight.getFlightType() == FlightTypes.ESCORT)
		     {
		         if (waypoint.getWpAction() == WaypointAction.WP_ACTION_RENDEZVOUS)
		         {
		             updateEscortWaypointsOnMap();
		         }
		     }

		     if (waypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL)
		     {
		    	 addAttackPoint(playerFlight.getTargetCoords().copy());
		     }
		     
		     prevWaypoint = waypoint;
		}
	}

	private void addPlayerFlightWaypoint(McuWaypoint prevWaypoint, McuWaypoint waypoint) throws PWCGException
	{
	    BriefingMapPoint waypointMapPoint = waypointToMapPoint(waypoint);
	    WaypointEditor waypointEditor = createWaypointEditor(prevWaypoint, waypoint);
	    EditorWaypointGroup editorWaypointGroup = new EditorWaypointGroup ();
	    editorWaypointGroup.setWaypointEditor(waypointEditor);
	    editorWaypointGroup.setWaypointInBriefing(waypoint);
	    editorWaypointGroup.setBriefingMapPoint(waypointMapPoint);
	    briefParametersContext.appendEditorGroup(editorWaypointGroup);
	}

    private WaypointEditor createWaypointEditor(McuWaypoint previousWP, McuWaypoint thisWP) throws PWCGException
    {
        WaypointEditor wpEdit = new WaypointEditor();
        wpEdit.initializeWPEdit(previousWP, thisWP);
        return wpEdit;
    }
    

	private BriefingMapPoint waypointToMapPoint(McuWaypoint waypoint)
	{
		BriefingMapPoint mapPoint = BriefingMapPointFactory.waypointToMapPoint(waypoint);
		return mapPoint;
	}

	public void addTakeoff() throws PWCGException 
	{
		Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight(PWCGContextManager.getInstance().getReferencePlayer());
		McuTakeoff takeoffEntity = playerFlight.getTakeoff();
		if (takeoffEntity != null)
		{
	        BriefingMapPoint takeoff = BriefingMapPointFactory.createTakeoff(mission);

			EditorWaypointGroup editorWaypointGroup = new EditorWaypointGroup();
			editorWaypointGroup.setBriefingMapPoint(takeoff);
			editorWaypointGroup.setWaypointEditor(null);
			editorWaypointGroup.setWaypointInBriefing(null);
			
			briefParametersContext.appendEditorGroup(editorWaypointGroup);
		}
	}
	
	public void addLanding() throws PWCGException 
	{
		Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight(PWCGContextManager.getInstance().getReferencePlayer());
		McuLanding landingEntity = playerFlight.getLanding();
		if (landingEntity != null)
		{
            BriefingMapPoint landing = BriefingMapPointFactory.createLanding(mission);

			EditorWaypointGroup editorWaypointGroup = new EditorWaypointGroup();
			editorWaypointGroup.setBriefingMapPoint(landing);
			editorWaypointGroup.setWaypointEditor(null);
			editorWaypointGroup.setWaypointInBriefing(null);

			briefParametersContext.appendEditorGroup(editorWaypointGroup);
		}
	}
	
	private void addAttackPoint(Coordinate targetLocation) throws PWCGException 
	{
        BriefingMapPoint target = BriefingMapPointFactory.createAttackPoint(targetLocation);

		EditorWaypointGroup editorWaypointGroup = new EditorWaypointGroup();
		editorWaypointGroup.setBriefingMapPoint(target);
		editorWaypointGroup.setWaypointEditor(null);
		editorWaypointGroup.setWaypointInBriefing(null);
		
		briefParametersContext.appendEditorGroup(editorWaypointGroup);
	}
	
    private void updateEscortWaypointsOnMap() throws PWCGException
    {
		Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight(PWCGContextManager.getInstance().getReferencePlayer());
    	List<McuWaypoint> escortedWaypoints = playerFlight.getLinkedWaypoints().getWaypointsForLeadPlane();
	    if (escortedWaypoints != null)
	    {
	        for (McuWaypoint waypoint : escortedWaypoints)
	        {
	            if (waypoint.getWpAction() == WaypointAction.WP_ACTION_LANDING_APPROACH)
	            {
	                continue;
	            }
	            
	            addEscortPoint(waypoint);
	        }               
	    }
    }
    
    private void addEscortPoint(McuWaypoint escortWaypoint)
	{
		BriefingMapPoint escort = new BriefingMapPoint();
		escort.desc = "Escort";
		escort.coord = escortWaypoint.getPosition().copy();
		escort.editable = false;

		EditorWaypointGroup editorWaypointGroup = new EditorWaypointGroup();
		editorWaypointGroup.setBriefingMapPoint(escort);
		editorWaypointGroup.setWaypointEditor(null);
		editorWaypointGroup.setWaypointInBriefing(null);
		briefParametersContext.appendEditorGroup(editorWaypointGroup);
	}

}
