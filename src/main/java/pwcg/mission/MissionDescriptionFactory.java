package pwcg.mission;

import pwcg.campaign.Campaign;

// TODO COOP TEST
public class MissionDescriptionFactory 
{
	public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission)
	{
		if (campaign.getCampaignData().isCoop())
		{
			return new MissionDescriptionCoop(campaign, mission);
		}
		else
		{
			return new MissionDescriptionSinglePlayer(campaign, mission);
		}
	}

}
