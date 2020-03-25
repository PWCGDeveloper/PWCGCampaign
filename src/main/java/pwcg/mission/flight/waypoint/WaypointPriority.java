package pwcg.mission.flight.waypoint;

import pwcg.campaign.plane.Role;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public enum WaypointPriority
{
	PRIORITY_LOW(0),
	PRIORITY_MED(1),
	PRIORITY_HIGH(2);

	private Integer waypointPriority;
	
    private WaypointPriority(final Integer waypointPriority) 
    {
        this.waypointPriority = waypointPriority;
    }

    public Integer getPriorityValue() 
    {
        return waypointPriority;
    }

    public static void setWaypointsNonFighterPriority(IFlight flight)
    {
        for (McuWaypoint waypoint : flight.getWaypointPackage().getAllWaypoints())
        {
            if (!flight.getFlightPlanes().getFlightLeader().isPrimaryRole(Role.ROLE_FIGHTER))
            {
                if (waypoint.getWpAction() == WaypointAction.WP_ACTION_TAKEOFF)
                {
                    waypoint.setPriority(WaypointPriority.PRIORITY_HIGH);
                }
                else
                {
                    if (FlightTypes.isHighPriorityFlight(flight.getFlightType()))
                    {
                        waypoint.setPriority(WaypointPriority.PRIORITY_MED);
                    }
                    else
                    {
                        waypoint.setPriority(WaypointPriority.PRIORITY_MED);
                    }
                }
            }
        }
    }
}
