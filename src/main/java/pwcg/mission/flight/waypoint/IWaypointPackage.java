package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;

public interface IWaypointPackage
{    
    void addMissionPointSet(IMissionPointSet missionPointSet);

    List<McuWaypoint> getAllWaypoints();
    
    List<McuWaypoint> getTargetWaypoints();

    List<MissionPoint> getFlightMissionPoints() throws PWCGException;
    
    List<BaseFlightMcu> getAllFlightPoints();

    MissionPoint getMissionPointByAction(WaypointAction action) throws PWCGException;

    void updateWaypoints(List<McuWaypoint> waypointsInBriefing) throws PWCGException;
    
    void write(BufferedWriter writer) throws PWCGIOException, PWCGException;

    IWaypointPackage duplicate(int positionInFormation) throws PWCGException;

    void finalize(PlaneMcu plane) throws PWCGException;
    
    IMissionPointSet getMissionPointSet(MissionPointSetType missionPointSetType) throws PWCGException;

    McuWaypoint getWaypointByAction(WaypointAction action) throws PWCGException;
    
    int secondsUntilWaypoint(WaypointAction action) throws PWCGException;

}