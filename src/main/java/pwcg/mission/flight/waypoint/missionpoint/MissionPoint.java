package pwcg.mission.flight.waypoint.missionpoint;

import pwcg.core.location.Coordinate;
import pwcg.mission.flight.waypoint.WaypointAction;

public class MissionPoint
{
    private Coordinate position;
    private WaypointAction action;

    public MissionPoint(Coordinate position, WaypointAction action)
    {
        this.position = position;
        this.action = action;
    }

    public Coordinate getPosition()
    {
        return position.copy();
    }

    public WaypointAction getAction()
    {
        return action;
    }
}
