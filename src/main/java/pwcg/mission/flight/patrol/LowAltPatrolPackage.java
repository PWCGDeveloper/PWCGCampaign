package pwcg.mission.flight.patrol;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class LowAltPatrolPackage implements IFlightPackage

{
    private List<IFlight> packageFlights = new ArrayList<>();

    @Override
    public List<IFlight> createFlightPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation);
        TargetDefinition targetDefinition = buildTargetDefintion(flightInformation);

        PatrolFlight patrolFlight = new PatrolFlight (flightInformation, targetDefinition);
        patrolFlight.createFlight();

        packageFlights.add(patrolFlight);
        return packageFlights;
    }

    private TargetDefinition buildTargetDefintion(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
