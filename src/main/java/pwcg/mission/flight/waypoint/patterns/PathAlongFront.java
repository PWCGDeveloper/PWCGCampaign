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
    private static final int INVALID_FRONT_LINE_INDEX = -1;
    private FrontLinesForMap frontLinesForMap;
    private PathAlongFrontData pathAlongFrontData;
    private boolean incrementFrontIndex;
    private List<Coordinate> coordinatesForPath = new ArrayList<>();
    private Side frontLineSide;
    private List<FrontLinePoint> frontLines = new ArrayList<>();
    private double remainingPathDistance = 100000.0;

    public List<Coordinate> createPathAlongFront(PathAlongFrontData pathAlongFrontData) throws PWCGException  
    {
        this.pathAlongFrontData = pathAlongFrontData;
        
        
        initializePathing(pathAlongFrontData);
        createPathAlongFrontFromPathingData();
        offsetPathAlongFront();
        createReturnPathAlongFront();
        
        return coordinatesForPath;
    }

    private void initializePathing(PathAlongFrontData pathAlongFrontData) throws PWCGException
    {
        frontLineSide = pathAlongFrontData.getSide().getOppositeSide();
        frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(pathAlongFrontData.getDate());
        frontLines = frontLinesForMap.getFrontLines(frontLineSide);
        remainingPathDistance = pathAlongFrontData.getPathDistance();
        determineFrontPointMovementDirection();
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

    private void createPathAlongFrontFromPathingData() throws PWCGException
    {
        int firstFrontIndex = addFirstStartPoint();
        determineNextFrontPosition(firstFrontIndex);
    }

    private int addFirstStartPoint() throws PWCGException
    {
        FrontLinePoint startPoint = frontLinesForMap.findClosestFrontPositionForSide(pathAlongFrontData.getTargetGeneralLocation(), frontLineSide);
        coordinatesForPath.add(startPoint.getPosition());
        return findStartingFrontLinePoint();
    }

    private int findStartingFrontLinePoint() throws PWCGException
    {
        int firstFrontIndex = frontLinesForMap.findIndexForClosestPosition(pathAlongFrontData.getTargetGeneralLocation(), frontLineSide);
        if (frontLines.size() < 20)
        {
            return 2;
        }
        
        if (firstFrontIndex > (frontLines.size() - 8))
        {
            firstFrontIndex = frontLines.size() - 8;
        }
        
        if (firstFrontIndex < 10)
        {
            firstFrontIndex = 10;
        }
        
        return firstFrontIndex;
    }

    private void determineNextFrontPosition(int currentFrontIndex) throws PWCGException  
    {
        FrontLinePoint frontPoint = frontLines.get(currentFrontIndex);
        if (isEdgeOffMap(frontPoint.getPosition()))
        {
            return;
        }
        
        int nextFrontIndex = getNextFrontPoint(frontPoint, currentFrontIndex);
        if (nextFrontIndex != INVALID_FRONT_LINE_INDEX)
        {
            boolean shouldAddAnother = addNextFrontPoint(nextFrontIndex, currentFrontIndex);
            if (shouldAddAnother)
            {
                determineNextFrontPosition(nextFrontIndex);
            }
        }
    }
    
    private boolean addNextFrontPoint(int nextFrontIndex, int currentFrontIndex) throws PWCGException 
    {
        FrontLinePoint nextFrontLinePoint = frontLines.get(nextFrontIndex);
        coordinatesForPath.add(nextFrontLinePoint.getPosition());

        FrontLinePoint frontPoint = frontLines.get(currentFrontIndex);
        double distanceForThisLeg = MathUtils.calcDist(nextFrontLinePoint.getPosition(), frontPoint.getPosition());
        remainingPathDistance -= distanceForThisLeg;
        
        if (remainingPathDistance > 0)
        {
            if (!isEdgeOffMap(nextFrontLinePoint.getPosition()))
            {
                return true;
            }
        }
        
        return false;
    }

    private int getNextFrontPoint(FrontLinePoint frontPoint, int currentPointIndex) throws PWCGException
    {
        if (!frontIndexIsInBounds(currentPointIndex))
        {
            return INVALID_FRONT_LINE_INDEX;
        }

        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        double minDistanceToNextWP = productSpecific.getMinimumDistanceBetweenPatrolPoints();
        
        int nextFrontIndex = currentPointIndex;
        double actualDistanceToNextWP = 0.0;
        while (actualDistanceToNextWP < minDistanceToNextWP)
        {
            nextFrontIndex = getNextFrontIndex(nextFrontIndex);
            if (!frontIndexIsInBounds(nextFrontIndex))
            {
                return INVALID_FRONT_LINE_INDEX;
            }
            
            FrontLinePoint nextFrontLinePoint = frontLines.get(nextFrontIndex);
            actualDistanceToNextWP = MathUtils.calcDist(nextFrontLinePoint.getPosition(), frontPoint.getPosition());
            
            if (isEdgeOffMap(nextFrontLinePoint.getPosition()))
            {
                return INVALID_FRONT_LINE_INDEX;
            }
        }
        
        return nextFrontIndex;
    }
    
    private boolean frontIndexIsInBounds(int currentPointIndex)
    {
        if (currentPointIndex >= (frontLines.size()-1))
        {
            return false;
        }
        if (currentPointIndex <= 0)
        {
            return false;
        }
        return true;
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
