package pwcg.mission.flight.patrol;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class LowAltPatrolPackage implements IFlightPackage

{
    public LowAltPatrolPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        IFlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.LOW_ALT_PATROL);
        TargetDefinition targetDefinition = buildTargetDefintion(flightInformation);

        PatrolFlight patrolFlight = new PatrolFlight (flightInformation, targetDefinition);
        patrolFlight.createFlight();
        return patrolFlight;
    }

    private TargetDefinition buildTargetDefintion(IFlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
