package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.target.TargetType;

public class TargetTypeNightGenerator
{
    public static TargetType createTargetType() throws PWCGException
    {
        List <TargetType>prioritizedTargetTypes = getTargetPriorities();
        return getTargetTypeFromPrioritizedList(prioritizedTargetTypes);            
    }

    private static List <TargetType> getTargetPriorities() throws PWCGException 
    {
        List <TargetType> targetTypes = new ArrayList<TargetType>();

        targetTypes.add (TargetType.TARGET_INFANTRY);
        targetTypes.add (TargetType.TARGET_INFANTRY);
        targetTypes.add (TargetType.TARGET_TRANSPORT);
        targetTypes.add (TargetType.TARGET_TRANSPORT);
        targetTypes.add (TargetType.TARGET_TRAIN);
        targetTypes.add (TargetType.TARGET_TRAIN);
        targetTypes.add (TargetType.TARGET_AIRFIELD);
        targetTypes.add (TargetType.TARGET_AIRFIELD);

        return targetTypes;
    }

    private static TargetType getTargetTypeFromPrioritizedList(List<TargetType> prioritizedTargetTypes)
    {
        TargetType targetType = prioritizedTargetTypes.get(RandomNumberGenerator.getRandom(prioritizedTargetTypes.size()));
        return targetType;
    }

}
