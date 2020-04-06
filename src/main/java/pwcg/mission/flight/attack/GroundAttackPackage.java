package pwcg.mission.flight.attack;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderFactory;

public class GroundAttackPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    public GroundAttackPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.GROUND_ATTACK);
        this.targetDefinition = buildTargetDefintion();

	    IGroundUnitCollection groundUnits = createGroundUnitsForFlight();
        GroundAttackFlight groundAttackFlight = createFlight(groundUnits);
		return groundAttackFlight;
	}

    private GroundAttackFlight createFlight(IGroundUnitCollection groundUnitCollection) throws PWCGException
    {
        GroundAttackFlight groundAttackFlight = new GroundAttackFlight (flightInformation, targetDefinition);
		groundAttackFlight.addLinkedGroundUnit(groundUnitCollection);
		groundAttackFlight.createFlight();
        return groundAttackFlight;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation, targetDefinition);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
