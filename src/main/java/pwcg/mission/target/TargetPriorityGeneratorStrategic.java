package pwcg.mission.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class TargetPriorityGeneratorStrategic
{
    public static List<TargetType> getTargetTypePriorities(Campaign campaign, Squadron squadron) throws PWCGException
    {
        List<TargetType> strategicTargetTypes = new ArrayList<>();
        
        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_RAIL, 70);
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_AIRFIELD, 50);
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_CITY, 30);
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_FACTORY, 30);
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_PORT, 10);
        }
        else
        {
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_RAIL, 40);
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_AIRFIELD, 50);
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_CITY, 30);
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_FACTORY, 30);
            addItemToWeightedList(strategicTargetTypes, TargetType.TARGET_PORT, 10);
        }

        Collections.shuffle(strategicTargetTypes);
        return strategicTargetTypes;
    }

    private static void addItemToWeightedList(List<TargetType> strategicTargetTypes, TargetType targetType, int weight) throws PWCGException
    {
        if (weight > 100)
        {
            weight = 100;
        }
        
        for (int i = 0; i < weight; ++i)
        {
            strategicTargetTypes.add(targetType);
        }
    }
}
