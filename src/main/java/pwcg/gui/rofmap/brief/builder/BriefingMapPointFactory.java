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
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint();

        briefingMapPoint.setDesc(waypoint.getWpAction().getAction());
        briefingMapPoint.setPosition(waypoint.getPosition());
        briefingMapPoint.setAltitude(Double.valueOf(waypoint.getPosition().getYPos()).intValue());
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setEditable(waypoint.getWpAction().isEditable());
        briefingMapPoint.setTarget(waypoint.isTargetWaypoint());

        return briefingMapPoint;
    }

    public static BriefingMapPoint missionPointToMapPoint(MissionPoint missionPoint)
    {
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint();

        briefingMapPoint.setDesc(missionPoint.getAction().getAction());
        briefingMapPoint.setPosition(missionPoint.getPosition());
        briefingMapPoint.setAltitude(Double.valueOf(missionPoint.getPosition().getYPos()).intValue());
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setEditable(missionPoint.getAction().isEditable());
        briefingMapPoint.setTarget(false);

        return briefingMapPoint;
    }

    public static BriefingMapPoint createTakeoff(IFlight flight) throws PWCGException
    {
        MissionPoint takeoffMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_TAKEOFF);
        if (takeoffMissionPoint != null)
        {
            BriefingMapPoint briefingMapPoint = new BriefingMapPoint();
            briefingMapPoint.setDesc("Airfield");
            briefingMapPoint.setPosition(takeoffMissionPoint.getPosition());
            briefingMapPoint.setAltitude(0);
            briefingMapPoint.setDistanceToNextPoint(0);
            briefingMapPoint.setEditable(false);
            briefingMapPoint.setTarget(false);
            return briefingMapPoint;
        }

        return null;
    }

    public static BriefingMapPoint createLanding(IFlight flight) throws PWCGException
    {
        MissionPoint landingMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_LANDING);
        if (landingMissionPoint != null)
        {
            BriefingMapPoint briefingMapPoint = new BriefingMapPoint();
            briefingMapPoint.setDesc("Land");
            briefingMapPoint.setPosition(landingMissionPoint.getPosition());
            briefingMapPoint.setAltitude(0);
            briefingMapPoint.setDistanceToNextPoint(0);
            briefingMapPoint.setEditable(false);
            briefingMapPoint.setTarget(false);
            return briefingMapPoint;
        }
        return null;
    }

    public static BriefingMapPoint createAttackPoint(Coordinate targetLocation)
    {
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint();
        briefingMapPoint.setDesc("Target");
        briefingMapPoint.setPosition(targetLocation.copy());
        briefingMapPoint.setAltitude(Double.valueOf(targetLocation.getYPos()).intValue());
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setEditable(false);
        briefingMapPoint.setTarget(true);
        return briefingMapPoint;
    }

    public static BriefingMapPoint createEscortPoint(McuWaypoint escortWaypoint)
    {
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint();
        briefingMapPoint.setDesc("Escort");
        briefingMapPoint.setPosition(escortWaypoint.getPosition());
        briefingMapPoint.setAltitude(Double.valueOf(escortWaypoint.getPosition().getYPos()).intValue());
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setEditable(false);
        briefingMapPoint.setTarget(true);
        return briefingMapPoint;
    }
}
