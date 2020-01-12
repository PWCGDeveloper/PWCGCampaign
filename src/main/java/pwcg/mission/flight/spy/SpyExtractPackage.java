package pwcg.mission.flight.spy;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class SpyExtractPackage implements IFlightPackage
{
    protected IFlightInformation flightInformation;

    public SpyExtractPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
    {
        SpyExtractFlight spyFlight = new SpyExtractFlight (flightInformation);
        spyFlight.createFlight();

        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        spyFlight.getFlightData().getLinkedGroundUnits().addLinkedGroundUnit(groundUnitCollection);

        return spyFlight;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
