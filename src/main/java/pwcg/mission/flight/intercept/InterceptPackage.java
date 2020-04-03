package pwcg.mission.flight.intercept;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightSpotterBuilder;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class InterceptPackage implements IFlightPackage
{	
    private IFlightInformation flightInformation;
    private FlightTypes flightType;

    public InterceptPackage(FlightTypes flightType)
    {
        this.flightType = flightType;
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, flightType);

        InterceptFlight interceptFlight = new InterceptFlight (flightInformation);
        interceptFlight.createFlight();
        
        if (flightInformation.isPlayerFlight())
        {
            FlightSpotterBuilder.createSpotters(interceptFlight, flightInformation);
        }
        
        buildOpposingInterceptFlights(interceptFlight);
        
        return interceptFlight;
    }

    private void buildOpposingInterceptFlights(IFlight flight) throws PWCGException
    {
        InterceptOpposingFlightBuilder opposingFlightBuilder = new InterceptOpposingFlightBuilder(flight.getFlightInformation());
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (IFlight opposingFlight: opposingFlights)
        {
            flight.getLinkedFlights().addLinkedFlight(opposingFlight);
        }
    }
}
