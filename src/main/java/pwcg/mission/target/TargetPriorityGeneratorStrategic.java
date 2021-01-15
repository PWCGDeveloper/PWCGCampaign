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

public class TargetPriorityGeneratorStrategic
{
    public static List<TargetType> getTargetTypePriorities(FlightInformation flightInformation) throws PWCGException
    {
        List<TargetType> strategicTargetTypes = new ArrayList<>();
        
        if (flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_RAIL, 70);
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_AIRFIELD, 50);
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_CITY, 30);
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_FACTORY, 30);
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_PORT, 10);
        }
        else
        {
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_RAIL, 40);
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_AIRFIELD, 50);
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_CITY, 30);
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_FACTORY, 30);
            addItemToWeightedList(strategicTargetTypes, flightInformation, TargetType.TARGET_PORT, 10);
        }

        Collections.shuffle(strategicTargetTypes);
        return strategicTargetTypes;
    }

    private static void addItemToWeightedList(List<TargetType> strategicTargetTypes, FlightInformation flightInformation, TargetType targetType, int weight) throws PWCGException
    {
        ConfigManagerCampaign configManager = flightInformation.getCampaign().getCampaignConfigManager();
        int detailedVictoryDescription = configManager.getIntConfigParam(ConfigItemKeys.PWCGChangesTargetOddsKey);
        if (detailedVictoryDescription == 0)
        {
            if (FlightTypes.isLevelBombingFlight(flightInformation.getFlightType()))
            {
                weight = weight * 10;
            }
            
            int addedWeightForpreference = TargetPriorityPreferenceModifier.getTargetPreferenceWeight(flightInformation, targetType);
            weight += addedWeightForpreference;
        }
            
        if (weight > TargetPriorityGeneratorTactical.MAX_WEIGHTED_ENTRIES)
        {
            weight = TargetPriorityGeneratorTactical.MAX_WEIGHTED_ENTRIES;
        }
        
        for (int i = 0; i < weight; ++i)
        {
            strategicTargetTypes.add(targetType);
        }
    }
}
