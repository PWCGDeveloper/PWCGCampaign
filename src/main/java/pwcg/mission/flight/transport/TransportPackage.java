package pwcg.mission.flight.transport;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class TransportPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public TransportPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    @Override
    public Flight createPackage() throws PWCGException
	{
        TransportFlight transportFlight = makeTransportFlight();
        return transportFlight;
	}
    
    private TransportFlight makeTransportFlight() throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());            
        TransportFlight transportFlight = new TransportFlight (flightInformation, missionBeginUnit);
        transportFlight.createUnitMission();

        return transportFlight;
    }
}
