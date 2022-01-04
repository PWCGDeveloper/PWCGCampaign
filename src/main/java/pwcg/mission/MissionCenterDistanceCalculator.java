package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;

public class MissionCenterDistanceCalculator
{
    public static final int MINIMUM_MAX_DISTANCE = 20000;
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;

    public MissionCenterDistanceCalculator (Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
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
