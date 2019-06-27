package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class PlayerEscortPackage implements IFlightPackage
{
    private FlightInformation flightInformation;    
    public PlayerEscortPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
    {
	    if(!flightInformation.isPlayerFlight())
        {
	        throw new PWCGMissionGenerationException ("Attempt to create non player escort package");
        }

        Coordinate squadronPosition = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit(squadronPosition.copy());	        
		PlayerEscortFlight playerEscort = new PlayerEscortFlight(flightInformation, missionBeginUnit);
		playerEscort.createUnitMission();
		return playerEscort;
	}
}
