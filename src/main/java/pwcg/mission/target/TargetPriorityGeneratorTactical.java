package pwcg.mission.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public class TargetPriorityGeneratorTactical
{
    static final int MAX_WEIGHTED_ENTRIES = 1000;

    public static List<TargetType> getTargetTypePriorities(FlightInformation flightInformation) throws PWCGException
    {
        List<TargetType> targetTypesByIndex = new ArrayList<>();

        addInfantryTargetTypePriorities(flightInformation, targetTypesByIndex);

        Collections.shuffle(targetTypesByIndex);
        return targetTypesByIndex;
    }
    
    private static void addInfantryTargetTypePriorities(FlightInformation flightInformation, List<TargetType> targetTypesByIndex) throws PWCGException
    {        
        addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeArmorKey, TargetType.TARGET_ARMOR);
        addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeInfantryKey, TargetType.TARGET_INFANTRY);
        addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeTransportKey, TargetType.TARGET_TRANSPORT);
        addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeTrainKey, TargetType.TARGET_TRAIN);
        addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeShippingKey, TargetType.TARGET_SHIPPING);
        addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeDrifterKey, TargetType.TARGET_DRIFTER);
    }

    private static void addItemToWeightedList(List<TargetType> targetTypesByIndex, FlightInformation flightInformation, String configKey, TargetType targetType) throws PWCGException
    {
        int weight = flightInformation.getCampaign().getCampaignConfigManager().getIntConfigParam(configKey);

        if (weight > MAX_WEIGHTED_ENTRIES)
        {
            weight = MAX_WEIGHTED_ENTRIES;
        }
        
        for (int i = 0; i < weight; ++i)
        {
            targetTypesByIndex.add(targetType);
        }
    }

}
