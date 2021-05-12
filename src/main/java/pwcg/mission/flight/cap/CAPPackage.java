package pwcg.mission.flight.cap;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightSpotterBuilder;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;
import pwcg.mission.target.TargetDefinitionBuilderOpposing;

public class CAPPackage implements IFlightPackage
{	
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private FlightTypes flightType;
    private List<IFlight> packageFlights = new ArrayList<>();

    public CAPPackage(FlightTypes flightType)
    {
        this.flightType = flightType;
    }

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
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
        }
        
        packageFlights.add(interceptFlight);
        return packageFlights;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder;
        if (this.flightInformation.isPlayerFlight())
        {
            targetDefinitionBuilder = new TargetDefinitionBuilderOpposing(flightInformation);
        }
        else
        {
            targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        }

        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
