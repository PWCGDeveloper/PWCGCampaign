package pwcg.mission.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;

public class TargetPriorityGeneratorGroundUnit
{
    public static List<TargetType> getTargetTypePriorities(Campaign campaign, Squadron squadron) throws PWCGException
    {
        List<TargetType> targetTypesByIndex = new ArrayList<>();
        
        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeInfantryKey, TargetType.TARGET_INFANTRY);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeTransportKey, TargetType.TARGET_TRANSPORT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeTrainKey, TargetType.TARGET_TRAIN);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeShippingKey, TargetType.TARGET_SHIPPING);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeDrifterKey, TargetType.TARGET_DRIFTER);
        }
        else
        {
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeInfantryKey, TargetType.TARGET_INFANTRY);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeTransportKey, TargetType.TARGET_TRANSPORT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeTrainKey, TargetType.TARGET_TRAIN);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeShippingKey, TargetType.TARGET_SHIPPING);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeDrifterKey, TargetType.TARGET_DRIFTER);
        }

        Collections.shuffle(targetTypesByIndex);
        return targetTypesByIndex;
    }

    private static void addItemToWeightedList(List<TargetType> targetTypesByIndex, Campaign campaign, String configKey, TargetType targetType) throws PWCGException
    {
        int weight = campaign.getCampaignConfigManager().getIntConfigParam(configKey);
        if (weight > 100)
        {
            weight = 100;
        }
        
        for (int i = 0; i < weight; ++i)
        {
            targetTypesByIndex.add(targetType);
        }
    }
}
