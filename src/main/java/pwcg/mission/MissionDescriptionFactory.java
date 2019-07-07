package pwcg.mission;

import pwcg.campaign.Campaign;

public class MissionDescriptionFactory 
{
	public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission)
	{
	    // We can use the more complete MissionDescriptionSinglePlayer for coop flights with only one human flight
		if (mission.getMissionFlightBuilder().getPlayerFlights().size() == 1)
		{
            return new MissionDescriptionSinglePlayer(campaign, mission);
		}
		else
		{
            return new MissionDescriptionCoop(campaign, mission);
		}
	}

}
