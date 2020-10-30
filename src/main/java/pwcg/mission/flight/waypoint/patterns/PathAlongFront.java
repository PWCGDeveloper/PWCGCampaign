package pwcg.mission.flight.waypoint.patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.MapArea;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class PathAlongFront
{
    private FrontLinesForMap frontLinesForMap;
    private PathAlongFrontData pathAlongFrontData;
    private boolean incrementFrontIndex;
    private List<Coordinate> coordinatesForPath = new ArrayList<>();
    private Side frontLineSide;
    
    public List<Coordinate> createPathAlongFront(PathAlongFrontData pathAlongFrontData) throws PWCGException  
    {
        this.pathAlongFrontData = pathAlongFrontData;
        
        int pathDistance = pathAlongFrontData.getPathDistance();
        
        FrontLinePoint startPoint = initializePathing(pathAlongFrontData);
        determineFrontPointMovementDirection();
        createPathAlongFront(pathDistance, startPoint);
        offsetPathAlongFront();
        createReturnPathAlongFront();
        
        return coordinatesForPath;
    }

    private FrontLinePoint initializePathing(PathAlongFrontData pathAlongFrontData) throws PWCGException
    {
        frontLineSide = pathAlongFrontData.getSide().getOppositeSide();
        frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(pathAlongFrontData.getDate());
        FrontLinePoint startPoint = frontLinesForMap.findClosestFrontPositionForSide(pathAlongFrontData.getTargetGeneralLocation(), frontLineSide);
        coordinatesForPath.add(startPoint.getPosition());
        return startPoint;
    }
    
    private void offsetPathAlongFront() throws PWCGException
    {
        double offsetAngle = determineAngleTowardsOtherSidesLines();
        List<Coordinate> offsetCoordinatesForPath = new ArrayList<>();
        for (Coordinate pathPosition : coordinatesForPath)
        {
            Coordinate offsetPathPosition = MathUtils.calcNextCoord(pathPosition, offsetAngle, pathAlongFrontData.getOffsetTowardsEnemy());
            offsetCoordinatesForPath.add(offsetPathPosition);
        }
        
        coordinatesForPath = offsetCoordinatesForPath;
    }

    private double determineAngleTowardsOtherSidesLines() throws PWCGException
    {
        double cumulativeAngle = 0.0;
        for (Coordinate pathCoordinate : coordinatesForPath)
        {
            Side friendlySide = frontLineSide.getOppositeSide();
            FrontLinePoint closestFriendlyPoint = frontLinesForMap.findClosestFrontPositionForSide(pathCoordinate, friendlySide);

            double angleTowardsOtherSidesLines = MathUtils.calcAngle(closestFriendlyPoint.getPosition(), pathCoordinate);
            cumulativeAngle += angleTowardsOtherSidesLines;
        }
        
        double offsetAngle = cumulativeAngle / coordinatesForPath.size();
        return offsetAngle;
    }
    
    private void createReturnPathAlongFront() throws PWCGException
    {
        if (pathAlongFrontData.isReturnAlongRoute())
        {
            Side friendlySide = frontLineSide.getOppositeSide();

            List<Coordinate> coordinatesForPathReversed = new ArrayList<>(coordinatesForPath);
            Collections.reverse(coordinatesForPathReversed);

            for (Coordinate coordinateToReverse : coordinatesForPathReversed) 
            {
                FrontLinePoint closestFriendlyPoint = frontLinesForMap.findClosestFrontPositionForSide(coordinateToReverse, friendlySide);
                coordinatesForPath.add(closestFriendlyPoint.getPosition());
            }
        }
    }

    private void createPathAlongFront(int pathDistance, FrontLinePoint startPoint) throws PWCGException
    {
        int currentPointIndex = frontLinesForMap.findIndexForClosestPosition(pathAlongFrontData.getTargetGeneralLocation(), frontLineSide);
        determineNextFrontPosition(currentPointIndex, pathDistance);
    }

    private void determineNextFrontPosition(int currentPointIndex,  double remainingPathDistance) throws PWCGException  
    {
        List<FrontLinePoint> frontLines = frontLinesForMap.getFrontLines(frontLineSide);
        FrontLinePoint frontPoint = frontLines.get(currentPointIndex);
        
        if (isEdgeOffMap(frontPoint.getPosition()))
        {
            return;
        }
        
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        double minDistanceToNextWP = productSpecific.getMinimumDistanceBetweenPatrolPoints();
        double actualDistanceToNextWP = 0.0;
        
        int nextFrontIndex = currentPointIndex;
        FrontLinePoint nextFrontLinePoint = null;
        while (actualDistanceToNextWP < minDistanceToNextWP)
        {
            nextFrontIndex = getNextFrontIndex(nextFrontIndex);
            nextFrontLinePoint = frontLines.get(nextFrontIndex);
            actualDistanceToNextWP = MathUtils.calcDist(nextFrontLinePoint.getPosition(), frontPoint.getPosition());
            
            if (isEdgeOffMap(nextFrontLinePoint.getPosition()))
            {
                break;
            }
        }

        nextFrontLinePoint = frontLines.get(nextFrontIndex);
        coordinatesForPath.add(nextFrontLinePoint.getPosition());

        double distanceForThisLeg = MathUtils.calcDist(nextFrontLinePoint.getPosition(), frontPoint.getPosition());
        remainingPathDistance -= distanceForThisLeg;
        
        if (remainingPathDistance > 0)
        {
            if (!isEdgeOffMap(nextFrontLinePoint.getPosition()))
            {
                determineNextFrontPosition(nextFrontIndex, remainingPathDistance);
            }
        }
        else
        {
            return;
        }
    }

    private void determineFrontPointMovementDirection() throws PWCGException 
    {
        int closeToTargetIndex = frontLinesForMap.findIndexForClosestPosition(pathAlongFrontData.getTargetGeneralLocation(), frontLineSide);
        int closeToMissionCenterIndex = frontLinesForMap.findIndexForClosestPosition(pathAlongFrontData.getMission().getMissionBorders().getCenter(), frontLineSide);
        if (closeToTargetIndex > closeToMissionCenterIndex)
        {
            incrementFrontIndex = false;
        }
        else
        {
            incrementFrontIndex = true;
        }
    }

    private boolean isEdgeOffMap(Coordinate frontPosition) throws PWCGException
    {
        MapArea usableMapArea = PWCGContext.getInstance().getCurrentMap().getUsableMapArea();
        if (!usableMapArea.isInMapArea(frontPosition))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    

    private int getNextFrontIndex(int nextFrontIndex) throws PWCGException
    {
        int frontIndex = nextFrontIndex;
        if (incrementFrontIndex)
        {
            ++frontIndex;
        }
        else
        {
            --frontIndex;
        }
        return frontIndex;
    }
}
