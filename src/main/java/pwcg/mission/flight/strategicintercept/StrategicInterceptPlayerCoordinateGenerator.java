package pwcg.mission.flight.strategicintercept;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderStrategic;

public class StrategicInterceptPlayerCoordinateGenerator
{
    private IFlightInformation flightInformation;

    public StrategicInterceptPlayerCoordinateGenerator(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }
    
    public TargetDefinition createTargetCoordinates() throws PWCGException
    {
        TargetDefinitionBuilderStrategic targetDefinitionBuilder = new TargetDefinitionBuilderStrategic(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
