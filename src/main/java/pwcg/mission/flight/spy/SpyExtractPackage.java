package pwcg.mission.flight.spy;

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

public class SpyExtractPackage implements IFlightPackage
{
    public SpyExtractPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        IFlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.SPY_EXTRACT);
        TargetDefinition targetDefinition = buildTargetDefintion(flightInformation);

        SpyExtractFlight spyFlight = new SpyExtractFlight (flightInformation, targetDefinition);
        spyFlight.createFlight();

        return spyFlight;
    }

    private TargetDefinition buildTargetDefintion(IFlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToGround(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
