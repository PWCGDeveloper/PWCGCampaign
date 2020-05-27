package pwcg.mission.flight.cap;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
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

public class CAPPackage implements IFlightPackage
{	
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private FlightTypes flightType;

    public CAPPackage(FlightTypes flightType)
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

        CAPFlight interceptFlight = new CAPFlight (flightInformation, targetDefinition);
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
        
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            CAPOpposingFlightBuilder opposingFlightBuilder = new CAPOpposingFlightBuilder(flight);
            IFlight opposingFlight = opposingFlightBuilder.buildOpposingFlights();
            if (opposingFlight != null)
            {
                flight.getLinkedFlights().addLinkedFlight(opposingFlight);
            }
        }
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
