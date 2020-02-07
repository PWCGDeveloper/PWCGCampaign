package pwcg.mission.target;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public interface ITargetDefinitionBuilder
{
    public TargetDefinition buildTargetDefinition () throws PWCGException;
    TargetDefinition buildScrambleOpposeTargetDefinition(FlightInformation flightInformation, TargetType targetType) throws PWCGException;
}
