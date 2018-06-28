package pwcg.mission.flight.patrol;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;

public class LowAltPatrolPackage extends PatrolPackage
{
    public LowAltPatrolPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.LOW_ALT_PATROL;
    }
}
