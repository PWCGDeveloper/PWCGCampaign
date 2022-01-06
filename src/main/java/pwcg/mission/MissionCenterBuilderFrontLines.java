package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.MapArea;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionCenterBuilderFrontLines implements IMissionCenterBuilder
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;

    public MissionCenterBuilderFrontLines(Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }

    public Coordinate findMissionCenter(int missionBoxRadius) throws PWCGException
    {
        Coordinate missionCenterCoordinateAxis = findAxisFrontCoordinateWithinSingleMissionParameters();
        Coordinate missionCenterCoordinateAllied = findAlliedCoordinateNearAxisCoordinate(missionCenterCoordinateAxis);

        double angle = MathUtils.calcAngle(missionCenterCoordinateAxis, missionCenterCoordinateAllied);
        double distance = MathUtils.calcDist(missionCenterCoordinateAxis, missionCenterCoordinateAllied) / 2;
        Coordinate missionCenterCoordinate = MathUtils.calcNextCoord(missionCenterCoordinateAxis, angle, distance);
        
        MapArea usableMapArea = PWCGContext.getInstance().getCurrentMap().getUsableMapArea();
        missionCenterCoordinate = MissionCenterAdjuster.keepWithinMap(missionCenterCoordinate.copy(), missionBoxRadius, usableMapArea);

        return missionCenterCoordinate;
    }

    private Coordinate findAxisFrontCoordinateWithinSingleMissionParameters() throws PWCGException
    {
        List<FrontLinePoint> selectedFrontPointsAxis = new ArrayList<>();

        MissionCenterDistanceCalculator distanceCalculator = new MissionCenterDistanceCalculator(campaign);
        int missionCenterMaxDistanceForMission = distanceCalculator.determineMaxDistanceForMissionCenter();

        while (selectedFrontPointsAxis.isEmpty())
        {
            selectedFrontPointsAxis = findFrontLinePointsForMissionCenter(missionCenterMaxDistanceForMission);
            missionCenterMaxDistanceForMission += 10000;
        }
        return selectMissionCenter(selectedFrontPointsAxis);
    }

    private Coordinate selectMissionCenter(List<FrontLinePoint> selectedFrontPointsAxis)
    {
        int frontLinePointIndex = RandomNumberGenerator.getRandom(selectedFrontPointsAxis.size());
        FrontLinePoint axisFrontLinePointForMissionCenter = selectedFrontPointsAxis.get(frontLinePointIndex);
        return axisFrontLinePointForMissionCenter.getPosition();
    }

    private List<FrontLinePoint> findFrontLinePointsForMissionCenter(int missionCenterMaxDistanceForMission)
            throws PWCGException
    {
        Side frontSide = determineFrontSide();

        List<FrontLinePoint> selectedFrontPoints = new ArrayList<>();
        int missionCenterMaxDistanceForMissionStop = missionCenterMaxDistanceForMission + 50000;
        while (selectedFrontPoints.size() < 15 && missionCenterMaxDistanceForMission <= missionCenterMaxDistanceForMissionStop)
        {
            selectedFrontPoints = findFrontLinePointsForMissionCenterByRange(missionCenterMaxDistanceForMission, frontSide);
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

    private List<FrontLinePoint> findFrontLinePointsForMissionCenterByRange(int missionCenterMaxDistanceForMission,
            Side frontSide) throws PWCGException
    {
        List<Coordinate> companyPositions = determineSquadronCoordinates();
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontPoints = frontLinesForMap.findAllFrontLinesForSide(frontSide);
        List<FrontLinePoint> selectedFrontPoints = new ArrayList<>();

        if (!hasFrontLinesFarEnoughFromBase(missionCenterMaxDistanceForMission, companyPositions, frontPoints))
        {
            selectedFrontPoints.addAll(frontPoints);
        }
        else
        {
            selectedFrontPoints = getFrontLinePositionsFromMaxDistance(missionCenterMaxDistanceForMission,
                    companyPositions, frontPoints);
        }

        return selectedFrontPoints;
    }

    private boolean hasFrontLinesFarEnoughFromBase(int missionCenterMinDistanceFromBase, List<Coordinate> companyPositions, List<FrontLinePoint> frontPoints)
            throws PWCGException
    {
        boolean hasFrontPointsFarEnough = false;
        for (FrontLinePoint frontLinePoint : frontPoints)
        {
            for (Coordinate companyPosition : companyPositions)
            {
                double distanceFromSquadron = MathUtils.calcDist(companyPosition, frontLinePoint.getPosition());
                if (distanceFromSquadron > missionCenterMinDistanceFromBase)
                {
                    hasFrontPointsFarEnough = true;
                    return hasFrontPointsFarEnough;
                }
            }
        }

        return hasFrontPointsFarEnough;
    }

    private List<FrontLinePoint> getFrontLinePositionsFromMaxDistance(
            int missionCenterMaxDistanceForMission,
            List<Coordinate> companyPositions, 
            List<FrontLinePoint> frontPoints)
    {
        List<FrontLinePoint> selectedFrontPoints = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : frontPoints)
        {
            for (Coordinate companyPosition : companyPositions)
            {
                double distanceFromSquadron = MathUtils.calcDist(companyPosition, frontLinePoint.getPosition());
                if (distanceFromSquadron < missionCenterMaxDistanceForMission)
                {
                    selectedFrontPoints.add(frontLinePoint);
                }
            }
        }
        return selectedFrontPoints;
    }

    private List<Coordinate> determineSquadronCoordinates() throws PWCGException
    {
        List<Coordinate> playerSquadronCoordinates = new ArrayList<>();
        for (int playerSquadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            Company playerSquadron = PWCGContext.getInstance().getCompanyManager().getCompany(playerSquadronId);
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
