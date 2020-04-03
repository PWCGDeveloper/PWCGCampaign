package pwcg.mission.flight.scramble;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;
import pwcg.mission.target.TargetDefinitionBuilderAirToGround;
import pwcg.mission.target.TargetType;

public class ScrambleOpposeTargetDefinitionBuilder
{
    public static TargetDefinition buildScrambleOpposeTargetDefinitionAirToAir(FlightInformation scrambleOpposingFlightInformation, TargetType targetType) throws PWCGException
    {
        TargetDefinitionBuilderAirToAir targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(scrambleOpposingFlightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        
        Coordinate targetLocation = scrambleOpposingFlightInformation.getTargetSearchStartLocation();
        targetDefinition.setTargetPosition(targetLocation);
        targetDefinition.setTargetOrientation(new Orientation());
        return targetDefinition;
    }


    public static TargetDefinition buildScrambleOpposeTargetDefinitionAirToGround(FlightInformation scrambleOpposingFlightInformation, TargetType targetType) throws PWCGException
    {
        TargetDefinitionBuilderAirToGround targetDefinitionBuilder = new TargetDefinitionBuilderAirToGround(scrambleOpposingFlightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        
        Coordinate targetLocation = scrambleOpposingFlightInformation.getTargetSearchStartLocation();
        targetDefinition.setTargetPosition(targetLocation);
        targetDefinition.setTargetOrientation(new Orientation());
        return targetDefinition;
    }
}
