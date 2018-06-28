package pwcg.mission.flight.waypoint;

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

}
