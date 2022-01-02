package pwcg.mission.flight.divebomb;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.GroundTargetDefinitionFactory;
import pwcg.mission.target.TargetDefinition;

public class DiveBombingPackage implements IFlightPackage
{
    private List<IFlight> packageFlights = new ArrayList<>();

    public DiveBombingPackage()
    {
    }

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.DIVE_BOMB);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        DiveBombingFlight diveBombingFlight = new DiveBombingFlight (flightInformation, targetDefinition);
        diveBombingFlight.createFlight();

        packageFlights.add(diveBombingFlight);
        return packageFlights;
    }

    private TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        return GroundTargetDefinitionFactory.buildTargetDefinition(flightInformation);
    }
}
