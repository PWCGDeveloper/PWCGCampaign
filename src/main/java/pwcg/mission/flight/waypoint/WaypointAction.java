package pwcg.mission.flight.waypoint;

import pwcg.campaign.plane.PlaneType;

public enum WaypointAction
{
	WP_ACTION_START("Start", false),
	WP_ACTION_TAKEOFF("Take Off", false),
	WP_ACTION_CLIMB("Climb", true),
    WP_ACTION_LANDING_APPROACH("Approach", true),
    WP_ACTION_LANDING("Land", true),

	WP_ACTION_INGRESS("Ingress", true),
	WP_ACTION_EGRESS("Egress", true),
	
	WP_ACTION_SPOT("Spot", false),
	WP_ACTION_RECON("Recon", false),
	WP_ACTION_SPY("Spy Drop", false),

	WP_ACTION_PATROL("Patrol", true),
	WP_ACTION_BALLOON(PlaneType.BALLOON, false),
	WP_ACTION_RENDEZVOUS("Rendezvous", false),

	WP_ACTION_TARGET_APPROACH("Target Approach", true),
	WP_ACTION_TARGET_FINAL("Target Final", false),
	WP_ACTION_TARGET_EGRESS("Target Egress", true),
	
    WP_ACTION_ATTACK("Attack", false),
	WP_ACTION_MOVE_TO("Move To", false);

    private String waypointAction;
    private boolean editable;
	
    private WaypointAction(String waypointAction, boolean editable) 
    {
        this.waypointAction = waypointAction;
        this.editable = editable;
    }

    public String getAction() 
    {
        return waypointAction;
    }

    public boolean isEditable()
    {
        return editable;
    }
}
