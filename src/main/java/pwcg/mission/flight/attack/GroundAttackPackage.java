package pwcg.mission.flight.attack;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToGround;

public class GroundAttackPackage implements IFlightPackage
{
    public GroundAttackPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        IFlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.GROUND_ATTACK);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        GroundAttackFlight groundAttackFlight = new GroundAttackFlight (flightInformation, targetDefinition);
		groundAttackFlight.createFlight();
        return groundAttackFlight;
    }
    
    private TargetDefinition buildTargetDefinition(IFlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToGround(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
