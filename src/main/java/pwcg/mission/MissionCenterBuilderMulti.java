package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionCenterBuilderMulti implements IMissionCenterBuilder
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;

    public MissionCenterBuilderMulti (Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }
    
    public Coordinate findMissionCenter() throws PWCGException
    {
        Coordinate averagePlayerLocation = findAveragePlayerLocation();
        Coordinate missionCenterCoordinate = findMissionCenter(averagePlayerLocation);
        return missionCenterCoordinate;
    }

    private Coordinate findAveragePlayerLocation() throws PWCGException
    {
        AveragePlayerLocationFinder averagePlayerLocationFinder = new AveragePlayerLocationFinder(campaign);
        return averagePlayerLocationFinder.findAveragePlayerLocation(participatingPlayers);
    }

    private Coordinate findMissionCenter(Coordinate averagePlayerLocation) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate frontLineCoordinateCloseToCentralLocation = frontLinesForMap.findClosestFrontCoordinateForSide(averagePlayerLocation, Side.AXIS);

        Coordinate missionCenterCoordinateAxis = findAxisFrontCoordinateWithinRadiusOfMissionCenter(frontLineCoordinateCloseToCentralLocation);
        Coordinate missionCenterCoordinateAllied = findAlliedCoordinateNearAxisCoordinate(frontLineCoordinateCloseToCentralLocation);
        
        double angle = MathUtils.calcAngle(missionCenterCoordinateAxis, missionCenterCoordinateAllied);
        double distance = MathUtils.calcDist(missionCenterCoordinateAxis, missionCenterCoordinateAllied) / 2;
        Coordinate missionCenterCoordinate = MathUtils.calcNextCoord(missionCenterCoordinateAxis, angle, distance);
        
        return missionCenterCoordinate;
    }

    private Coordinate findAxisFrontCoordinateWithinRadiusOfMissionCenter(Coordinate frontLineCoordinateCloseToCentralLocation) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> nearbyFrontPointsAxis = frontLinesForMap.findClosestFrontPositionsForSide(
                frontLineCoordinateCloseToCentralLocation, productSpecific.getSmallMissionRadius(), Side.AXIS);
        
        int frontLinePointIndex = RandomNumberGenerator.getRandom(nearbyFrontPointsAxis.size());
        FrontLinePoint axisFrontLinePointNearMissionCenter = nearbyFrontPointsAxis.get(frontLinePointIndex);
        return axisFrontLinePointNearMissionCenter.getPosition();
    }

    private Coordinate findAlliedCoordinateNearAxisCoordinate(Coordinate axisCoordinate) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate alliedCoordinateCloseToAxisCoordinate = frontLinesForMap.findClosestFrontCoordinateForSide(axisCoordinate, Side.ALLIED);
        return alliedCoordinateCloseToAxisCoordinate;
    }

}
