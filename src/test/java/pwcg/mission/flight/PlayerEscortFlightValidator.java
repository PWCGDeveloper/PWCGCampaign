package pwcg.mission.flight;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.escort.PlayerEscortFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortFlightValidator 
{
	public void validateEscortFlight(PlayerEscortFlight flight) throws PWCGException
	{
		assert(flight.getWaypointPackage().getWaypointsForLeadPlane().size() > 0);
		validateWaypointLinkage(flight);
		validateWaypointTypes(flight);
	}

	private void validateWaypointLinkage(PlayerEscortFlight escortFlight) throws PWCGException 
	{
		Flight escortedFlight = escortFlight.getEscortedFlight();
		McuWaypoint escortedIngressWP = getEscortedFlightWaypoint(escortedFlight, WaypointAction.WP_ACTION_INGRESS);
		McuWaypoint escortedEgressWP = getEscortedFlightWaypoint(escortedFlight, WaypointAction.WP_ACTION_EGRESS);

		McuTimer coverTimer  = escortFlight.getCoverTimer();
		McuCover cover = escortFlight.getCover();
	    McuTimer escortedFlightWaypointTimer = escortFlight.getEscortedFlightWaypointTimer();

		McuTimer deactivateCoverTimer = escortFlight.getDeactivateCoverTimer();
		McuDeactivate deactivateCoverEntity = escortFlight.getDeactivateCoverEntity();
	    McuTimer egressTimer = escortFlight.getEgressTimer();

		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint : escortFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (prevWaypoint != null)
			{
				boolean isNextWaypointLinked = isIndexInTargetList(waypoint.getIndex(), prevWaypoint.getTargets());
				if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEVOUS))
				{
					assert(!isNextWaypointLinked);
					assert(isIndexInTargetList(coverTimer.getIndex(), prevWaypoint.getTargets()));
					assert(isIndexInTargetList(escortedFlight.getPlanes().get(0).getEntity().getIndex(), cover.getTargets()));
					assert(isIndexInTargetList(cover.getIndex(), coverTimer.getTargets()));
					assert(isIndexInTargetList(escortedFlightWaypointTimer.getIndex(), coverTimer.getTargets()));
					assert(isIndexInTargetList(escortedIngressWP.getIndex(), escortedFlightWaypointTimer.getTargets()));
					
				}
				else if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_EGRESS))
				{
					assert(isNextWaypointLinked);
					assert(isIndexInTargetList(deactivateCoverTimer.getIndex(), escortedEgressWP.getTargets()));
					assert(isIndexInTargetList(deactivateCoverEntity.getIndex(), deactivateCoverTimer.getTargets()));
					assert(isIndexInTargetList(egressTimer.getIndex(), deactivateCoverTimer.getTargets()));
					assert(isIndexInTargetList(prevWaypoint.getIndex(), egressTimer.getTargets()));
				}
				else
				{
					assert(isNextWaypointLinked);
				}
			}
			
			prevWaypoint = waypoint;
		}
	}
	
	private McuWaypoint getEscortedFlightWaypoint(Flight escortedFlight, WaypointAction wpActionIngress) throws PWCGException
	{
		for (McuWaypoint waypoint : escortedFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (waypoint.getWpAction().equals(wpActionIngress))
			{
				return waypoint;
			}
		}
		
		throw new PWCGException("No waypoint of type found: " + wpActionIngress);
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

	private void validateWaypointTypes(PlayerEscortFlight attackFlight) 
	{
		boolean rendezvousFound = false;

		for (McuWaypoint waypoint : attackFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TAKEOFF))
			{
				assert(waypoint.getPriority() == WaypointPriority.PRIORITY_HIGH);
			}
			else
			{
				assert(waypoint.getPriority() == WaypointPriority.PRIORITY_LOW);
			}
			
			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEVOUS))
			{
				rendezvousFound = true;
			}
		}
		
		assert(rendezvousFound);
	}
}
