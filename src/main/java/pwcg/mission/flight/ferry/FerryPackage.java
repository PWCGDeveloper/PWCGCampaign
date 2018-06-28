package pwcg.mission.flight.ferry;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;

public class FerryPackage
{
	public Flight createPackage (Mission mission, 
	                             Campaign campaign,
	                             Squadron squad) throws PWCGException 
	{
		// The actual mission is just flying from your old airfield to the new
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(campaign.getSquadronMoveEvent().getLastAirfield().getPosition().copy());
        
		FerryFlight ferry = new FerryFlight (false);
		ferry.initialize(mission, campaign, squad, missionBeginUnit);

		ferry.createUnitMission();
		
		return ferry;
	}
}
