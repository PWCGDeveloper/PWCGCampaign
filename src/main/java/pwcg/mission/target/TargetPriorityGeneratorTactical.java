package pwcg.mission.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public class TargetPriorityGeneratorTactical
{
    public static List<TargetType> getTargetTypePriorities(Campaign campaign, Squadron squadron, FlightTypes flightType) throws PWCGException
    {
        List<TargetType> targetTypesByIndex = new ArrayList<>();

        addInfantryTargetTypePriorities(campaign, squadron, targetTypesByIndex);
        addStructureTargetTypePriorities(flightType, campaign, squadron, targetTypesByIndex);

        Collections.shuffle(targetTypesByIndex);
        return targetTypesByIndex;
    }
    
    private static void addInfantryTargetTypePriorities(Campaign campaign, Squadron squadron, List<TargetType> targetTypesByIndex) throws PWCGException
    {        
        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeArmorKey, TargetType.TARGET_ARMOR);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeInfantryKey, TargetType.TARGET_INFANTRY);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeTransportKey, TargetType.TARGET_TRANSPORT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeTrainKey, TargetType.TARGET_TRAIN);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeShippingKey, TargetType.TARGET_SHIPPING);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeDrifterKey, TargetType.TARGET_DRIFTER);
        }
        else
        {
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeArmorKey, TargetType.TARGET_ARMOR);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeInfantryKey, TargetType.TARGET_INFANTRY);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeTransportKey, TargetType.TARGET_TRANSPORT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeTrainKey, TargetType.TARGET_TRAIN);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeShippingKey, TargetType.TARGET_SHIPPING);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeDrifterKey, TargetType.TARGET_DRIFTER);
        }
    }

    private static void addItemToWeightedList(List<TargetType> targetTypesByIndex, Campaign campaign, String configKey, TargetType targetType) throws PWCGException
    {
        int weight = campaign.getCampaignConfigManager().getIntConfigParam(configKey);
        if (weight > 1000)
        {
            weight = 1000;
        }
        
        for (int i = 0; i < weight; ++i)
        {
            targetTypesByIndex.add(targetType);
        }
    }

    private static void addStructureTargetTypePriorities(FlightTypes flightType, Campaign campaign, Squadron squadron, List<TargetType> targetTypesByIndex) throws PWCGException
    {
        if (flightType == FlightTypes.BOMB || flightType == FlightTypes.STRATEGIC_BOMB)
        {
            addStructuresForBombingMission(campaign, squadron, targetTypesByIndex);
        }
        else if (flightType == FlightTypes.GROUND_ATTACK || 
                 flightType == FlightTypes.DIVE_BOMB || 
                 flightType == FlightTypes.LOW_ALT_BOMB)
        {
            addStructuresForAttackMission(campaign, squadron, targetTypesByIndex);
        }
    }

    private static void addStructuresForBombingMission(Campaign campaign, Squadron squadron, List<TargetType> targetTypesByIndex) throws PWCGException
    {
        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeBridgeKey, TargetType.TARGET_BRIDGE);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeRailKey, TargetType.TARGET_RAIL);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeFactoryKey, TargetType.TARGET_FACTORY);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypePortKey, TargetType.TARGET_PORT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeDepotKey, TargetType.TARGET_DEPOT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeFuelDepotKey, TargetType.TARGET_FUEL);
        }
        else
        {
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeBridgeKey, TargetType.TARGET_BRIDGE);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeRailKey, TargetType.TARGET_RAIL);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeFactoryKey, TargetType.TARGET_FACTORY);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypePortKey, TargetType.TARGET_PORT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeDepotKey, TargetType.TARGET_DEPOT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeFuelDepotKey, TargetType.TARGET_FUEL);
        }
    }

    private static void addStructuresForAttackMission(Campaign campaign, Squadron squadron, List<TargetType> targetTypesByIndex) throws PWCGException
    {
        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeBridgeKey, TargetType.TARGET_BRIDGE);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeRailKey, TargetType.TARGET_RAIL);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeDepotKey, TargetType.TARGET_DEPOT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AlliedTargetTypeFuelDepotKey, TargetType.TARGET_FUEL);
        }
        else
        {
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeBridgeKey, TargetType.TARGET_BRIDGE);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeRailKey, TargetType.TARGET_RAIL);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeDepotKey, TargetType.TARGET_DEPOT);
            addItemToWeightedList(targetTypesByIndex, campaign, ConfigItemKeys.AxisTargetTypeFuelDepotKey, TargetType.TARGET_FUEL);
        }
    }
 
}
