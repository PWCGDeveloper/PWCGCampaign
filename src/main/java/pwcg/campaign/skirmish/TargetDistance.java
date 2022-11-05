package pwcg.campaign.skirmish;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.MissionHumanParticipants;

public class TargetDistance
{
    public static int findMaxTargetDistanceForConfiguration(Campaign campaign) throws PWCGException
    {
        return campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey) * 1000;
    }

    public static boolean isWithinRange(Campaign campaign, MissionHumanParticipants participatingPlayers, Coordinate battleLocation) throws PWCGException
    {
        for (SquadronMember player : participatingPlayers.getAllParticipatingPlayers())
        {
            int maxRangeForPlayer = findMaxTargetDistanceForPlayers(campaign, participatingPlayers);        
            double distance = calculateDistance(campaign, player, battleLocation);
            if (distance > maxRangeForPlayer)
            {
                return false;
            }            
        }
        return true;
    }

    public static double calculateDistance(Campaign campaign,  SquadronMember player, Coordinate battleLocation) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
        Coordinate squadronPosition = squadron.determineCurrentPosition(campaign.getDate());
        double distance = MathUtils.calcDist(battleLocation, squadronPosition);
        return distance;
    }
 
    public static int findMaxTargetDistanceForPlayers(Campaign campaign, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        int missionCenterMaxDistanceFromBase = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        for (int playerSquadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            int missionCenterMaxDistanceFromBaseForPlayerSquadron = findMaxTargetDistanceForSquadron(campaign, playerSquadronId);
            
            if (missionCenterMaxDistanceFromBase > missionCenterMaxDistanceFromBaseForPlayerSquadron)
            {
                missionCenterMaxDistanceFromBase = missionCenterMaxDistanceFromBaseForPlayerSquadron;
            }
        }

        return missionCenterMaxDistanceFromBase;
    }
       
    public static int findMaxTargetDistanceForSquadron(Campaign campaign, int playerSquadronId) throws PWCGException
    {
        Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronId);
        PlaneType planeForSquadron = playerSquadron.determineBestPlane(campaign.getDate());
        int maxRange = planeForSquadron.getRange();
        int maxPercentOfRange = 40;
        int missionCenterMaxDistanceFromBaseForPlayerSquadron = Double.valueOf((Integer.valueOf(maxRange).doubleValue()) * (Integer.valueOf(maxPercentOfRange).doubleValue() / 100.0)).intValue();
        missionCenterMaxDistanceFromBaseForPlayerSquadron *= 1000;
        return missionCenterMaxDistanceFromBaseForPlayerSquadron;
    }

    public static int findTargetDistanceToReferencePlayer(Campaign campaign, Coordinate targetPosition) throws PWCGException
    {
        SquadronMember referencePlayer = campaign.getReferencePlayer();
        int squadronId = referencePlayer.getSquadronId();
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        double distance = MathUtils.calcDist(squadron.determineCurrentPosition(campaign.getDate()), targetPosition);
        return Double.valueOf(distance).intValue();
    }

}
