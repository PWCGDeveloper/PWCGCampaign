package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetType;

public class GroundFreeHuntObjective
{
    static String getMissionObjective(IFlight flight) throws PWCGException 
    {
        String objective = "Free hunt for transportation assets in the area.  ";
        
        TargetType targetType = flight.getTargetDefinition().getTargetType();
        if (targetType == TargetType.TARGET_TRAIN)
        {
            objective += "Preferred target is enemy rail assets.  Follow nearby rail lines to search out trains on the move";
        }
        else if (targetType == TargetType.TARGET_TRANSPORT)
        {
            objective += "Preferred target is enemy trucking and transport.  Follow nearby roads lines to search out transport convoys on the move";
        }
        else if (targetType == TargetType.TARGET_SHIPPING)
        {
            objective += "Preferred target is enemy shipping.  Initiate a search pattern to locate and destro any enemy ships encountered";
        }
        else
        {
            objective += "Preferred target nothing to see here";
        }
        return objective;
    }

}
