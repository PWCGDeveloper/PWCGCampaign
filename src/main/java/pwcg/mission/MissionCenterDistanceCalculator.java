package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;

public class MissionCenterDistanceCalculator
{
    public static final int MINIMUM_MAX_DISTANCE = 1000;
    private Campaign campaign;

    public MissionCenterDistanceCalculator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public int determineMaxDistanceForMissionCenter() throws PWCGException
    {        
        int missionCenterMaxDistance = determineMaxDistanceByConfig();
        
        if (missionCenterMaxDistance < MINIMUM_MAX_DISTANCE)
        {
            missionCenterMaxDistance = MINIMUM_MAX_DISTANCE;
        }
        
        return missionCenterMaxDistance;
    }
    
    private int determineMaxDistanceByConfig() throws PWCGException
    {
        int missionCenterMaxDistanceFromBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMaxDistanceFromBaseKey) * 1000;
        return missionCenterMaxDistanceFromBase;
    }
}
