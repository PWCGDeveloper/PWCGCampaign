package pwcg.mission.flight.transport;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class TransportPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public TransportPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    @Override
    public IFlight createPackage() throws PWCGException
	{
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
