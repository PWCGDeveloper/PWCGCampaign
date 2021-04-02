package pwcg.mission.flight.spy;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilder;

public class SpyExtractPackage implements IFlightPackage
{
    public SpyExtractPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.SPY_EXTRACT);
        TargetDefinition targetDefinition = buildTargetDefintion(flightInformation);

        SpyExtractFlight spyFlight = new SpyExtractFlight (flightInformation, targetDefinition);
        spyFlight.createFlight();

        return spyFlight;
    }

    private TargetDefinition buildTargetDefintion(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
