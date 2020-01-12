package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuWaypoint;

public interface IWaypointPackage
{
    List<MissionPoint> getFlightMissionPoints() throws PWCGException;
    
    void addMissionPointSet(MissionPointSetType missionPointSetType, IMissionPointSet missionPointSet);

    List<McuWaypoint> getAllWaypoints();

    MissionPoint getMissionPointByAction(WaypointAction action) throws PWCGException;

    void updateWaypoints(List<McuWaypoint> waypointsInBriefing) throws PWCGException;

    void finalize() throws PWCGException;
    
    void write(BufferedWriter writer) throws PWCGIOException, PWCGException;
}