package pwcg.mission.flight;

import pwcg.campaign.company.Company;
import pwcg.mission.Mission;

public class FlightBuildInformation
{
    private Mission mission;
    private Company squadron;
    private NecessaryFlightType necessaryFlightType = NecessaryFlightType.NONE;

    public FlightBuildInformation(Mission mission, Company squadron, NecessaryFlightType necessaryFlightType)
    {
        this.mission = mission;
        this.squadron = squadron;
        this.necessaryFlightType = necessaryFlightType;
    }

    public Mission getMission()
    {
        return mission;
    }

    public Company getSquadron()
    {
        return squadron;
    }

    public NecessaryFlightType getNecessaryFlightType()
    {
        return necessaryFlightType;
    }

    public boolean isPlayerFlight()
    {
        return (necessaryFlightType == NecessaryFlightType.PLAYER_FLIGHT);
    }
}
