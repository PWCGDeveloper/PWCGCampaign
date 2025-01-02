package pwcg.gui.rofmap.brief.builder;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingMapPointFactory
{
    public static BriefingMapPoint waypointToMapPoint(McuWaypoint waypoint)
    {
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint(waypoint.getWaypointID());

        briefingMapPoint.setDesc(waypoint.getWpAction().getAction());
        briefingMapPoint.setPosition(waypoint.getPosition());
        briefingMapPoint.setAltitude(Double.valueOf(waypoint.getPosition().getYPos()).intValue());
        briefingMapPoint.setCruisingSpeed(waypoint.getSpeed());
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setIsEditable(waypoint.getWpAction().isEditable());
        briefingMapPoint.setIsTarget(waypoint.isTargetWaypoint());
        briefingMapPoint.setIsWaypoint(true);

        return briefingMapPoint;
    }

    public static BriefingMapPoint missionPointToMapPoint(MissionPoint missionPoint)
    {
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint(McuWaypoint.NO_WAYPOINT_ID);

        briefingMapPoint.setDesc(missionPoint.getAction().getAction());
        briefingMapPoint.setPosition(missionPoint.getPosition());
        briefingMapPoint.setAltitude(Double.valueOf(missionPoint.getPosition().getYPos()).intValue());
        briefingMapPoint.setCruisingSpeed(Double.valueOf(missionPoint.getPosition().getYPos()).intValue());
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setIsEditable(missionPoint.getAction().isEditable());
        briefingMapPoint.setIsTarget(false);
        briefingMapPoint.setIsWaypoint(false);

        return briefingMapPoint;
    }

    public static BriefingMapPoint createTakeoff(IFlight flight) throws PWCGException
    {
        MissionPoint takeoffMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_TAKEOFF);
        if (takeoffMissionPoint != null)
        {
            BriefingMapPoint briefingMapPoint = new BriefingMapPoint(McuWaypoint.NO_WAYPOINT_ID);
            briefingMapPoint.setDesc("Airfield");
            briefingMapPoint.setPosition(takeoffMissionPoint.getPosition());
            briefingMapPoint.setAltitude(0);
            briefingMapPoint.setCruisingSpeed(0);
            briefingMapPoint.setDistanceToNextPoint(0);
            briefingMapPoint.setIsEditable(false);
            briefingMapPoint.setIsTarget(false);
            briefingMapPoint.setIsWaypoint(false);
            return briefingMapPoint;
        }

        return null;
    }

    public static BriefingMapPoint createLanding(IFlight flight) throws PWCGException
    {
        MissionPoint landingMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_LANDING);
        if (landingMissionPoint != null)
        {
            BriefingMapPoint briefingMapPoint = new BriefingMapPoint(McuWaypoint.NO_WAYPOINT_ID);
            briefingMapPoint.setDesc("Land");
            briefingMapPoint.setPosition(landingMissionPoint.getPosition());
            briefingMapPoint.setAltitude(0);
            briefingMapPoint.setCruisingSpeed(0);
            briefingMapPoint.setDistanceToNextPoint(0);
            briefingMapPoint.setIsEditable(false);
            briefingMapPoint.setIsTarget(false);
            briefingMapPoint.setIsWaypoint(false);
            return briefingMapPoint;
        }
        return null;
    }

    public static BriefingMapPoint createAttackPoint(Coordinate targetLocation)
    {
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint(McuWaypoint.NO_WAYPOINT_ID);
        briefingMapPoint.setDesc("Target");
        briefingMapPoint.setPosition(targetLocation.copy());
        briefingMapPoint.setAltitude(0);
        briefingMapPoint.setCruisingSpeed(0);
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setIsEditable(false);
        briefingMapPoint.setIsTarget(true);
        briefingMapPoint.setIsWaypoint(false);
        return briefingMapPoint;
    }

    public static BriefingMapPoint createEscortPoint(McuWaypoint escortWaypoint)
    {
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint(McuWaypoint.NO_WAYPOINT_ID);
        briefingMapPoint.setDesc("Escort");
        briefingMapPoint.setPosition(escortWaypoint.getPosition());
        briefingMapPoint.setAltitude(0);
        briefingMapPoint.setCruisingSpeed(0);
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setIsEditable(false);
        briefingMapPoint.setIsTarget(true);
        briefingMapPoint.setIsWaypoint(false);
        return briefingMapPoint;
    }

	public static BriefingMapPoint createReconPoint(Coordinate photoPosition) {
	       BriefingMapPoint briefingMapPoint = new BriefingMapPoint(McuWaypoint.NO_WAYPOINT_ID);
	        briefingMapPoint.setDesc("Photo");
	        briefingMapPoint.setPosition(photoPosition.copy());
	        briefingMapPoint.setAltitude(Double.valueOf(photoPosition.getYPos()).intValue());
	        briefingMapPoint.setCruisingSpeed(0);
	        briefingMapPoint.setDistanceToNextPoint(0);
	        briefingMapPoint.setIsEditable(false);
	        briefingMapPoint.setIsTarget(true);
	        briefingMapPoint.setIsWaypoint(false);
	        return briefingMapPoint;
	}
}
