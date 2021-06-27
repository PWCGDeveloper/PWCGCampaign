package pwcg.mission.flight.waypoint;

public enum WaypointType
{
    TAKEOFF_WAYPOINT("TakeOff"),
    CLIMB_WAYPOINT("Climb"),
    AIR_START_WAYPOINT("AirStart"),
	INGRESS_WAYPOINT("Ingress"),
	EGRESS_WAYPOINT("Egress"),
	LANDING_APPROACH_WAYPOINT("Landing Approach"),

    ARTILLERY_SPOT_WAYPOINT("Artillery Spot"),
	RECON_WAYPOINT("Recon"),
	RECON_TARGET_WAYPOINT("Target"),
    SPY_EXTRACT_WAYPOINT("Spy Extract"),
	TARGET_APPROACH_WAYPOINT("Target Approach"),
    TARGET_FINAL_WAYPOINT("Target Final"),
    TARGET_EGRESS_WAYPOINT("Target Egress"),

    ESCORT_WAYPOINT("Escort"),
	INTERCEPT_WAYPOINT("Intercept"),
	PATROL_WAYPOINT("Patrol"),
    BALLOON_BUST_WAYPOINT("Balloon Bust"),
    BALLOON_DEFENSE_WAYPOINT("Balloon Defense"),
	RENDEZVOUS_WAYPOINT("Rendezvous"),
    DEPART_WAYPOINT("Depart"),

    MOVE_TO_WAYPOINT("Move To"), 
    HUNT_WAYPOINT("Free Hunt");

	private String waypointName;
	
    private WaypointType(final String waypointName) 
    {
        this.waypointName = waypointName;
    }

    public String getName() 
    {
        return waypointName;
    }
    
    public static int getAltitudeForWaypointType(WaypointType waypointType, int flightAltitude)
    {
        if (waypointType == TAKEOFF_WAYPOINT ||
            waypointType == CLIMB_WAYPOINT ||
            waypointType == AIR_START_WAYPOINT)
        {
            int waypointAltitude = Double.valueOf(flightAltitude * .8).intValue();
            if (waypointAltitude < 1000)
            {
                waypointAltitude = 1000;
            }
            return waypointAltitude;
        }
        return flightAltitude;
    }   
}
