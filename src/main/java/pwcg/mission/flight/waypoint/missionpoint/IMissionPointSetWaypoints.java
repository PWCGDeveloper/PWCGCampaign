package pwcg.mission.flight.waypoint.missionpoint;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.mcu.McuWaypoint;

public interface IMissionPointSetWaypoints
{
    List<McuWaypoint> getAllWaypoints();
    
    boolean containsWaypoint(long waypointIdToFind);

    McuWaypoint getWaypointById(long waypointId) throws PWCGException;

    void updateWaypointFromBriefing(BriefingMapPoint waypoint) throws PWCGException;

    void removeUnwantedWaypoints(List<BriefingMapPoint> waypointsInBriefing) throws PWCGException;

    void addWaypointFromBriefing(BriefingMapPoint newWaypoint, long waypointIdAfter) throws PWCGException;

    void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException;

    void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException;    
}
