package pwcg.mission.flight.attack;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class GroundAttackPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public GroundAttackPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

	public IFlight createPackage () throws PWCGException 
	{
	    IGroundUnitCollection groundUnits = createGroundUnitsForFlight();
        GroundAttackFlight groundAttackFlight = createFlight(groundUnits);
		return groundAttackFlight;
	}

    private GroundAttackFlight createFlight(IGroundUnitCollection groundUnitCollection) throws PWCGException
    {
        GroundAttackFlight groundAttackFlight = new GroundAttackFlight (flightInformation);
		groundAttackFlight.addLinkedGroundUnit(groundUnitCollection);
		groundAttackFlight.createFlight();
        return groundAttackFlight;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
