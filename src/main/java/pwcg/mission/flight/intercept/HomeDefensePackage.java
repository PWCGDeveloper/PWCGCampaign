package pwcg.mission.flight.intercept;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;

public class HomeDefensePackage extends InterceptPackage
{
    public HomeDefensePackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);

		flightType = FlightTypes.HOME_DEFENSE;
		opposingFlightRole = Role.ROLE_STRAT_BOMB;
		opposingFlightType = FlightTypes.STRATEGIC_BOMB;
    }
}
