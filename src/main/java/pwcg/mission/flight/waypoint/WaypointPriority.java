package pwcg.mission.flight.waypoint;

import pwcg.mission.flight.FlightTypeCategory;
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
            if (!flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
            {
                waypoint.setPriority(WaypointPriority.PRIORITY_MED);
            }
        }
    }
}
