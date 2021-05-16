package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.AltitudeForOpposingFlightAdjuster;
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

public class InterceptPackage implements IFlightPackage
{	
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private FlightTypes flightType;
    private List<IFlight> packageFlights = new ArrayList<>();

    public InterceptPackage(FlightTypes flightType)
    {
        this.flightType = flightType;
    }

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, flightType);
        this.targetDefinition = buildTargetDefintion();
        adjustAltitudeToMatchOpposingFlight();
        
        InterceptFlight interceptFlight = new InterceptFlight (flightInformation, targetDefinition);
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
    
    private void adjustAltitudeToMatchOpposingFlight() throws PWCGException
    {
        double highestOpposingAltitude = AltitudeForOpposingFlightAdjuster.getAltitudeForOpposingFlights(flightInformation.getMission());
        if (highestOpposingAltitude > 1500.0)
        {
            int interceptAltitude = Double.valueOf(highestOpposingAltitude).intValue();
            interceptAltitude += 200;
            interceptAltitude += RandomNumberGenerator.getRandom(500);
            flightInformation.setAltitude(interceptAltitude);
        }
    }
}
