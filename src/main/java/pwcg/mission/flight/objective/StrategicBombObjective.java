package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetType;

public class StrategicBombObjective
{
    static String getMissionObjective(IFlight flight, TargetType targetType) throws PWCGException 
    {
        String objective = "Bomb the specified objective.  ";

        if (targetType == TargetType.TARGET_FACTORY)
        {
            objective = "Bomb the factories near " + flight.getTargetDefinition().getTargetName();
        }
        else if (targetType == TargetType.TARGET_CITY)
        {
            objective = "Bomb available targets at " + flight.getTargetDefinition().getTargetName();
        }
        else if (targetType == TargetType.TARGET_RAIL)
        {
            objective = "Bomb the rail station at " + flight.getTargetDefinition().getTargetName();
        }
        else if (targetType == TargetType.TARGET_AIRFIELD)
        {
            objective = "Bomb the airfield at " + flight.getTargetDefinition().getTargetName();
        }
        else if (targetType == TargetType.TARGET_PORT)
        {
            objective = "Bomb the port facilities at " + flight.getTargetDefinition().getTargetName();
        }
        else if (targetType == TargetType.TARGET_SHIPPING)
        {
            objective = "Bomb shipping that has been detected.  Expected shipping types include: " + flight.getTargetDefinition().getTargetName() + ".";
        }

        return objective;
    }

}
