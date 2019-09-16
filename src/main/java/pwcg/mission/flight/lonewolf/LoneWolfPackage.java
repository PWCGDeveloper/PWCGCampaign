package pwcg.mission.flight.lonewolf;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.patrol.PatrolPackage;

public class LoneWolfPackage extends PatrolPackage
{
    public LoneWolfPackage(FlightInformation flightInformation)
    {
        super(flightInformation);
    }

    @Override
    public Flight createPackage () throws PWCGException 
	{
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());        
		LoneWolfFlight patrol = new LoneWolfFlight (flightInformation, missionBeginUnit);
		patrol.createUnitMission();		
		return patrol;
	}

}
