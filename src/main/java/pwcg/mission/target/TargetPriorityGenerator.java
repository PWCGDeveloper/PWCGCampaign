package pwcg.mission.target;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.target.TargetType;

public class TargetPriorityGenerator
{
    private Campaign campaign;
    private Map<TargetType, Integer> targetTypesByIndex = new HashMap<>();

    public TargetPriorityGenerator(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Map<TargetType, Integer> getTargetTypePriorities(Squadron squadron) throws PWCGException
    {
        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(ConfigItemKeys.AlliedTargetTypeAssaultKey, TargetType.TARGET_ASSAULT);
            addItemToWeightedList(ConfigItemKeys.AlliedTargetTypeDefenseKey, TargetType.TARGET_DEFENSE);
            addItemToWeightedList(ConfigItemKeys.AlliedTargetTypeTransportKey, TargetType.TARGET_TRANSPORT);
            addItemToWeightedList(ConfigItemKeys.AlliedTargetTypeTrainKey, TargetType.TARGET_TRAIN);
            addItemToWeightedList(ConfigItemKeys.AlliedTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(ConfigItemKeys.AlliedTargetTypeShippingKey, TargetType.TARGET_SHIPPING);
            addItemToWeightedList(ConfigItemKeys.AlliedTargetTypeDrifterKey, TargetType.TARGET_DRIFTER);
        }
        else
        {
            addItemToWeightedList(ConfigItemKeys.AxisTargetTypeAssaultKey, TargetType.TARGET_ASSAULT);
            addItemToWeightedList(ConfigItemKeys.AxisTargetTypeDefenseKey, TargetType.TARGET_DEFENSE);
            addItemToWeightedList(ConfigItemKeys.AxisTargetTypeTransportKey, TargetType.TARGET_TRANSPORT);
            addItemToWeightedList(ConfigItemKeys.AxisTargetTypeTrainKey, TargetType.TARGET_TRAIN);
            addItemToWeightedList(ConfigItemKeys.AxisTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(ConfigItemKeys.AxisTargetTypeShippingKey, TargetType.TARGET_SHIPPING);
            addItemToWeightedList(ConfigItemKeys.AxisTargetTypeDrifterKey, TargetType.TARGET_DRIFTER);
        }

        return targetTypesByIndex;
    }

    private void addItemToWeightedList(String configKey, TargetType targetType) throws PWCGException
    {
        int weight = campaign.getCampaignConfigManager().getIntConfigParam(configKey);
        targetTypesByIndex.put(targetType, weight);
    }
}
