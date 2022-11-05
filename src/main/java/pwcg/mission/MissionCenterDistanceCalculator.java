package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.skirmish.TargetDistance;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PositionFinder;

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
        int missionCenterMaxDistance = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        
        int missionCenterMaxDistanceFromBaseByConfig = determineMaxDistanceByConfig();
        int missionCenterMaxDistanceFromBaseByRange = TargetDistance.findMaxTargetDistanceForPlayers(campaign, participatingPlayers);
        
        if (missionCenterMaxDistanceFromBaseByConfig < missionCenterMaxDistanceFromBaseByRange)
        {
            missionCenterMaxDistance = missionCenterMaxDistanceFromBaseByConfig;
        }
        else
        {
            missionCenterMaxDistance = missionCenterMaxDistanceFromBaseByRange;
        }
        
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
