package pwcg.mission.flight.validate;

import org.junit.jupiter.api.Assertions;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;

public class PatrolFlightValidator 
{
    public void validatePatrolFlight(IFlight flight) throws PWCGException
    {
        Assertions.assertTrue(flight.getWaypointPackage().getAllWaypoints().size() > 0);
        validateWaypointLinkage(flight);
        validateWaypointTypes(flight);
    }

    private void validateWaypointLinkage(IFlight flight) 
    {
        McuWaypoint prevWaypoint = null;
        for (McuWaypoint waypoint : flight.getWaypointPackage().getAllWaypoints())
        {
            Assertions.assertTrue(waypoint.getPosition().getYPos() > 1.0);
            if (prevWaypoint != null)
            {
                boolean isNextWaypointLinked = IndexLinkValidator.isIndexInTargetList(prevWaypoint.getTargets(), waypoint.getIndex());
                Assertions.assertTrue(isNextWaypointLinked);
            }
            
            prevWaypoint = waypoint;
        }
    }

    private void validateWaypointTypes(IFlight flight) throws PWCGException
    {
        boolean patrolFound = false;

        WaypointPriorityValidator.validateWaypointTypes(flight);

        for (McuWaypoint waypoint : flight.getWaypointPackage().getAllWaypoints())
        {
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_PATROL))
            {
                patrolFound = true;
            }
        }
        
        Assertions.assertTrue(patrolFound);
    }
}
