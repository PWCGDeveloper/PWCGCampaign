package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.mission.flight.IFlight;
import pwcg.mission.options.MissionType;

public class MissionDescriptionFactory 
{
	public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission)
	{
	    if (mission.getMissionOptions().getMissionType() == MissionType.SINGLE_MISSION || mission.getMissionOptions().getMissionType() == MissionType.COOP_MISSION)
	    {
	        return new MissionDescriptionSinglePlayer(campaign, mission, mission.getMissionFlights().getReferencePlayerFlight());
	    }
	    else
	    {
            return new MissionDescriptionAAATruck(campaign, mission);
	    }
	}

    public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission, IFlight selectedFlight)
    {
        return new MissionDescriptionSinglePlayer(campaign, mission, selectedFlight);
    }

}
