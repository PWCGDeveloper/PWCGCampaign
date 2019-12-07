package pwcg.campaign.target.locator.targettype;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.target.TacticalTarget;

public class TargetTypeNightGenerator
{
    public static TacticalTarget createTargetType() throws PWCGException
    {
        List <TacticalTarget>prioritizedTargetTypes = getTargetPriorities();
        return getTargetTypeFromPrioritizedList(prioritizedTargetTypes);            
    }

    private static List <TacticalTarget> getTargetPriorities() throws PWCGException 
    {
        List <TacticalTarget> targetTypes = new ArrayList<TacticalTarget>();

        targetTypes.add (TacticalTarget.TARGET_TROOP_CONCENTRATION);
        targetTypes.add (TacticalTarget.TARGET_ARTILLERY);
        targetTypes.add (TacticalTarget.TARGET_ARTILLERY);
        targetTypes.add (TacticalTarget.TARGET_TRANSPORT);
        targetTypes.add (TacticalTarget.TARGET_TRANSPORT);
        targetTypes.add (TacticalTarget.TARGET_TRANSPORT);
        targetTypes.add (TacticalTarget.TARGET_TRANSPORT);
        targetTypes.add (TacticalTarget.TARGET_TRANSPORT);
        targetTypes.add (TacticalTarget.TARGET_TRAIN);
        targetTypes.add (TacticalTarget.TARGET_TRAIN);
        targetTypes.add (TacticalTarget.TARGET_TRAIN);
        targetTypes.add (TacticalTarget.TARGET_AIRFIELD);
        targetTypes.add (TacticalTarget.TARGET_AIRFIELD);

        return targetTypes;
    }

    private static TacticalTarget getTargetTypeFromPrioritizedList(List<TacticalTarget> prioritizedTargetTypes)
    {
        TacticalTarget targetType = prioritizedTargetTypes.get(RandomNumberGenerator.getRandom(prioritizedTargetTypes.size()));
        return targetType;
    }

}
