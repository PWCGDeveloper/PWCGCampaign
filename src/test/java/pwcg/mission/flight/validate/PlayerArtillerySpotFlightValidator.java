package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.artySpot.PlayerArtillerySpotFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerArtillerySpotFlightValidator 
{
	public void validateArtillerySpotFlight(PlayerArtillerySpotFlight flight) throws PWCGException
	{
		assert(flight.getWaypointPackage().getWaypointsForLeadPlane().size() > 0);
		validateWaypointLinkage(flight);
		validateWaypointTypes(flight);
	}

	private void validateWaypointLinkage(PlayerArtillerySpotFlight artillerySpotFlight) 
	{
		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint : artillerySpotFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (prevWaypoint != null)
			{
				boolean isNextWaypointLinked = isIndexInTargetList(waypoint.getIndex(), prevWaypoint.getTargets());
				if (!prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_SPOT))
				{
					assert(isNextWaypointLinked);
				}
				else
				{
					assert(!isNextWaypointLinked);
					
					McuTimer startSpotTimer = artillerySpotFlight.getStartSpotTimer();
					McuTimer loiterCompleteTimer = artillerySpotFlight.getLoiterCompleteTimer();
					McuTimer egressWPTimer = artillerySpotFlight.getEgressWPTimer();
					
					assert(isIndexInTargetList(startSpotTimer.getIndex(), prevWaypoint.getTargets()));
					assert(isIndexInTargetList(loiterCompleteTimer.getIndex(), startSpotTimer.getTargets()));
					assert(isIndexInTargetList(egressWPTimer.getIndex(), loiterCompleteTimer.getTargets()));
					assert(isIndexInTargetList(waypoint.getIndex(), egressWPTimer.getTargets()));
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

	private void validateWaypointTypes(PlayerArtillerySpotFlight attackFlight) 
	{
		boolean artySpotFound = false;

		for (McuWaypoint waypoint : attackFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TAKEOFF))
			{
				assert(waypoint.getPriority() == WaypointPriority.PRIORITY_HIGH);
			}
			else
			{
				assert(waypoint.getPriority() == WaypointPriority.PRIORITY_MED);
			}
			
			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_SPOT))
			{
				artySpotFound = true;
			}
		}
		
		assert(artySpotFound);
	}
}
