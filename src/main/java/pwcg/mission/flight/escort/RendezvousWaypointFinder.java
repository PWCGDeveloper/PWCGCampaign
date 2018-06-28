package pwcg.mission.flight.escort;

import java.util.List;

import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class RendezvousWaypointFinder
{
    public McuWaypoint findRendezvousWaypoint(List<McuWaypoint> waypoints)
    {
    	McuWaypoint rendezvousWaypoint = null;
    	
    	rendezvousWaypoint = WaypointGeneratorBase.findWaypointByType(waypoints, WaypointType.INGRESS_WAYPOINT.getName());
    	if (rendezvousWaypoint != null)
    	{
    		return rendezvousWaypoint;
    	}
    	
    	rendezvousWaypoint = WaypointGeneratorBase.findWaypointByType(waypoints, WaypointType.INGRESS_WAYPOINT.getName());
    	if (rendezvousWaypoint != null)
    	{
    		return rendezvousWaypoint;
    	}
    	
    	return null;
    }

}
