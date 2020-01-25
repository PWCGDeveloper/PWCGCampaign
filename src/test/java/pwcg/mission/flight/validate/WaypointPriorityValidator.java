package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointPriorityValidator
{

    public static void validateWaypointTypes(IFlight flight) 
    {
        
        WaypointPriority expectedWaypointPriority = WaypointPriority.PRIORITY_MED;
        if (flight.getFlightPlanes().get(0).isPrimaryRole(Role.ROLE_FIGHTER))
        {
            expectedWaypointPriority = WaypointPriority.PRIORITY_LOW;
        }
        else
        {
            expectedWaypointPriority = WaypointPriority.PRIORITY_MED;
            if (FlightTypes.isHighPriorityFlight(flight.getFlightInformation().getFlightType()))
            {
                expectedWaypointPriority = WaypointPriority.PRIORITY_HIGH;
            }
        }

        for (List<McuWaypoint> waypoints : flight.getWaypointPackage().getAllWaypointsSets().values())
        {
            for (McuWaypoint waypoint : waypoints)
            {
                if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TAKEOFF))
                {
                    assert(waypoint.getPriority() == WaypointPriority.PRIORITY_HIGH);
                }
                else
                {
                    assert(waypoint.getPriority() == expectedWaypointPriority);
                }
            }        
        }
    }
}
