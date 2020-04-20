package pwcg.mission.flight.validate;

import pwcg.mission.flight.FlightTypeCategory;
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
        if (flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
        {
            expectedWaypointPriority = WaypointPriority.PRIORITY_LOW;
        }
        else
        {
            expectedWaypointPriority = WaypointPriority.PRIORITY_MED;
            if (FlightTypes.isHighPriorityFlight(flight.getFlightType()))
            {
                expectedWaypointPriority = WaypointPriority.PRIORITY_HIGH;
            }
        }

        for (McuWaypoint waypoint : flight.getWaypointPackage().getAllWaypoints())
        {
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TAKEOFF))
            {
                assert (waypoint.getPriority() != WaypointPriority.PRIORITY_LOW);
            }
            else
            {
                assert (waypoint.getPriority() == expectedWaypointPriority);
            }
        }
    }
}
