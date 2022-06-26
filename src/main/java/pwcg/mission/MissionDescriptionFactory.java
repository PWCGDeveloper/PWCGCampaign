package pwcg.mission;

import pwcg.mission.flight.IFlight;

public class MissionDescriptionFactory 
{
	public static IMissionDescription buildMissionDescription(Mission mission)
	{
	    if (mission.isAAATruckMission())
	    {
            return new MissionDescriptionAAATruck(mission);
	    }
	    else
	    {
            return new MissionDescriptionSinglePlayer(mission, mission.getFlights().getReferencePlayerFlight());
	    }
	}

    public static IMissionDescription buildMissionDescription(Mission mission, IFlight selectedFlight)
    {
        return new MissionDescriptionSinglePlayer(mission, selectedFlight);
    }

}
