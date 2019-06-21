package pwcg.mission.flight.patrol;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class PatrolPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public PatrolPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());        
		PatrolFlight patrol = new PatrolFlight (flightInformation, missionBeginUnit);
		patrol.createUnitMission();
		
		return patrol;
	}
}
