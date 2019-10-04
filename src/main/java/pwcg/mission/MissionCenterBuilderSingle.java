package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionCenterBuilderSingle implements IMissionCenterBuilder
{
    private Campaign campaign;
    private SquadronMember player;

    public MissionCenterBuilderSingle (Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.player = participatingPlayers.getAllParticipatingPlayers().get(0);
    }

    public Coordinate findMissionCenter() throws PWCGException
    {
        Coordinate missionCenterCoordinateAxis = findAxisFrontCoordinateWithinSingleMissionParameters();
        Coordinate missionCenterCoordinateAllied = findAlliedCoordinateNearAxisCoordinate(missionCenterCoordinateAxis);
        
        double angle = MathUtils.calcAngle(missionCenterCoordinateAxis, missionCenterCoordinateAllied);
        double distance = MathUtils.calcDist(missionCenterCoordinateAxis, missionCenterCoordinateAllied) / 2;
        Coordinate missionCenterCoordinate = MathUtils.calcNextCoord(missionCenterCoordinateAxis, angle, distance);
        
        return missionCenterCoordinate;
    }

    private Coordinate findAxisFrontCoordinateWithinSingleMissionParameters() throws PWCGException
    {
        int missionCenterMinDistanceFromBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey) * 1000;
        int missionCenterMaxDistanceFromBase = determineMaxDistanceFrombase(missionCenterMinDistanceFromBase);

        Coordinate squadronPosition = determineSquadronCoordinates();
        
        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontPointsAxis = frontLinesForMap.findAllFrontLinesForSide(Side.AXIS);
        List<FrontLinePoint> selectedFrontPointsAxis = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : frontPointsAxis)
        {
            double distanceFromBase = MathUtils.calcDist(squadronPosition, frontLinePoint.getPosition());
            if (distanceFromBase > missionCenterMinDistanceFromBase && distanceFromBase < missionCenterMaxDistanceFromBase)
            {
                selectedFrontPointsAxis.add(frontLinePoint);
            }
        }
        
        int frontLinePointIndex = RandomNumberGenerator.getRandom(selectedFrontPointsAxis.size());
        FrontLinePoint axisFrontLinePointForMissionCenter = selectedFrontPointsAxis.get(frontLinePointIndex);
        return axisFrontLinePointForMissionCenter.getPosition();
    }

    private int determineMaxDistanceFrombase(int missionCenterMinDistanceFromBase) throws PWCGException
    {
        Squadron playerSquadron = player.determineSquadron();
        PlaneType planeForSquadron = playerSquadron.determineBestPlane(campaign.getDate());
        int maxRange = planeForSquadron.getRange();
        
        int maxPercentOfRange = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMaxDistancePercentRangeKey);
        if (maxPercentOfRange > 45)
        {
            maxPercentOfRange = 45;
        }

        if (maxPercentOfRange < 25)
        {
            maxPercentOfRange = 25;
        }

        int missionCenterMaxDistanceFromBase = new Double((new Integer(maxRange).doubleValue()) * (new Integer(maxPercentOfRange).doubleValue() / 100.0)).intValue();
        missionCenterMaxDistanceFromBase *= 1000;
        
        if (missionCenterMaxDistanceFromBase < (missionCenterMinDistanceFromBase + 10000))
        {
            missionCenterMaxDistanceFromBase = missionCenterMinDistanceFromBase + 10000;
        }

        return missionCenterMaxDistanceFromBase;
    }

    private Coordinate determineSquadronCoordinates() throws PWCGException
    {
        Squadron playerSquadron = player.determineSquadron();
        return playerSquadron.determineCurrentPosition(campaign.getDate());
    }

    private Coordinate findAlliedCoordinateNearAxisCoordinate(Coordinate axisCoordinate) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate alliedCoordinateCloseToAxisCoordinate = frontLinesForMap.findClosestFrontCoordinateForSide(axisCoordinate, Side.ALLIED);
        return alliedCoordinateCloseToAxisCoordinate;
    }
}
