package pwcg.mission.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;

public class TargetPriorityGeneratorTactical
{
    static final int MAX_WEIGHTED_ENTRIES = 1000;

    public static List<TargetType> getTargetTypePriorities(FlightInformation flightInformation) throws PWCGException
    {
        List<TargetType> targetTypesByIndex = new ArrayList<>();

        addInfantryTargetTypePriorities(flightInformation, targetTypesByIndex);
        addStructureTargetTypePriorities(flightInformation, targetTypesByIndex);

        Collections.shuffle(targetTypesByIndex);
        return targetTypesByIndex;
    }
    
    private static void addInfantryTargetTypePriorities(FlightInformation flightInformation, List<TargetType> targetTypesByIndex) throws PWCGException
    {        
        if (flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeArmorKey, TargetType.TARGET_ARMOR);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeInfantryKey, TargetType.TARGET_INFANTRY);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeTransportKey, TargetType.TARGET_TRANSPORT);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeTrainKey, TargetType.TARGET_TRAIN);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeShippingKey, TargetType.TARGET_SHIPPING);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeDrifterKey, TargetType.TARGET_DRIFTER);
        }
        else
        {
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeArmorKey, TargetType.TARGET_ARMOR);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeInfantryKey, TargetType.TARGET_INFANTRY);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeTransportKey, TargetType.TARGET_TRANSPORT);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeTrainKey, TargetType.TARGET_TRAIN);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeShippingKey, TargetType.TARGET_SHIPPING);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeDrifterKey, TargetType.TARGET_DRIFTER);
        }
    }

    private static void addStructureTargetTypePriorities(FlightInformation flightInformation, List<TargetType> targetTypesByIndex) throws PWCGException
    {
        FlightTypes flightType = flightInformation.getFlightType();
        if (flightType == FlightTypes.BOMB || flightType == FlightTypes.STRATEGIC_BOMB)
        {
            addStructuresForBombingMission(flightInformation, targetTypesByIndex);
        }
        else if (flightType == FlightTypes.GROUND_ATTACK || 
                 flightType == FlightTypes.DIVE_BOMB || 
                 flightType == FlightTypes.LOW_ALT_BOMB)
        {
            addStructuresForAttackMission(flightInformation, targetTypesByIndex);
        }
    }

    private static void addStructuresForBombingMission(FlightInformation flightInformation, List<TargetType> targetTypesByIndex) throws PWCGException
    {
        if (flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeBridgeKey, TargetType.TARGET_BRIDGE);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeRailKey, TargetType.TARGET_RAIL);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeFactoryKey, TargetType.TARGET_FACTORY);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypePortKey, TargetType.TARGET_PORT);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeDepotKey, TargetType.TARGET_DEPOT);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeFuelDepotKey, TargetType.TARGET_FUEL);
        }
        else
        {
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeBridgeKey, TargetType.TARGET_BRIDGE);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeRailKey, TargetType.TARGET_RAIL);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeFactoryKey, TargetType.TARGET_FACTORY);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypePortKey, TargetType.TARGET_PORT);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeDepotKey, TargetType.TARGET_DEPOT);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeFuelDepotKey, TargetType.TARGET_FUEL);
        }
    }

    private static void addStructuresForAttackMission(FlightInformation flightInformation, List<TargetType> targetTypesByIndex) throws PWCGException
    {
        if (flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeBridgeKey, TargetType.TARGET_BRIDGE);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeRailKey, TargetType.TARGET_RAIL);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeDepotKey, TargetType.TARGET_DEPOT);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AlliedTargetTypeFuelDepotKey, TargetType.TARGET_FUEL);
        }
        else
        {
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeAirfieldKey, TargetType.TARGET_AIRFIELD);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeBridgeKey, TargetType.TARGET_BRIDGE);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeRailKey, TargetType.TARGET_RAIL);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeDepotKey, TargetType.TARGET_DEPOT);
            addItemToWeightedList(targetTypesByIndex, flightInformation, ConfigItemKeys.AxisTargetTypeFuelDepotKey, TargetType.TARGET_FUEL);
        }
    }

    private static void addItemToWeightedList(List<TargetType> targetTypesByIndex, FlightInformation flightInformation, String configKey, TargetType targetType) throws PWCGException
    {
        ConfigManagerCampaign configManager = flightInformation.getCampaign().getCampaignConfigManager();
        int detailedVictoryDescription = configManager.getIntConfigParam(ConfigItemKeys.PWCGChangesTargetOddsKey);
        int weight = flightInformation.getCampaign().getCampaignConfigManager().getIntConfigParam(configKey);
        if (detailedVictoryDescription == 0)
        {
            if (FlightTypes.isLevelBombingFlight(flightInformation.getFlightType()))
            {
                weight = weight * 10;
            }
            
            int addedWeightForpreference = TargetPriorityPreferenceModifier.getTargetPreferenceWeight(flightInformation, targetType);
            weight += addedWeightForpreference;
        }

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
