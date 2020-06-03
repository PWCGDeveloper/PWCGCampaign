package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionCenterBuilderFrontLines implements IMissionCenterBuilder
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;

    public MissionCenterBuilderFrontLines (Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
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
        MissionCenterDistanceCalculator distanceCalculator = new MissionCenterDistanceCalculator(campaign, participatingPlayers);
        int missionCenterMaxDistanceForMission = distanceCalculator.determineMaxDistanceForMissionCenter();
        int missionCenterMinDistanceFromBase = calculateMinimumDistance(missionCenterMaxDistanceForMission);        
        List<FrontLinePoint> selectedFrontPointsAxis = findFrontLinePointsForMissionCenter(missionCenterMinDistanceFromBase, missionCenterMaxDistanceForMission);
        return selectMissionCenter(selectedFrontPointsAxis);
    }

    private Coordinate selectMissionCenter(List<FrontLinePoint> selectedFrontPointsAxis)
    {
        int frontLinePointIndex = RandomNumberGenerator.getRandom(selectedFrontPointsAxis.size());
        FrontLinePoint axisFrontLinePointForMissionCenter = selectedFrontPointsAxis.get(frontLinePointIndex);
        return axisFrontLinePointForMissionCenter.getPosition();
    }

    private List<FrontLinePoint> findFrontLinePointsForMissionCenter(int missionCenterMinDistanceFromBase, int missionCenterMaxDistanceForMission) throws PWCGException
    {        
        Side frontSide = determineFrontSide();
        
        List<FrontLinePoint> selectedFrontPoints = new ArrayList<>();
        int missionCenterMaxDistanceForMissionStop = missionCenterMaxDistanceForMission + 50000;
        while (selectedFrontPoints.size() < 15 && missionCenterMaxDistanceForMission <= missionCenterMaxDistanceForMissionStop)
        {
            selectedFrontPoints = findFrontLinePointsForMissionCenterByRange(missionCenterMinDistanceFromBase, missionCenterMaxDistanceForMission, frontSide);
            missionCenterMaxDistanceForMission += 5000;
        }
        return selectedFrontPoints;
    }

    private Side determineFrontSide()
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            return Side.ALLIED;
        }
        else
        {
            return Side.AXIS;
        }
    }

    private List<FrontLinePoint> findFrontLinePointsForMissionCenterByRange(int missionCenterMinDistanceFromBase, int missionCenterMaxDistanceForMission, Side frontSide) throws PWCGException
    {
        List<Coordinate> squadronPositions = determineSquadronCoordinates();
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontPoints = frontLinesForMap.findAllFrontLinesForSide(frontSide);
        List<FrontLinePoint> selectedFrontPoints = new ArrayList<>();

        if (!hasFrontLinesFarEnoughFromBase(missionCenterMaxDistanceForMission, squadronPositions, frontPoints))
        {
            selectedFrontPoints.addAll(frontPoints);
        }
        else
        {
            selectedFrontPoints = getFrontLinePositionsFromMinMaxDistance(missionCenterMinDistanceFromBase, missionCenterMaxDistanceForMission, squadronPositions, frontPoints);
        }
        
        return selectedFrontPoints;
    }

    private boolean hasFrontLinesFarEnoughFromBase(int missionCenterMinDistanceFromBase, List<Coordinate> squadronPositions, List<FrontLinePoint> frontPoints) throws PWCGException
    {
        boolean hasFrontPointsFarEnough = false;
        for (FrontLinePoint frontLinePoint : frontPoints)
        {
            for (Coordinate squadronPosition : squadronPositions)
            {
                double distanceFromSquadron = MathUtils.calcDist(squadronPosition, frontLinePoint.getPosition());
                if (distanceFromSquadron > missionCenterMinDistanceFromBase)
                {
                    hasFrontPointsFarEnough = true;
                    return hasFrontPointsFarEnough;
                }
            }
        }
        
        return hasFrontPointsFarEnough;
    }

    private List<FrontLinePoint> getFrontLinePositionsFromMinMaxDistance(int missionCenterMinDistanceFromBase, int missionCenterMaxDistanceForMission,
            List<Coordinate> squadronPositions, List<FrontLinePoint> frontPoints)
    {
        List<FrontLinePoint> selectedFrontPoints = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : frontPoints)
        {
            boolean isInRange = false;
            for (Coordinate squadronPosition : squadronPositions)
            {
                double distanceFromSquadron = MathUtils.calcDist(squadronPosition, frontLinePoint.getPosition());
                if (distanceFromSquadron > missionCenterMinDistanceFromBase)
                {
                    if (distanceFromSquadron < missionCenterMaxDistanceForMission)
                    {
                        isInRange = true;
                    }
                }
            }
            if (isInRange)
            {
                selectedFrontPoints.add(frontLinePoint);
            }
        }
        return selectedFrontPoints;
    }


    private int calculateMinimumDistance(int missionCenterMaxDistanceForMission) throws PWCGException
    {
        int missionCenterMinDistanceFromBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey) * 1000;
        if (missionCenterMinDistanceFromBase > (missionCenterMaxDistanceForMission - MissionCenterDistanceCalculator.MINIMUM_MAX_DISTANCE))
        {
            missionCenterMinDistanceFromBase = missionCenterMaxDistanceForMission - MissionCenterDistanceCalculator.MINIMUM_MAX_DISTANCE;
        }
        return missionCenterMinDistanceFromBase;
    }

    private List<Coordinate> determineSquadronCoordinates() throws PWCGException
    {
        List<Coordinate> playerSquadronCoordinates = new ArrayList<>();
        for (int playerSquadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronId);
            Coordinate playerSquadronCoordinate = playerSquadron.determineCurrentPosition(campaign.getDate());
            playerSquadronCoordinates.add(playerSquadronCoordinate);
        }
        return playerSquadronCoordinates;
    }

    private Coordinate findAlliedCoordinateNearAxisCoordinate(Coordinate axisCoordinate) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate alliedCoordinateCloseToAxisCoordinate = frontLinesForMap.findClosestFrontCoordinateForSide(axisCoordinate, Side.ALLIED);
        return alliedCoordinateCloseToAxisCoordinate;
    }
}
