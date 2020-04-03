package pwcg.mission.flight;

import pwcg.campaign.squadron.Squadron;
import pwcg.mission.Mission;

public class FlightBuildInformation
{
    private Mission mission;
    private Squadron squadron;
    private boolean isPlayerFlight = false;

    public FlightBuildInformation(Mission mission, Squadron squadron, boolean isPlayerFlight)
    {
        this.mission = mission;
        this.squadron = squadron;
        this.isPlayerFlight = isPlayerFlight;
    }

    public Mission getMission()
    {
        return mission;
    }

    public Squadron getSquadron()
    {
        return squadron;
    }

    public boolean isPlayerFlight()
    {
        return isPlayerFlight;
    }
}
