package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointSet;
import pwcg.mission.mcu.McuWaypoint;

public abstract class MissionPointSetMultipleWaypointSet implements IMissionPointSetWaypoints
{
    protected WaypointSet waypointsBefore = new WaypointSet();
    protected WaypointSet waypointsAfter = new WaypointSet();

    @Override
    public List<McuWaypoint> getAllWaypoints()
    {
        List<McuWaypoint> waypoints = new ArrayList<>();
        waypoints.addAll(waypointsBefore.getWaypoints());
        waypoints.addAll(waypointsAfter.getWaypoints());
        return waypoints;
    }

    @Override
    public boolean containsWaypoint(long waypointIdToFind)
    {
        if (waypointsBefore.containsWaypoint(waypointIdToFind))
        {
            return true;
        }
        if (waypointsAfter.containsWaypoint(waypointIdToFind))
        {
            return true;
        }
        return false;
    }

    @Override
    public McuWaypoint getWaypointById(long waypointId) throws PWCGException
    {
        if (waypointsBefore.containsWaypoint(waypointId))
        {
            return waypointsBefore.getWaypointById(waypointId);
        }
        if (waypointsAfter.containsWaypoint(waypointId))
        {
            return waypointsAfter.getWaypointById(waypointId);
        }
        throw new PWCGException("Waypoint not found in waypoint set " + waypointId);
    }

    @Override
    public void updateWaypointFromBriefing(BriefingMapPoint waypoint) throws PWCGException
    {
        if (waypointsBefore.containsWaypoint(waypoint.getWaypointID()))
        {
            waypointsBefore.updateWaypointFromBriefing(waypoint);
        }
        else if (waypointsAfter.containsWaypoint(waypoint.getWaypointID()))
        {
            waypointsAfter.updateWaypointFromBriefing(waypoint);
        }
        else
        {
            throw new PWCGException("Waypoint not found in waypoint set " + waypoint.getWaypointID());
        }
    }

    @Override
    public void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        if (waypointsBefore.containsWaypoint(waypointIdAfter))
        {
            waypointsBefore.addWaypointAfterWaypoint(newWaypoint, waypointIdAfter);
        }
        if (waypointsAfter.containsWaypoint(waypointIdAfter))
        {
            waypointsAfter.addWaypointAfterWaypoint(newWaypoint, waypointIdAfter);
        }
        throw new PWCGException("Waypoint not found in waypoint set " + waypointIdAfter);        
    }

    @Override
    public void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException
    {
        if (waypointsBefore.containsWaypoint(waypointIdBefore))
        {
            waypointsBefore.addWaypointAfterWaypoint(newWaypoint, waypointIdBefore);
        }
        if (waypointsAfter.containsWaypoint(waypointIdBefore))
        {
            waypointsAfter.addWaypointAfterWaypoint(newWaypoint, waypointIdBefore);
        }
        throw new PWCGException("Waypoint not found in waypoint set " + waypointIdBefore);        
    }


    @Override
    public long addWaypointFromBriefing(BriefingMapPoint newWaypoint, long waypointIdBefore) throws PWCGException
    {
        if (waypointsBefore.containsWaypoint(waypointIdBefore))
        {
            return waypointsBefore.addWaypointFromBriefing(newWaypoint, waypointIdBefore);
        }
        if (waypointsAfter.containsWaypoint(waypointIdBefore))
        {
            return waypointsAfter.addWaypointFromBriefing(newWaypoint, waypointIdBefore);
        }
        throw new PWCGException("Waypoint not found in waypoint set " + waypointIdBefore); 
    }

    @Override
    public void removeUnwantedWaypoints(List<BriefingMapPoint> waypointsToKeep) throws PWCGException
    {
        waypointsBefore.removeUnwantedWaypoints(waypointsToKeep);        
        waypointsAfter.removeUnwantedWaypoints(waypointsToKeep);        
    }

    protected void finalizeMissionPointSet(PlaneMcu plane) throws PWCGException
    {
        waypointsBefore.finalize(plane);
        waypointsAfter.finalize(plane);
    }

    protected void write(BufferedWriter writer) throws PWCGException
    {
        waypointsBefore.write(writer);
        waypointsAfter.write(writer);        
    }
    
    
    protected void setLinkToLastWaypoint(int nextTargetIndex) throws PWCGException
    {
        if (waypointsAfter.hasWaypoints())
        {
            McuWaypoint lastWaypoint = waypointsAfter.getLastWaypoint();
            lastWaypoint.setTarget(nextTargetIndex);
        }
    }
    
    protected int getEntryPointAtFirstWaypoint() throws PWCGException
    {
        if (waypointsBefore.hasWaypoints())
        {
            McuWaypoint firstWaypoint = waypointsBefore.getFirstWaypoint();
            return firstWaypoint.getIndex();
        }
        return 0;
    }

    protected List<MissionPoint> getWaypointsBeforeAsMissionPoints()
    {
        List<MissionPoint> missionPoints = new ArrayList<>();
        for (McuWaypoint waypoint : waypointsBefore.getWaypoints())
        {
            MissionPoint attackPrePoint = new MissionPoint(waypoint.getPosition(), waypoint.getWpAction());
            missionPoints.add(attackPrePoint);
        }
        return missionPoints;
    }

    protected List<MissionPoint> getWaypointsAfterAsMissionPoints()
    {
        List<MissionPoint> missionPoints = new ArrayList<>();
        for (McuWaypoint waypoint : waypointsAfter.getWaypoints())
        {
            MissionPoint attackPrePoint = new MissionPoint(waypoint.getPosition(), waypoint.getWpAction());
            missionPoints.add(attackPrePoint);
        }
        return missionPoints;
    }

    public void addWaypointBefore(McuWaypoint waypoint)
    {
        waypointsBefore.addWaypoint(waypoint);
    }
    
    public void addWaypointAfter(McuWaypoint waypoint)
    {
        waypointsAfter.addWaypoint(waypoint);
    }

    public McuWaypoint getFirstWaypoint() throws PWCGException
    {
        return waypointsBefore.getFirstWaypoint();
    }

    public McuWaypoint getFirstWaypointAfter() throws PWCGException
    {
        return waypointsAfter.getFirstWaypoint();
    }

    public McuWaypoint getLastWaypointBefore() throws PWCGException
    {
        return waypointsBefore.getLastWaypoint();
    }
    
    protected WaypointSet duplicateBeginWaypoints(int positionInFormation) throws PWCGException
    {
        return waypointsBefore.duplicateInFormation(positionInFormation);
    }
    
    protected WaypointSet duplicateAfterWaypoints(int positionInFormation) throws PWCGException
    {
        return waypointsAfter.duplicateInFormation(positionInFormation);
    }
}
