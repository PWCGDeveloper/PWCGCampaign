package pwcg.mission.flight.offensive;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class OffensivePackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public OffensivePackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
	{
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());        
		OffensiveFlight offensive = new OffensiveFlight (flightInformation, missionBeginUnit);
		offensive.createUnitMission();
        return offensive;
    }
}
