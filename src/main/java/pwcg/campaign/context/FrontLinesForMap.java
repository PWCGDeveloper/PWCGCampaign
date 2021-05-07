package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.core.utils.RandomNumberGenerator;

public class FrontLinesForMap 
{
    public static final double NOT_BEHIND_ENEMY_LINES = -1.0;

    private List<FrontLinePoint>frontLinesAllied = new ArrayList<FrontLinePoint>();
    private List<FrontLinePoint>frontLinesAxis = new ArrayList<FrontLinePoint>();
    private List<Date> frontDates = new ArrayList<Date>();
    private String mapName = null;

    public FrontLinesForMap (String mapName) throws PWCGException
    {
        this.mapName = mapName;
    }

    public Date getEarliestDateForMap()
    {
        return frontDates.get(0);
    }

    public void configureForDate(Date frontDate) throws PWCGException  
    {
        frontLinesAllied.clear();
        frontLinesAxis.clear();
        
        String dateDir = DateUtils.getDateStringYYYYMMDD(frontDate);
        String frontFilePath = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\" + dateDir +  "\\";			
        read(frontFilePath);
    }

    public Coordinate getCoordinates(int index, Side side ) throws PWCGException 
    {
        try 
        {
            List<FrontLinePoint>frontLines = findAllFrontLinesForSide(side);        
            return frontLines.get(index).getPosition().copy();
        }
        catch (Exception e)
        {
            throw new PWCGException("Invalid front line index: " + index);
        }
    }

    public double getOrientation(int index, Side side, Date date) throws PWCGException 
    {
        List<FrontLinePoint>frontLines = findAllFrontLinesForSide(side);
        
        return frontLines.get(index).getOrientation(date);
    }

    public List<FrontLinePoint> getFrontLines(Side side) throws PWCGException 
    {
        List<FrontLinePoint>frontLines = findAllFrontLinesForSide(side);
        
        return frontLines;
   }

    private void read (String filePath) throws PWCGException 
    {
        LocationSet frontLineLocations = LocationIOJson.readJson(filePath, "FrontLines");
        
        for (PWCGLocation frontLineLocation : frontLineLocations.getLocations())
        {
            FrontLinePoint frontLinePoint = new FrontLinePoint();
            frontLinePoint.setLocation(frontLineLocation);
            
            List<FrontLinePoint>frontLines = findAllFrontLinesForSide(frontLinePoint.getCountry().getSide());
            frontLines.add(frontLinePoint);
        }
    }

    public int findIndexForClosestPosition(Coordinate referenceLocation, Side side ) throws PWCGException 
    {
        List<FrontLinePoint>frontLines = findAllFrontLinesForSide(side);
        FrontLinePoint closestPosition = this.findClosestFrontPositionForSide(referenceLocation, side);
        return frontLines.indexOf(closestPosition);
    }

    public double findClosestFriendlyPositionAngle(Coordinate source, Side side) throws PWCGException 
    {
        List<FrontLinePoint>frontLines = findAllFrontLinesForSide(side);

        int closestPositionIndex = findIndexForClosestPosition(source, side);
        int nextPositionIndex = closestPositionIndex + 1;
        
        double angle = MathUtils.calcAngle(
                        frontLines.get(closestPositionIndex).getPosition(), 
                        frontLines.get(nextPositionIndex).getPosition());

        return angle;
    }

    public double findClosestFriendlyPositionDistance(Coordinate referenceLocation, Side side) throws PWCGException 
    {
        PositionFinder<FrontLinePoint> positionFinder = new PositionFinder<FrontLinePoint>();
        List<FrontLinePoint>frontLines = findAllFrontLinesForSide(side);
        FrontLinePoint closestFrontLinePoint = positionFinder.selectClosestPosition(frontLines, referenceLocation);
        return MathUtils.calcDist(referenceLocation, closestFrontLinePoint.getPosition());
    }


    public double findClosestEnemyPositionAngle(Coordinate source, Side side) throws PWCGException 
    {
        FrontLinePoint closestFriendlyPosition = this.findClosestFrontPositionForSide(source, side);
        FrontLinePoint closestEnemyPosition = this.findClosestFrontPositionForSide(source, side.getOppositeSide());
        double angle = MathUtils.calcAngle(closestFriendlyPosition.getPosition(), closestEnemyPosition.getPosition());
        return angle;
    }
    
    public boolean isFarFromFront (Coordinate position, Side side, Date date) throws PWCGException
    {
        double closestFrontDistance = findClosestFriendlyPositionDistance(position, side);
        if (closestFrontDistance > 30000)
        {
            return true;
        }

        return false;
    }

    public boolean isCoordinateAllied(Coordinate source) throws PWCGException 
    {
        FrontLinePoint closestAlliedPosition = this.findClosestFrontPositionForSide(source, Side.ALLIED);
        FrontLinePoint closestAxisPosition = this.findClosestFrontPositionForSide(source, Side.AXIS);

        double alliedDistance = MathUtils.calcDist(source, closestAlliedPosition.getPosition());
        double axisDistance = MathUtils.calcDist(source, closestAxisPosition.getPosition());

        boolean isCoordinateAllied = false;
        if (alliedDistance < axisDistance)
        {
            isCoordinateAllied =  true;
        }

        return isCoordinateAllied;
    }

