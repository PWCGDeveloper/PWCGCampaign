package pwcg.mission.target;

import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.target.preference.TargetPreference;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public class TargetPriorityPreferenceModifier
{
    static int getTargetPreferenceWeight(FlightInformation flightInformation, TargetType targetType) throws PWCGException
    {
        Side side = flightInformation.getSquadron().getCountry().getSide();
        List<TargetPreference> targetPreferences = PWCGContext.getInstance().getMap(flightInformation.getCampaignMap()).getTargetPreferenceManager().getTargetPreference(flightInformation.getCampaign(), side);
        
        int extraWeightForTargetType = 0;
        for (TargetPreference targetPreference : targetPreferences)
        {
            if (targetPreference.getTargetType() == targetType)
            {
                extraWeightForTargetType += targetPreference.getOddsOfUse();
            }
        }
        return extraWeightForTargetType;
    }
}
