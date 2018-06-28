package pwcg.gui.rofmap.brief;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
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
		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint :  mission.getMissionFlightBuilder().getPlayerFlight().getWaypointPackage().getWaypointsForLeadPlane())
		{				
		     addPlayerFlightWaypoint(prevWaypoint, waypoint);

		     if (mission.getMissionFlightBuilder().getPlayerFlight().getFlightType() == FlightTypes.ESCORT)
		     {
		         if (waypoint.getWpAction() == WaypointAction.WP_ACTION_RENDEVOUS)
		         {
		             updateEscortWaypointsOnMap();
		         }
		     }

		     if (waypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL)
		     {
		    	 addAttackPoint(mission.getMissionFlightBuilder().getPlayerFlight().getTargetCoords().copy());
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
		BriefingMapPoint mapPoint = new BriefingMapPoint();
		mapPoint.desc = waypoint.getWpAction().getAction();
		mapPoint.coord = waypoint.getPosition().copy();
		
		if ((waypoint.getWpAction() == WaypointAction.WP_ACTION_RENDEVOUS) || 
			(waypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL) || 
            (waypoint.getWpAction() == WaypointAction.WP_ACTION_RECON) || 
			(waypoint.getWpAction() == WaypointAction.WP_ACTION_LANDING_APPROACH))
		{
			mapPoint.editable = false;
		}

		
		return mapPoint;
	}

	public void addTakeoff() 
	{
		McuTakeoff takeoffEntity = mission.getMissionFlightBuilder().getPlayerFlight().getTakeoff();
		if (takeoffEntity != null)
		{
			BriefingMapPoint takeoff = new BriefingMapPoint();
			takeoff.desc = "Airfield";
			takeoff.coord = takeoffEntity.getPosition().copy();
			takeoff.editable = false;

			EditorWaypointGroup editorWaypointGroup = new EditorWaypointGroup();
			editorWaypointGroup.setBriefingMapPoint(takeoff);
			editorWaypointGroup.setWaypointEditor(null);
			editorWaypointGroup.setWaypointInBriefing(null);
			
			briefParametersContext.appendEditorGroup(editorWaypointGroup);
		}
	}
	
	public void addLanding() 
	{
		McuLanding landingEntity = mission.getMissionFlightBuilder().getPlayerFlight().getLanding();
		if (landingEntity != null)
		{
			BriefingMapPoint landing = new BriefingMapPoint();
			landing.desc = "Airfield";
			landing.coord = landingEntity.getPosition().copy();
			landing.editable = false;

			EditorWaypointGroup editorWaypointGroup = new EditorWaypointGroup();
			editorWaypointGroup.setBriefingMapPoint(landing);
			editorWaypointGroup.setWaypointEditor(null);
			editorWaypointGroup.setWaypointInBriefing(null);

			briefParametersContext.appendEditorGroup(editorWaypointGroup);
		}
	}
	
	private void addAttackPoint(Coordinate targetLocation) 
	{
		BriefingMapPoint target = new BriefingMapPoint();
		target.desc = "Target";
		target.coord = targetLocation.copy();
		target.editable = false;

		EditorWaypointGroup editorWaypointGroup = new EditorWaypointGroup();
		editorWaypointGroup.setBriefingMapPoint(target);
		editorWaypointGroup.setWaypointEditor(null);
		editorWaypointGroup.setWaypointInBriefing(null);
		
		briefParametersContext.appendEditorGroup(editorWaypointGroup);
	}
	
    private void updateEscortWaypointsOnMap()
    {        
	    List<McuWaypoint> escortedWaypoints = mission.getMissionFlightBuilder().getPlayerFlight().getLinkedWaypoints().getWaypointsForLeadPlane();
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
