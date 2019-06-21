package pwcg.mission.flight.intercept;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class InterceptPackage implements IFlightPackage
{	
    private FlightInformation flightInformation;

    public InterceptPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
    {
        InterceptFlight interceptFlight = createInterceptFlight();            
		return interceptFlight;
	}

    private InterceptFlight createInterceptFlight() throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
		InterceptFlight interceptFlight = new InterceptFlight (flightInformation, missionBeginUnit);

		interceptFlight.createUnitMission();
        return interceptFlight;
    }
}
