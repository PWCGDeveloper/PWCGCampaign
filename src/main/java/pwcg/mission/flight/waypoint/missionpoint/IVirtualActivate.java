package pwcg.mission.flight.waypoint.missionpoint;

import pwcg.core.exception.PWCGException;

public interface IVirtualActivate
{

    void linkMissionBeginToFirstVirtualWaypoint(int firstVirtualWaypointIndex) throws PWCGException;

}
