package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointSet;
import pwcg.mission.mcu.McuWaypoint;

public abstract class MissionPointSetSingleWaypointSet implements IMissionPointSetWaypoints
{
    protected WaypointSet waypoints = new WaypointSet();
    
    @Override
    public void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        if (waypoints.containsWaypoint(waypointIdAfter))
        {
            waypoints.addWaypointAfterWaypoint(newWaypoint, waypointIdAfter);
        }        
    }
    
    @Override
    public List<McuWaypoint> getAllWaypoints()
    {
        return waypoints.getWaypoints();
    }

    @Override
    public boolean containsWaypoint(long waypointIdToFind)
    {
        return waypoints.containsWaypoint(waypointIdToFind);
    }

    @Override
    public McuWaypoint getWaypointById(long waypointId) throws PWCGException
    {
        return waypoints.getWaypointById(waypointId);
    }

    @Override
    public void updateWaypointFromBriefing(BriefingMapPoint waypoint) throws PWCGException
    {
        waypoints.updateWaypointFromBriefing(waypoint);
    }

    @Override
    public void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException
    {
        waypoints.addWaypointBeforeWaypoint(newWaypoint, waypointIdBefore);
    }

    @Override
    public void removeUnwantedWaypoints(List<BriefingMapPoint> waypointsToKeep) throws PWCGException
    {
        waypoints.removeUnwantedWaypoints(waypointsToKeep);        
    }

    protected void finalizeMissionPointSet(PlaneMcu plane) throws PWCGException
    {
        waypoints.finalize(plane);
    }

    protected void write(BufferedWriter writer) throws PWCGException
    {
        waypoints.write(writer);
    }

    protected void addWaypoint(McuWaypoint waypoint)
    {
        waypoints.addWaypoint(waypoint);
    }
    
    @Override
    public long addWaypointFromBriefing(BriefingMapPoint newWaypoint, long waypointIdBefore) throws PWCGException
    {
        return waypoints.addWaypointFromBriefing(newWaypoint, waypointIdBefore);                                
    }

    protected McuWaypoint getLastWaypoint() throws PWCGException
    {
        return waypoints.getLastWaypoint();
    }

    protected McuWaypoint getFirstWaypoint() throws PWCGException
    {
        return waypoints.getFirstWaypoint();
    }

    protected void addWaypoints(List<McuWaypoint> climbWaypoints)
    {
        waypoints.addWaypoints(climbWaypoints);        
    }

    protected List<MissionPoint> getWaypointsAsMissionPoints()
    {       
        List<MissionPoint> missionPoints = new ArrayList<>();
        for (McuWaypoint waypoint : waypoints.getWaypoints())
        {
            MissionPoint missionPoint = new MissionPoint(waypoint.getPosition(), waypoint.getWpAction());
            missionPoints.add(missionPoint);
        }
        return missionPoints;
    }
        
    protected WaypointSet duplicateWaypoints(int positionInFormation) throws PWCGException
    {
        return waypoints.duplicateInFormation(positionInFormation);
    }

}
