package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointSet
{
    private List<McuWaypoint> waypoints = new ArrayList<>();

    public WaypointSet()
    {
    }
    
    public void addWaypoint(McuWaypoint waypoint)
    {
        waypoints.add(waypoint);
    }
    
    public void addWaypoints(List<McuWaypoint> additionalWaypoints)
    {
        waypoints.addAll(additionalWaypoints);
    }

    public boolean containsWaypoint(long waypointIdToFind)
    {
        for (McuWaypoint waypoint : waypoints)
        {
            if (waypoint.getWaypointID() == waypointIdToFind)
            {
                return true;
            }
        }
        return false;
    }

    public McuWaypoint getWaypointById(long waypointId) throws PWCGException
    {
        for (McuWaypoint waypoint : waypoints)
        {
            if (waypoint.getWaypointID() == waypointId)
            {
                return waypoint;
            }
        }
        throw new PWCGException("Waypoint not found in waypoint set " + waypointId);
    }

    public void replaceWaypoint(McuWaypoint waypoint) throws PWCGException
    {
        int replaceIndex = this.getWaypointIndex(waypoint.getWaypointID());
        waypoints.set(replaceIndex, waypoint);
    }

    public void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        int insertAfterIndex = this.getWaypointIndex(waypointIdAfter);
        waypoints.add(insertAfterIndex+1, newWaypoint);
    }

    public void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException
    {
        int insertBeforeIndex = this.getWaypointIndex(waypointIdBefore);
        waypoints.add(insertBeforeIndex, newWaypoint);
    }
    
    public List<McuWaypoint> getWaypoints()
    {
        return waypoints;
    }
    
    public boolean hasWaypoints()
    {
        if (waypoints.size() == 0)
        {
            return false;
        }
        return true;
    }

    public McuWaypoint getFirstWaypoint() throws PWCGException
    {
        if (waypoints.size() == 0)
        {
            throw new PWCGException("No waypoints in waypoint set");            
        }
        return waypoints.get(0);
    }

    public McuWaypoint getLastWaypoint() throws PWCGException
    {
        if (waypoints.size() == 0)
        {
            throw new PWCGException("No waypoints in waypoint set");            
        }
        return waypoints.get(waypoints.size()-1);
    }

    public void finalize(PlaneMcu plane)
    {
        createTargetAssociations();
        createObjectAssociations(plane);
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        for (McuWaypoint waypoint : waypoints)
        {
            waypoint.write(writer);
        }
    }

    public void removeUnwantedWaypoints(List<McuWaypoint> waypointsToKeep) throws PWCGException
    {
        for (McuWaypoint unwantedWaypoint : getUnwantedWaypoints(waypointsToKeep))
        {
            int indexToRemove = getWaypointIndex(unwantedWaypoint.getWaypointID());
            waypoints.remove(indexToRemove);
        }
    }

    public List<McuWaypoint> getUnwantedWaypoints(List<McuWaypoint> waypointsToKeep) throws PWCGException
    {
        List<McuWaypoint> unwantedWaypoints = new ArrayList<>();
        for (McuWaypoint waypoint : waypoints)
        {
            boolean keepWaypoint = false;
            for (McuWaypoint waypointToKeep : waypointsToKeep)
            {
                if (waypoint.getWaypointID() == waypointToKeep.getWaypointID())
                {
                    keepWaypoint = true;
                }
            }
            
            if (!keepWaypoint)
            {
                unwantedWaypoints.add(waypoint);
            }
        }
        return unwantedWaypoints;        
    }

    private int getWaypointIndex(long waypointId) throws PWCGException
    {
        int index = 0;
        for (McuWaypoint waypoint : waypoints)
        {
            if (waypoint.getWaypointID() == waypointId)
            {
                return index;
            }
            ++index;
        }
        throw new PWCGException("Waypoint not found in waypoint set " + waypointId);
    }

    private void createObjectAssociations(PlaneMcu plane)
    {
        for (McuWaypoint waypoint : waypoints)
        {
            waypoint.setObject(plane.getLinkTrId());
        }
    }

    private void createTargetAssociations()
    {
        McuWaypoint previousWaypoint = null;
        for (McuWaypoint waypoint : waypoints)
        {
            if (previousWaypoint != null)
            {
                previousWaypoint.setTarget(waypoint.getIndex());
            }
            previousWaypoint = waypoint;
        }
    }
    
    public WaypointSet duplicateInFormation(int positionInFormation) throws PWCGException
    {
        WaypointSet duplicate = new WaypointSet();
        for (McuWaypoint waypoint : waypoints)
        {
            McuWaypoint planeWaypoint = waypoint.copy();
            Coordinate position = FormationGenerator.generatePositionForPlaneInFormation(waypoint.getOrientation(), planeWaypoint.getPosition(), positionInFormation);
            planeWaypoint.setPosition(position);
            duplicate.waypoints.add(planeWaypoint);
        }
        return duplicate;
    }
}
