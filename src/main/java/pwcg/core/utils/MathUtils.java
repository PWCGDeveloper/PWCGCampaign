package pwcg.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.context.MapArea;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MathUtils 
{

	public static double calcDist (final Coordinate item, final Coordinate compareItem)
	{
		double xdist = Math.abs(item.getXPos() - compareItem.getXPos());
		double zdist = Math.abs(item.getZPos() - compareItem.getZPos());
		double distance = Math.sqrt((xdist * xdist) + (zdist * zdist));

		return distance;
	}

	public static double calcAngle(final Coordinate coord1, final Coordinate coord2) throws PWCGException 
	{
	    double angle = 0.0;
	    
		angle = Math.toDegrees(Math.atan2(coord2.getZPos() - coord1.getZPos(), coord2.getXPos() - coord1.getXPos()));
		
		if (angle > 360.0)
		{
			angle -= 360.0;
		}

		if (angle < 0.0)
		{
			angle += 360.0;
		}

        if (Double.isNaN(angle))
		{
		    throw new PWCGException("Invalid values passed to calcAngle: " + coord1.toString() + "   " + coord2.toString());
		}
		
		return angle;
	}

	public static double adjustAngle(double angle, double adjustment)
	{
	    angle += adjustment;
		if (angle < 0)
		{
			angle += 360.0;
		}

		if (angle > 360.0)
		{
			angle -= 360.0;
		}

		return angle;
	}

    public static double calcNumberOfDegrees(double angle1, double angle2)
    {
        double degrees = Math.abs(angle1 - angle2);
        if (degrees > 180)
        {
            degrees = 360 - degrees;
        }
        
        return degrees;
    }

    public static Coordinate calcNextCoordWithMapAdjustments(final Coordinate coord1, double angle, double distance) throws PWCGException 
    {
        Coordinate unadjustedCartesianCoord = calcNextCoord(coord1, angle, distance);
        MapArea usableMapArea = PWCGContext.getInstance().getCurrentMap().getUsableMapArea();
        double cartesianX = adjustXForMap(unadjustedCartesianCoord.getXPos(), usableMapArea);
        double cartesianZ = adjustZForMap(unadjustedCartesianCoord.getZPos(), usableMapArea);

        Coordinate cartesianCoord = new Coordinate();
        cartesianCoord.setXPos(cartesianX); // X moves from 0 to max (south to north)
        cartesianCoord.setZPos(cartesianZ); // Z moves from 0 to max (west to east)
        return cartesianCoord;
    }

	public static Coordinate calcNextCoord(final Coordinate coord1, double angle, double distance) throws PWCGException 
	{
		if (distance == 0.0)
		{
			Coordinate sameCoord = coord1.copy();
			return sameCoord;
		}
		
		if (distance < 0.0)
		{
			distance = Math.abs(distance);
			angle += adjustAngle(angle, 180);
		}
		
		// All of this work is to convert from cartesian angle to polar angle
		double xPolarDiff = distance * StrictMath.cos(Math.toRadians(angle));
		double zPolarDiff = distance * StrictMath.sin(Math.toRadians(angle));		
		
        double polarX = coord1.getXPos() + xPolarDiff;
		double polarZ = coord1.getZPos() + zPolarDiff;
		
		// Get the angle
		Coordinate polarCoord = new Coordinate();
		polarCoord.setXPos(polarX);
		polarCoord.setZPos(polarZ);
		double polarAngle = calcAngle(coord1, polarCoord);
		
		// Now calculate the new coordinates in cartesian terms
		double zDiff = distance * StrictMath.sin(Math.toRadians(polarAngle));  // This is reversed because x is n/s
		double xDiff = distance * StrictMath.cos(Math.toRadians(polarAngle));  // This is reversed because z is e/w
		
        MapArea mapArea = PWCGContext.getInstance().getCurrentMap().getMapArea();

		double cartesianX = coord1.getXPos() + xDiff;		
        cartesianX = adjustXForMap(cartesianX, mapArea);

        double cartesianZ = coord1.getZPos() + zDiff;
        cartesianZ = adjustZForMap(cartesianZ, mapArea);

		Coordinate cartesianCoord = new Coordinate();
		cartesianCoord.setXPos(cartesianX); // X moves from 0 to max (south to north)
		cartesianCoord.setZPos(cartesianZ); // Z moves from 0 to max (west to east)
		return cartesianCoord;
	}

    private static double adjustXForMap(double xValueInMeters, MapArea mapArea)
    {
        if (xValueInMeters > mapArea.getxMax())
        {
            xValueInMeters = mapArea.getxMax() - 100;
        }
        else if (xValueInMeters < mapArea.getxMin())
        {
            xValueInMeters = mapArea.getxMin() + 100;
        }
        return xValueInMeters;
    }

    private static double adjustZForMap(double zValueInMeters, MapArea mapArea)
    {
        if (zValueInMeters > mapArea.getzMax())
        {
            zValueInMeters = mapArea.getzMax() - 100;
        }
        else if (zValueInMeters < mapArea.getzMin())
        {
            zValueInMeters = mapArea.getzMin() + 100;
        }
        return zValueInMeters;
    }
	
	public static String numberToBinaryForm(int number)
	{
		StringBuffer binaryRepresentation = new StringBuffer("");
		numberToBinaryFormCalculator (binaryRepresentation, number);
		return binaryRepresentation.toString();
	}
	
    private static void numberToBinaryFormCalculator(StringBuffer binaryRepresentation, int number) 
    {
        int remainder;
        if (number <= 1) 
        {
        	binaryRepresentation.append(number);
            return;
        }

        remainder = number %2; 
        int shifterNumber = number >> 1;
        numberToBinaryFormCalculator(binaryRepresentation, shifterNumber);
    	binaryRepresentation.append(remainder);
    }

    public static double distFromLine(final Coordinate lineStart, final Coordinate lineEnd, final Coordinate coord) throws PWCGException
    {
        double xDist = lineEnd.getXPos() - lineStart.getXPos();
        double zDist = lineEnd.getZPos() - lineStart.getZPos();
        double distSquared = xDist * xDist + zDist * zDist;

        if (distSquared == 0)
            return calcDist(lineStart, coord);

        double t = ((coord.getXPos() - lineStart.getXPos()) * xDist + (coord.getZPos() - lineStart.getZPos()) * zDist) / distSquared;
        t = Math.max(0, Math.min(1,  t));

        Coordinate proj = new Coordinate();
        proj.setXPos(lineStart.getXPos() + t * xDist);
        proj.setZPos(lineStart.getZPos() + t * zDist);

        return calcDist(proj, coord);
    }

    private static Integer angleCompare(final Coordinate prevPoint, final Coordinate base, final Coordinate a, final Coordinate b)
    {
        try {
            double base_angle = 0;
            if (prevPoint != null)
            {
                base_angle = calcAngle(prevPoint, base);
            }
            double a_angle = adjustAngle(calcAngle(base, a), -base_angle);
            double b_angle = adjustAngle(calcAngle(base, b), -base_angle);
            return Double.compare(a_angle, b_angle);
        } catch (PWCGException e) {
            return null;
        }
    }

    public static List<Coordinate> convexHull(Collection<Coordinate> points)
    {
        Set<Coordinate> pointSet = new HashSet<>();
        List<Coordinate> hull = new ArrayList<>();

        for (Coordinate point : points)
        {
            Coordinate newPoint = point.copy();
            newPoint.setYPos(0);
            pointSet.add(newPoint);
        }

        Comparator<Coordinate> comparator = (a, b) -> (a.getZPos() == b.getZPos()) ? Double.compare(a.getXPos(), b.getXPos()) : Double.compare(a.getZPos(), b.getZPos());
        Coordinate prevPoint = null;
        Coordinate pointOnHull = pointSet.stream().min(comparator).get();

        while (hull.indexOf(pointOnHull) == -1)
        {
            hull.add(pointOnHull);
            Coordinate finalPoint = pointOnHull;
            Coordinate finalPrev = prevPoint;
            pointOnHull = pointSet.stream().filter(x -> comparator.compare(finalPoint, x) != 0).min((a, b) -> angleCompare(finalPrev, finalPoint, a, b)).get();
            prevPoint = finalPoint;
        }

        assert(hull.indexOf(pointOnHull) == 0);

        return hull;
    }

    public static boolean pointInsidePolygon(Coordinate point, List<Coordinate> polygon)
    {
        boolean inside = false;

        Coordinate a = polygon.get(polygon.size() - 1);

        for (Coordinate b : polygon)
        {
            if ((a.getXPos() > point.getXPos()) != (b.getXPos() > point.getXPos()))
            {
                double t = (point.getXPos() - a.getXPos()) / (b.getXPos() - a.getXPos());
                double intersection_z = ((b.getZPos() - a.getZPos()) * t) + a.getZPos();
                if (intersection_z > point.getZPos())
                    inside = !inside;
            }
            a = b;
        }

        return inside;
    }

    public static double polygonArea(List<Coordinate> polygon)
    {
        double area = 0;

        Coordinate a = polygon.get(polygon.size() - 1);

        for (Coordinate b : polygon)
        {
            area += (b.getZPos() - a.getZPos()) * (a.getXPos() + b.getXPos()) / 2.0;
            a = b;
        }

        return area;
    }
}
