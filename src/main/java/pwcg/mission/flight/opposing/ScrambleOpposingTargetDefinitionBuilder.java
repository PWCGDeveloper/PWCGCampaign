package pwcg.mission.flight.opposing;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class ScrambleOpposingTargetDefinitionBuilder
{
    public static TargetDefinition buildSpecificTargetDefinition (FlightInformation scrambleOpposingFlightInformation, FlightTypes opposingFlightType) throws PWCGException
    {
        if (FlightTypes.isBombingFlight(opposingFlightType))
        {
            return ScrambleOpposeTargetDefinitionBuilder.buildScrambleOpposeTargetDefinitionAirToGround(scrambleOpposingFlightInformation, TargetType.TARGET_AIRFIELD);
        }
        else
        {
            return ScrambleOpposeTargetDefinitionBuilder.buildScrambleOpposeTargetDefinitionAirToAir(scrambleOpposingFlightInformation, TargetType.TARGET_AIRFIELD);
         }
    }
}
