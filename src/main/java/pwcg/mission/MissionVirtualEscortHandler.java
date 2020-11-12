package pwcg.mission;

import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
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
        if (isIncludeEscortforCpuAllowance(escortedFlight))
        {
            if (numEscortsProvided < maxVirtualEscorts)
            {
                needsEscort = NeedsEscortDecider.aiNeedsEscort(escortedFlight);
                if (needsEscort)
                {
                    ++numEscortsProvided;
                }
            }
        }
                
        return needsEscort;
    }
    
    private boolean isIncludeEscortforCpuAllowance(IFlight escortedFlight) throws PWCGException
    {
        ConfigManagerCampaign configManager = escortedFlight.getCampaign().getCampaignConfigManager();
        String currentCpuAllowanceSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigCpuAllowanceKey);
        if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            return false;
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            Side missionSide = escortedFlight.getMission().getMissionSide();
            if (missionSide == Side.NEUTRAL)
            {
                return false;
            }
            else if (missionSide == escortedFlight.getSquadron().determineSide())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            return true;
        }
        
        return false;
    }
}
