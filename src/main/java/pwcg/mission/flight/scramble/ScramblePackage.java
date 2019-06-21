package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class ScramblePackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public ScramblePackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
	{        		
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());        
		PlayerScrambleFlight scramble = new PlayerScrambleFlight (flightInformation, missionBeginUnit);
		scramble.createUnitMission();

		List<Role> acceptableRoles = new ArrayList<Role>();
		acceptableRoles.add(Role.ROLE_FIGHTER);
		return scramble;
	}
}
