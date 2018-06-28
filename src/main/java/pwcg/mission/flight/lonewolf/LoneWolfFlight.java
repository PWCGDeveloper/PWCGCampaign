package pwcg.mission.flight.lonewolf;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.patrol.PatrolFlight;

public class LoneWolfFlight extends PatrolFlight
{
	public LoneWolfFlight() 
	{
		super ();
	}
	
	/**
	 * @param mission
	 * @param campaign
	 * @param targetCoords
	 * @param squad
	 * @param field
	 * @param isPlayerFlight
	 * @throws PWCGException 
	 * @
	 */
	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Squadron squad, 
	            MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, FlightTypes.LONE_WOLF, targetCoords, squad, missionBeginUnit, isPlayerFlight);
	}

	/**
	 * Calculate the number of planes in the mission
	 * 
	 * @
	 */
	@Override
	public int calcNumPlanes() 
	{
	    return 1;
	}
	
	/**
	 * @return
	 * @throws PWCGException 
	 * @
	 */
	public String getMissionObjective() throws PWCGException 
	{
		String objective = "You have chosen to fly lone.  Be careful.";
		
		return objective;
	}
}
