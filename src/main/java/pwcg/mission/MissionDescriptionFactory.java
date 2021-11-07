package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.mission.flight.IFlight;

public class MissionDescriptionFactory 
{
	public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission)
	{
	    if (mission.isAAATruckMission())
	    {
            return new MissionDescriptionAAATruck(campaign, mission);
	    }
	    else
	    {
            return new MissionDescriptionSinglePlayer(campaign, mission, mission.getFlights().getReferencePlayerFlight());
	    }
	}

    public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission, IFlight selectedFlight)
    {
        return new MissionDescriptionSinglePlayer(campaign, mission, selectedFlight);
    }

}
