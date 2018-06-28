package pwcg.mission.flight.waypoint;

public enum WaypointGoal
{
    GOAL_DEFAULT(0),
    GOAL_PRIMARY(1),
    GOAL_SECONDARY(2);

	private Integer waypointGoal;
	
    private WaypointGoal(final Integer waypointGoal) 
    {
        this.waypointGoal = waypointGoal;
    }

    public Integer getGoal() 
    {
        return waypointGoal;
    }

}
