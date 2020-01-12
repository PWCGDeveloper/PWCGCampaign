package pwcg.mission.flight.waypoint.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class PathAlongFront
{
    private List<Coordinate> coordinatesForPath = new ArrayList<>();
    private FrontLinesForMap frontLinesForMap;
    private PathAlongFrontData pathAlongFrontData;
    private int currentPointIndex;
    private boolean goNorth;
    private double angleTowardsOtherSidesLines;
    private double distanceOtherSidesLines;
    
    public List<Coordinate> createPathAlongFront(PathAlongFrontData pathAlongFrontData) throws PWCGException  
    {
        this.pathAlongFrontData = pathAlongFrontData;
        
        int randomDistance = RandomNumberGenerator.getRandom(pathAlongFrontData.getRandomDistanceMax());
        int pathDistance = pathAlongFrontData.getPathDistance() + randomDistance;
        
        FrontLinePoint startPoint = initializePathing(pathAlongFrontData);
        determineAngleTowardsOtherSidesLines(startPoint);
        determineDistanceToOtherSidesLines(startPoint);
        determineGoNorth();
        createOutboundPathAlongFront(pathDistance, startPoint);
        offsetOutboundPathAlongFront();
        createReturnPathAlongFront();
        
        return coordinatesForPath;
    }

    private FrontLinePoint initializePathing(PathAlongFrontData pathAlongFrontData) throws PWCGException
    {
        frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(pathAlongFrontData.getDate());
        currentPointIndex = frontLinesForMap.findIndexForClosestPosition(pathAlongFrontData.getTargetGeneralLocation(), pathAlongFrontData.getSide());
        FrontLinePoint startPoint = frontLinesForMap.findAllFrontLinesForSide(pathAlongFrontData.getSide()).get(currentPointIndex);
        coordinatesForPath.add(startPoint.getPosition());
        return startPoint;
    }
    
    private void determineAngleTowardsOtherSidesLines(FrontLinePoint startPoint) throws PWCGException
    {
        Side oppositeSide = pathAlongFrontData.getSide().getOppositeSide();
        int closestToStartPointIndex = frontLinesForMap.findIndexForClosestPosition(startPoint.getPosition(), oppositeSide);
        FrontLinePoint closestToStartPoint = frontLinesForMap.findAllFrontLinesForSide(oppositeSide).get(closestToStartPointIndex);

        angleTowardsOtherSidesLines = MathUtils.calcAngle(startPoint.getPosition(), closestToStartPoint.getPosition());
    }
    
    private void determineDistanceToOtherSidesLines(FrontLinePoint startPoint) throws PWCGException
    {
        Side oppositeSide = pathAlongFrontData.getSide().getOppositeSide();
        int closestToStartPointIndex = frontLinesForMap.findIndexForClosestPosition(startPoint.getPosition(), oppositeSide);
        FrontLinePoint closestToStartPoint = frontLinesForMap.findAllFrontLinesForSide(oppositeSide).get(closestToStartPointIndex);

        distanceOtherSidesLines = MathUtils.calcDist(startPoint.getPosition(), closestToStartPoint.getPosition());
    }

    private void createOutboundPathAlongFront(int pathDistance, FrontLinePoint startPoint) throws PWCGException
    {
        determineNextFrontPosition(startPoint, pathDistance);
    }
    
    
    private void offsetOutboundPathAlongFront() throws PWCGException
    {
        List<Coordinate> offsetCoordinatesForPath = new ArrayList<>();
        for (Coordinate pathPosition : coordinatesForPath)
        {
            Coordinate offsetPathPosition = MathUtils.calcNextCoord(pathPosition, angleTowardsOtherSidesLines, pathAlongFrontData.getOffsetTowardsEnemy());
            offsetCoordinatesForPath.add(offsetPathPosition);
        }
        
        coordinatesForPath = offsetCoordinatesForPath;
    }
    
    private void createReturnPathAlongFront() throws PWCGException
    {
        List<Coordinate> returnPathPositions = new ArrayList<>();
        if (pathAlongFrontData.isReturnAlongRoute())
        {
            double distanceToReturn = distanceOtherSidesLines + (pathAlongFrontData.getOffsetTowardsEnemy() * -1);
            ListIterator<Coordinate> iter = coordinatesForPath.listIterator(coordinatesForPath.size());
            while (iter.hasPrevious()) 
            {
                Coordinate pathPosition = iter.previous();
                Coordinate returnPathPosition = MathUtils.calcNextCoord(pathPosition, angleTowardsOtherSidesLines, distanceToReturn);
                returnPathPositions.add(returnPathPosition);
            }
        }
        
        coordinatesForPath.addAll(returnPathPositions);
    }

    private void determineNextFrontPosition(FrontLinePoint startPoint,  double remainingPathDistance) throws PWCGException  
    {
        if (isEdgeOffMap(currentPointIndex))
        {
            return;
        }
        
        List<FrontLinePoint> frontLines = frontLinesForMap.getFrontLines(pathAlongFrontData.getSide());  
        
        double minDistanceToNextWP = 5000;
        double actualDistanceToNextWP = 0.0;
        
        int nextFrontIndex = currentPointIndex;
        FrontLinePoint nextFrontLinePoint = null;
        while (actualDistanceToNextWP < minDistanceToNextWP)
        {
            nextFrontIndex = getNextFrontIndex(nextFrontIndex);
            nextFrontLinePoint = frontLines.get(nextFrontIndex);
            actualDistanceToNextWP = MathUtils.calcDist(nextFrontLinePoint.getPosition(), startPoint.getPosition());
            
            if (isEdgeOffMap(nextFrontIndex))
            {
                break;
            }
        }

        currentPointIndex = nextFrontIndex;
        coordinatesForPath.add(nextFrontLinePoint.getPosition());

        double distanceForThisLeg = MathUtils.calcDist(nextFrontLinePoint.getPosition(), startPoint.getPosition());
        remainingPathDistance -= distanceForThisLeg;
        
        if (remainingPathDistance > 0)
        {
            if (!isEdgeOffMap(currentPointIndex))
            {
                determineNextFrontPosition(nextFrontLinePoint, remainingPathDistance);
            }
        }
        else
        {
            return;
        }
    }

    protected boolean determineGoNorth() throws PWCGException 
    {
        final int closestToEdge = 10;
        goNorth = true;
        
        if (currentPointIndex < closestToEdge)
        {
            goNorth = false;
        }
        else
        {
            CoordinateBox missionBox = pathAlongFrontData.getMission().getMissionBorders();
            double distanceToNorth = MathUtils.calcDist(pathAlongFrontData.getTargetGeneralLocation(), missionBox.getNorth());
            double distanceToSouth = MathUtils.calcDist(pathAlongFrontData.getTargetGeneralLocation(), missionBox.getSouth());
            if (distanceToNorth < distanceToSouth)
            {
                goNorth = false;
            }
        }
        
        return goNorth;
    }

    protected boolean isEdgeOffMap(int frontIndex) throws PWCGException
    {
        if (goNorth && frontIndex < 4)
        {
            return true;
        }
        
        List<FrontLinePoint> frontLines = frontLinesForMap.getFrontLines(pathAlongFrontData.getSide());
        if ((!goNorth) && (frontIndex > (frontLines.size() - 4)))
        {
            return true;
        }
        
        return false;
    }
    

    protected int getNextFrontIndex(int nextFrontIndex) throws PWCGException
    {
        int frontIndex = nextFrontIndex;
        if (goNorth)
        {
            --frontIndex;
        }
        else
        {
            ++frontIndex;
        }
        return frontIndex;
    }
}
