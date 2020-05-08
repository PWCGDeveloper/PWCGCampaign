package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.mission.flight.IFlight;

public class MissionDescriptionFactory 
{
	public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission, IFlight flight)
	{
        return new MissionDescriptionSinglePlayer(campaign, mission, flight);
	}

}
