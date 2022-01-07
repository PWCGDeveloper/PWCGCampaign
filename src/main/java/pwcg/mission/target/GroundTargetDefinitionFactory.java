package pwcg.mission.target;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public class GroundTargetDefinitionFactory
{
    public static TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(flightInformation); 
        TargetDefinition targetDefinition =  targetDefinitionBuilder.buildTargetDefinition();
        return targetDefinition;
    }
}
