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
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class InterceptPackage implements IFlightPackage
{	
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private FlightTypes flightType;

    public InterceptPackage(FlightTypes flightType)
    {
        this.flightType = flightType;
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        if (flightType != FlightTypes.INTERCEPT && flightType != FlightTypes.LOW_ALT_CAP)
        {
            throw new PWCGException("Invalid intercept flight type " + flightType);
        }
        
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, flightType);
        this.targetDefinition = buildTargetDefintion();

        InterceptFlight interceptFlight = new InterceptFlight (flightInformation, targetDefinition);
        interceptFlight.createFlight();
        
        if (flightInformation.isPlayerFlight())
        {
            FlightSpotterBuilder.createSpotters(interceptFlight, flightInformation);
            
            buildOpposingInterceptFlights(interceptFlight);
        }
        
        return interceptFlight;
    }

    private void buildOpposingInterceptFlights(IFlight flight) throws PWCGException
    {
        InterceptOpposingFlightBuilder opposingFlightBuilder = new InterceptOpposingFlightBuilder(flight);
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (IFlight opposingFlight: opposingFlights)
        {
            flight.getLinkedFlights().addLinkedFlight(opposingFlight);
        }
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
