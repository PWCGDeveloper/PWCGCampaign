package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.GroundTargetAttackFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackFlightValidator 
{
	public void validateGroundAttackFlight(GroundTargetAttackFlight flight) throws PWCGException
	{
		assert(flight.getWaypointPackage().getWaypointsForLeadPlane().size() > 0);
		validateWaypointLinkage(flight);
		validateWaypointTypes(flight);
	}

	private void validateWaypointLinkage(GroundTargetAttackFlight attackFlight) throws PWCGException 
	{
		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint : attackFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (prevWaypoint != null)
			{
				boolean isNextWaypointLinked = isIndexInTargetList(waypoint.getIndex(), prevWaypoint.getTargets());
				if (!waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TARGET_EGRESS))
				{
					assert(isNextWaypointLinked);
				}
				else
				{
					assert(!isNextWaypointLinked);
					attackFlight.validate();
				}
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

	private void validateWaypointTypes(GroundTargetAttackFlight attackFlight) 
	{
		boolean attackIngressFound = false;
		boolean attackFinalFound = false;
		boolean attackEgressFound = false;
		
		WaypointPriorityValidator.validateWaypointTypes(attackFlight);

        for (List<McuWaypoint> waypoints : attackFlight.getWaypointPackage().getAllWaypointsSets().values())
        {
            for (McuWaypoint waypoint : waypoints)
            {
    			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TARGET_APPROACH))
    			{
    				attackIngressFound = true;
    			}
    			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TARGET_FINAL))
    			{
    				attackFinalFound = true;
    			}
    			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TARGET_EGRESS))
    			{
    				attackEgressFound = true;
    			}
    		}
        }
		
		assert(attackIngressFound);
		assert(attackFinalFound);
		assert(attackEgressFound);
	}
}
