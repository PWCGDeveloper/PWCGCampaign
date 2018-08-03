package pwcg.mission.flight.lonewolf;

import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.patrol.PatrolFlight;

public class LoneWolfFlight extends PatrolFlight
{
    public LoneWolfFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }
    
	@Override
	public int calcNumPlanes() 
	{
	    return 1;
	}

	public String getMissionObjective() throws PWCGException 
	{
		String objective = "You have chosen to fly lone.  Be careful.";
		
		return objective;
	}
}
