package pwcg.mission.flight.waypoint;

public enum WaypointAction
{
	WP_ACTION_START("Start"),
	WP_ACTION_TAKEOFF("Take Off"),
	WP_ACTION_CLIMB("Climb"),
	WP_ACTION_LANDING_APPROACH("Approach"),

	WP_ACTION_INGRESS("Ingress"),
	WP_ACTION_EGRESS("Egress"),
	
	WP_ACTION_SPOT("Spot"),
	WP_ACTION_RECON("Recon"),
	WP_ACTION_SPY("Spy Drop"),

	WP_ACTION_PATROL("Patrol"),
	WP_ACTION_BALLOON("Balloon"),
	WP_ACTION_RENDEVOUS("Rendevous"),

	WP_ACTION_TARGET_APPROACH("Target Approach"),
	WP_ACTION_TARGET_FINAL("Target Final Approach"),
	WP_ACTION_TARGET_EGRESS("Target Final Egress"),
	
	WP_ACTION_MOVE_TO("Move To");

	private String waypointAction;
	
    private WaypointAction(final String waypointAction) 
    {
        this.waypointAction = waypointAction;
    }

    public String getAction() 
    {
        return waypointAction;
    }
}
