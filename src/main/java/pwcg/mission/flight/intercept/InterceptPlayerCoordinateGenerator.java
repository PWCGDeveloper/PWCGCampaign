package pwcg.mission.flight.intercept;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetBuilder;

public class InterceptPlayerCoordinateGenerator
{
    private IFlightInformation flightInformation;

    public InterceptPlayerCoordinateGenerator(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }
    
    public TargetDefinition createTargetCoordinates() throws PWCGException
    {
        TargetBuilder targetDefinitionBuilder = new TargetBuilder(flightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        return targetDefinition;
    }
}
