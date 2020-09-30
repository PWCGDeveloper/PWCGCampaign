package pwcg.mission;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.NeedsEscortDecider;

public class MissionVirtualEscortHandler
{
    private int numEscortsProvided;
    
    public boolean needsEscort(IFlight escortedFlight) throws PWCGException
    {
        int maxVirtualEscorts = escortedFlight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MaxVirtualEscortedFlightKey);

        boolean needsEscort = false;
        if (numEscortsProvided < maxVirtualEscorts)
        {
            needsEscort = NeedsEscortDecider.aiNeedsEscort(escortedFlight);
            if (needsEscort)
            {
                ++numEscortsProvided;
            }
        }
        return needsEscort;
    }
}
