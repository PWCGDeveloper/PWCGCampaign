package pwcg.mission.flight.groundattack;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.GroundTargetDefinitionFactory;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class GroundAttackPackage implements IFlightPackage
{
    private List<IFlight> packageFlights = new ArrayList<>();
    private TargetType roleBasedTarget = TargetType.TARGET_NONE;

    public GroundAttackPackage(TargetType roleBasedTarget)
    {
        this.roleBasedTarget = roleBasedTarget;
    }
    
    @Override
    public List<IFlight> createFlightPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation);
        flightInformation.setRoleBasedTarget(roleBasedTarget);
        
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        GroundAttackFlight groundAttackFlight = new GroundAttackFlight (flightInformation, targetDefinition);
		groundAttackFlight.createFlight();
		
        packageFlights.add(groundAttackFlight);
        return packageFlights;
    }

    private TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        return GroundTargetDefinitionFactory.buildTargetDefinition(flightInformation);
    }
}
