package pwcg.mission.flight.scramble;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class ScrambleOpposeTargetDefinitionBuilder
{
    public static TargetDefinition buildScrambleOpposeTargetDefinitionAirToAir(FlightInformation scrambleOpposingFlightInformation, TargetType targetType) throws PWCGException
    {
        Coordinate targetLocation = scrambleOpposingFlightInformation.getTargetSearchStartLocation();
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_AIR, targetLocation, scrambleOpposingFlightInformation.getCountry());
        return targetDefinition;
    }


    public static TargetDefinition buildScrambleOpposeTargetDefinitionAirToGround(FlightInformation scrambleOpposingFlightInformation, TargetType targetType) throws PWCGException
    {
        Coordinate targetLocation = scrambleOpposingFlightInformation.getTargetSearchStartLocation();
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_AIR, targetLocation, scrambleOpposingFlightInformation.getCountry());
        return targetDefinition;
    }
}
