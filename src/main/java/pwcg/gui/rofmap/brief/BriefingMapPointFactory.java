package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingMapPointFactory
{
    public static BriefingMapPoint waypointToMapPoint(McuWaypoint waypoint)
    {
        BriefingMapPoint mapPoint = new BriefingMapPoint();
        mapPoint.desc = waypoint.getWpAction().getAction();
        mapPoint.coord = waypoint.getPosition().copy();
        mapPoint.editable = waypoint.getWpAction().isEditable();
        return mapPoint;
    }
    
    public static BriefingMapPoint missionPointToMapPoint(MissionPoint missionPoint)
    {
        BriefingMapPoint mapPoint = new BriefingMapPoint();
        mapPoint.desc = missionPoint.getAction().getAction();
        mapPoint.coord = missionPoint.getPosition().copy();
        mapPoint.editable = missionPoint.getAction().isEditable();
        return mapPoint;
    }

	public static BriefingMapPoint createTakeoff(IFlight flight) throws PWCGException 
	{
        BriefingMapPoint takeoff =null;
        MissionPoint takeoffMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_TAKEOFF);
        if (takeoffMissionPoint != null)
		{
			takeoff = new BriefingMapPoint();
			takeoff.desc = "Airfield";
			takeoff.coord = takeoffMissionPoint.getPosition();
			takeoff.editable = false;
		}
		
		return takeoff;
	}
	
	public static BriefingMapPoint createLanding(IFlight flight) throws PWCGException 
	{
        BriefingMapPoint briefingMapLanding =null;
        MissionPoint landingMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_LANDING);
        if (landingMissionPoint != null)
		{
			briefingMapLanding = new BriefingMapPoint();
			briefingMapLanding.desc = "Land";
			briefingMapLanding.coord = landingMissionPoint.getPosition();
			briefingMapLanding.editable = false;
		}
        return briefingMapLanding;
	}
	   
    public static BriefingMapPoint createAttackPoint(Coordinate targetLocation) 
    {
        BriefingMapPoint target = new BriefingMapPoint();
        target.desc = "Target";
        target.coord = targetLocation.copy();
        target.editable = false;
        return target;
    }

	public static BriefingMapPoint createTargetPoint(McuWaypoint waypoint) 
	{
		BriefingMapPoint target = new BriefingMapPoint();
		target.desc = "Target";
		target.coord = waypoint.getPosition().copy();
        target.editable = false;
        target.isTarget = true;
        return target;
	}

	public static BriefingMapPoint createEscortPoint(McuWaypoint escortWaypoint)
	{
		BriefingMapPoint escort = new BriefingMapPoint();
		escort.desc = "Escort";
		escort.coord = escortWaypoint.getPosition().copy();
		escort.editable = false;
		escort.isTarget = true;
        return escort;
	}
	
	public static BriefingMapPoint virtualWaypointToMapPoint(VirtualWayPointCoordinate vwp)
    {
        BriefingMapPoint mapPoint = new BriefingMapPoint();
        mapPoint.desc = "";
        mapPoint.coord = vwp.getPosition().copy();
        
        return mapPoint;
    }
}
