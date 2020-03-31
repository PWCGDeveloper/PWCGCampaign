package pwcg.mission.flight.intercept;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToGround;

public class InterceptPlayerCoordinateGenerator
{
    private IFlightInformation flightInformation;

    public InterceptPlayerCoordinateGenerator(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }
    
    public TargetDefinition createTargetCoordinates() throws PWCGException
    {
        TargetDefinitionBuilderAirToGround targetDefinitionBuilder = new TargetDefinitionBuilderAirToGround(flightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        return targetDefinition;
    }
}
