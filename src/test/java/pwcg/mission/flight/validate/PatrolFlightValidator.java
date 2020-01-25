package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;

public class PatrolFlightValidator 
{
    public void validatePatrolFlight(IFlight flight) throws PWCGException
    {
        assert(flight.getWaypointPackage().getWaypointsForLeadPlane().size() > 0);
        validateWaypointLinkage(flight);
        validateWaypointTypes(flight);
    }

    private void validateWaypointLinkage(IFlight flight) 
    {
        McuWaypoint prevWaypoint = null;
        for (McuWaypoint waypoint : flight.getWaypointPackage().getWaypointsForLeadPlane())
        {
            if (prevWaypoint != null)
            {
                boolean isNextWaypointLinked = isIndexInTargetList(waypoint.getIndex(), prevWaypoint.getTargets());
                assert(isNextWaypointLinked);
            }
            
            prevWaypoint = waypoint;
        }
    }

    private boolean isIndexInTargetList(int index, List<String>targets) 
    {
        boolean isIndexInTargetList = false;
        for (String targetIndex : targets)
        {
            if (targetIndex.equals(new String("" + index)))
            {
                isIndexInTargetList = true; 
            }
        }
        return isIndexInTargetList;
    }

    private void validateWaypointTypes(IFlight flight) 
    {
        boolean patrolFound = false;

        WaypointPriorityValidator.validateWaypointTypes(flight);

        for (McuWaypoint waypoint : flight.getWaypointPackage().getWaypointsForLeadPlane())
        {
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_PATROL))
            {
                patrolFound = true;
            }
        }
        
        assert(patrolFound);
    }
}
