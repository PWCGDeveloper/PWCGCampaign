package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class GroundAttackObjective
{
    static String getMissionObjective(IFlight flight) throws PWCGException 
    {
        if (flight.getTargetDefinition().getTargetType().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRATEGIC)
        {
            return GroundAttackObjectiveStrategic.getMissionObjective(flight);
        }
        else
        {
            return GroundAttackObjectiveTactical.getMissionObjective(flight);
        }
    }
}
