package pwcg.gui.rofmap.brief;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuLanding;
import pwcg.mission.mcu.McuTakeoff;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingMapPointFactory
{
	public static BriefingMapPoint waypointToMapPoint(McuWaypoint waypoint)
	{
		BriefingMapPoint mapPoint = new BriefingMapPoint();
		mapPoint.desc = waypoint.getWpAction().getAction();
		mapPoint.coord = waypoint.getPosition().copy();
		
		if ((waypoint.getWpAction() == WaypointAction.WP_ACTION_RENDEZVOUS) || 
			(waypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL) || 
            (waypoint.getWpAction() == WaypointAction.WP_ACTION_RECON) || 
			(waypoint.getWpAction() == WaypointAction.WP_ACTION_LANDING_APPROACH))
		{
			mapPoint.editable = false;
		}

		if (waypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL)
		{
		    mapPoint.isTarget = true;
		}
		
		return mapPoint;
	}

	public static BriefingMapPoint createTakeoff(Mission mission) throws PWCGException 
	{
        BriefingMapPoint takeoff =null;
        Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight(PWCGContextManager.getInstance().getReferencePlayer());
		McuTakeoff takeoffEntity = playerFlight.getTakeoff();
		if (takeoffEntity != null)
		{
			takeoff = new BriefingMapPoint();
			takeoff.desc = "Airfield";
			takeoff.coord = takeoffEntity.getPosition().copy();
			takeoff.editable = false;
		}
		
		return takeoff;
	}
	
	public static BriefingMapPoint createLanding(Mission mission) throws PWCGException 
	{
        BriefingMapPoint landing =null;
		Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight(PWCGContextManager.getInstance().getReferencePlayer());
		McuLanding landingEntity = playerFlight.getLanding();
		if (landingEntity != null)
		{
			landing = new BriefingMapPoint();
			landing.desc = "Airfield";
			landing.coord = landingEntity.getPosition().copy();
			landing.editable = false;
		}
        return landing;
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
        mapPoint.coord = vwp.getCoordinate().copy();
        
        return mapPoint;
    }
}
