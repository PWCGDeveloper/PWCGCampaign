package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.flight.FlightPlanes;
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

    List<MissionPoint> getMissionPoints() throws PWCGException;
    
    List<BaseFlightMcu> getAllFlightPoints();
    
    void setAttackToTriggerOnPlane(List<PlaneMcu> planes) throws PWCGException;

    MissionPoint getMissionPointByAction(WaypointAction action) throws PWCGException;

    void updateWaypointsFromBriefing(List<BriefingMapPoint> waypointsInBriefing) throws PWCGException;
    
    void write(BufferedWriter writer) throws PWCGException, PWCGException;

    void finalize(FlightPlanes flightPlanes) throws PWCGException;
    
    IMissionPointSet getMissionPointSet(MissionPointSetType missionPointSetType) throws PWCGException;

    McuWaypoint getWaypointByAction(WaypointAction action) throws PWCGException;
    
    int secondsUntilWaypoint(WaypointAction action) throws PWCGException;

    double getDistanceStartToTarget() throws PWCGException;

    void clearMissionPointSet();

    void addObjectToAllMissionPoints(PlaneMcu planeMcu);
}