    public List<FrontLinePoint> findAllFrontLinesForSide(Side side) throws PWCGException
    {
        List<FrontLinePoint> frontLines = null;
        
        if (side == Side.ALLIED)
        {
            frontLines = frontLinesAllied;
        }
        else if (side == Side.AXIS)
        {
            frontLines = frontLinesAxis;
        }
        else
        {
            throw new PWCGException("Attempt to get front lines for neutral");
        }

        return frontLines;
    }

    public Coordinate findPositionBehindLinesForSide (Coordinate referencePoint, double radius, int minDistance, int maxDistance, Side side) throws PWCGException 
    {
        FrontLinePoint targetCountryFrontPoint = findCloseFrontPositionForSide(referencePoint, radius, side);
        FrontLinePoint oppositeCountryFrontPoint = findCloseFrontPositionForSide(targetCountryFrontPoint.getPosition(), 1000, side.getOppositeSide());
        double angleBehindEnemyLines = MathUtils.calcAngle(oppositeCountryFrontPoint.getPosition(), targetCountryFrontPoint.getPosition());
        double distance = minDistance + RandomNumberGenerator.getRandom(maxDistance - minDistance);
        Coordinate behindLinesPosition = MathUtils.calcNextCoord(targetCountryFrontPoint.getPosition(), angleBehindEnemyLines, distance);
        return behindLinesPosition;
    }

    public FrontLinePoint findCloseFrontPositionForSide(Coordinate generalTargetLocation, double radius, Side side) throws PWCGException
    {
        List<FrontLinePoint> frontPointsWithinRadius = findClosestFrontPositionsForSide(generalTargetLocation, radius, side);
        int index = RandomNumberGenerator.getRandom(frontPointsWithinRadius.size());
        return frontPointsWithinRadius.get(index);
    }

    public List<FrontLinePoint> findClosestFrontPositionsForSide(Coordinate referenceLocation, double initialRadius, Side side) throws PWCGException 
    {
        PositionFinder<FrontLinePoint> positionFinder = new PositionFinder<FrontLinePoint>();
        List<FrontLinePoint> frontLines = findAllFrontLinesForSide(side);
        List<FrontLinePoint> frontPointsWithinRadius = positionFinder.findWithinExpandingRadius(frontLines, referenceLocation, initialRadius, 2000000.0);
        return frontPointsWithinRadius;
    }

    public List<FrontLinePoint> findFrontPositionsForSideInBox(CoordinateBox coordinateBox, Side side) throws PWCGException 
    {
        PositionFinder<FrontLinePoint> positionFinder = new PositionFinder<FrontLinePoint>();
        List<FrontLinePoint> frontLines = findAllFrontLinesForSide(side);
        List<FrontLinePoint> frontPointsWithinRadius = positionFinder.findWithinBox(frontLines, coordinateBox);
        return frontPointsWithinRadius;
    }

    public FrontLinePoint findClosestFrontPositionForSide(Coordinate referenceLocation, Side side) throws PWCGException 
    {
        PositionFinder<FrontLinePoint> positionFinder = new PositionFinder<FrontLinePoint>();
        List<FrontLinePoint>frontLines = findAllFrontLinesForSide(side);
        FrontLinePoint closestFrontLinePoint = positionFinder.selectClosestPosition(frontLines, referenceLocation);
        return closestFrontLinePoint;
    }

    public Coordinate findClosestFrontCoordinateForSide(Coordinate referenceLocation, Side side) throws PWCGException 
    {
        return findClosestFrontPositionForSide(referenceLocation, side).getPosition();
    }
    
    public FrontLinePoint getFirstPositionForSide(Side side) throws PWCGException
    {        
        if (side == Side.ALLIED)
        {
            return frontLinesAllied.get(0);
        }
        else
        {
            return frontLinesAxis.get(0);
        }
    }
    
    public FrontLinePoint getLastPositionForSide(Side side) throws PWCGException
    {        
        if (side == Side.ALLIED)
        {
            return frontLinesAllied.get(frontLinesAllied.size()-1);
        }
        else
        {
            return frontLinesAxis.get(frontLinesAxis.size()-1);
        }
    }

    public void deletePoint(int frontIndexToDelete, Side side) throws PWCGException
    {
        List<FrontLinePoint> newFrontLines = new ArrayList<FrontLinePoint>();
        
        List<FrontLinePoint>frontLines = findAllFrontLinesForSide(side);

        for (int i = 0; i < frontLines.size(); ++i)
        {
            if (i != frontIndexToDelete)
            {
                FrontLinePoint frontPoint = frontLines.get(i);
                newFrontLines.add(frontPoint);
            }
        }
        
        if (side == Side.ALLIED)
        {
            frontLinesAllied = newFrontLines;
        }
        else if (side == Side.AXIS)
        {
            frontLinesAxis = newFrontLines;
        }
        else
        {
            throw new PWCGException ("Attempt to delete point for neutral front lines");
        }
    }

    public String getFrontFile() 
    {
        return mapName;
    }

    public void setFrontFile(String frontFile) 
    {
        this.mapName = frontFile;
    }

    public void setFrontLinesAllied(List<FrontLinePoint> frontLinesAllied)
    {
        this.frontLinesAllied = frontLinesAllied;
    }


    public void setFrontLinesAxis(List<FrontLinePoint> frontLinesAxis)
    {
        this.frontLinesAxis = frontLinesAxis;
    }

	public List<FrontLinePoint> getFrontLinesAllied()
	{
		return frontLinesAllied;
	}

	public List<FrontLinePoint> getFrontLinesAxis()
	{
		return frontLinesAxis;
	}
}
