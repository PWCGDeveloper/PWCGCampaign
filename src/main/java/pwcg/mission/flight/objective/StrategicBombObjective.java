package pwcg.mission.flight.objective;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetType;

public class StrategicBombObjective
{
    static String getMissionObjective(IFlight flight) throws PWCGException 
    {
        String objective = "Bomb the specified objective.  ";

        TargetType targetType = flight.getTargetDefinition().getTargetType();
        String nearestTown = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(flight.getTargetDefinition().getPosition()).getName();
        
        if (targetType == TargetType.TARGET_FACTORY)
        {
            objective = "Bomb the factories near " + nearestTown;
        }
        else if (targetType == TargetType.TARGET_CITY)
        {
            objective = "Bomb available targets at " + nearestTown;
        }
        else if (targetType == TargetType.TARGET_RAIL)
        {
            objective = "Bomb the rail station near " + nearestTown;
        }
        else if (targetType == TargetType.TARGET_AIRFIELD)
        {
            objective = "Bomb the airfield near " + nearestTown;
        }
        else if (targetType == TargetType.TARGET_PORT)
        {
            objective = "Bomb the port facilities near " + nearestTown;
        }

        return objective;
    }

}
