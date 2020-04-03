package pwcg.mission.flight.transport;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class TransportPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public TransportPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.TRANSPORT);

        TransportFlight transportFlight = makeTransportFlight();
        return transportFlight;
	}
    
    private TransportFlight makeTransportFlight() throws PWCGException
    {
        TransportFlight transportFlight = new TransportFlight (flightInformation);
        transportFlight.createFlight();
        return transportFlight;
    }
}
