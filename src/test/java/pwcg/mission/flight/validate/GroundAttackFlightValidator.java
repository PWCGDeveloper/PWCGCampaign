package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackFlightValidator 
{
	public void validateGroundAttackFlight(IFlight flight) throws PWCGException
	{
		assert(flight.getWaypointPackage().getAllWaypoints().size() > 0);
		validateWaypointLinkage(flight);
		validateWaypointTypes(flight);
	}

	private void validateWaypointLinkage(IFlight attackFlight) throws PWCGException 
	{
		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint : attackFlight.getWaypointPackage().getAllWaypoints())
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

	private void validateWaypointTypes(IFlight attackFlight) 
	{
		boolean attackIngressFound = false;
		boolean attackFinalFound = false;
		boolean attackEgressFound = false;
		
		WaypointPriorityValidator.validateWaypointTypes(attackFlight);

        for (McuWaypoint waypoint : attackFlight.getWaypointPackage().getAllWaypoints())
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
		
		assert(attackIngressFound);
		assert(attackFinalFound);
		assert(attackEgressFound);
	}
}
