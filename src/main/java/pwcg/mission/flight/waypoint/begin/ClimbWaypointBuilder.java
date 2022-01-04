package pwcg.mission.flight.waypoint.begin;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

// TC DELETE THIS
public class ClimbWaypointBuilder
{
    protected Campaign campaign = null;
    protected IFlight flight = null;
    private List<McuWaypoint> climbWPs = new ArrayList<McuWaypoint>();

    public ClimbWaypointBuilder(IFlight flight) throws PWCGException
    {
        this.campaign = flight.getCampaign();
        this.flight = flight;
    }

    public List<McuWaypoint> createClimbWaypointsForPlayerFlight(McuWaypoint takeoffWP) throws PWCGException
    {
        return climbWPs;
    }
}
