package pwcg.mission.flight.intercept;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightSpotterBuilder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class InterceptPackage implements IFlightPackage
{	
    private IFlightInformation flightInformation;

    public InterceptPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
    {
        InterceptFlight interceptFlight = new InterceptFlight (flightInformation);
        interceptFlight.createFlight();
        
        if (flightInformation.isPlayerFlight())
        {
            FlightSpotterBuilder.createSpotters(interceptFlight, flightInformation);
        }
        
        return interceptFlight;
    }
}
