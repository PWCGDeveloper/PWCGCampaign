package pwcg.mission.flight.spy;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class SpyExtractPackage implements IFlightPackage
{
    protected IFlightInformation flightInformation;

    public SpyExtractPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.SPY_EXTRACT);

        SpyExtractFlight spyFlight = new SpyExtractFlight (flightInformation);
        spyFlight.createFlight();

        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        spyFlight.addLinkedGroundUnit(groundUnitCollection);

        return spyFlight;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
