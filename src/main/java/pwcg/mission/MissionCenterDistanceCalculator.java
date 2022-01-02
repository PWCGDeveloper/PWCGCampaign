package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Company;
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
        int missionCenterMaxDistanceFromBaseByRange = determineMaxDistanceByRange();
        
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
    
    private int determineMaxDistanceByRange() throws PWCGException
    {
        int missionCenterMaxDistanceFromBase = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        for (int playerSquadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            Company playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronId);
            PlaneType planeForSquadron = playerSquadron.determineBestPlane(campaign.getDate());
            int maxRange = planeForSquadron.getRange();
            
            int maxPercentOfRange = 40;
            int missionCenterMaxDistanceFromBaseForPlayerSquadron = Double.valueOf((Integer.valueOf(maxRange).doubleValue()) * (Integer.valueOf(maxPercentOfRange).doubleValue() / 100.0)).intValue();
            missionCenterMaxDistanceFromBaseForPlayerSquadron *= 1000;
            
            if (missionCenterMaxDistanceFromBase > missionCenterMaxDistanceFromBaseForPlayerSquadron)
            {
                missionCenterMaxDistanceFromBase = missionCenterMaxDistanceFromBaseForPlayerSquadron;
            }
        }

        return missionCenterMaxDistanceFromBase;
    }
}
