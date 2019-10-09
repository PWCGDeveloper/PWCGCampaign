package pwcg.core.utils;

import pwcg.campaign.context.FrontParameters;
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
	    
	    // Avoid divide by zero
	    if (Math.abs(coord1.getXPos() - coord2.getXPos()) < 1.0)
	    {
	    	coord2.setXPos(coord2.getXPos() + 1.0);
	    }
	    
		angle = Math.toDegrees(Math.atan((coord2.getZPos() - coord1.getZPos())/ (coord2.getXPos() - coord1.getXPos())));
		
		if (coord1.getXPos() > coord2.getXPos())
	    {
	    	angle = 180 + angle;
	    	if (angle > 360)
	    	{
	    		angle = angle - 360;
	    	}
	    }
		
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

	public static Coordinate calcNextCoord(final Coordinate coord1, double angle, double distance) throws PWCGException 
	{
		if (distance == 0.0)
		{
			Coordinate sameCoord = coord1.copy();
			return sameCoord;
		}
		
		if (distance < 0.0)
		{
			distance = distance * -1.0;
			angle += 180.0;
			if (angle > 360.0)
			{
				angle -= 360.0;
			}
		}
		
		// All of this work is to convert from cartesian angle to polar angle
		double polarX = 0.0;
		double polarZ = 0.0;
		
		double xPolarDiff = distance * StrictMath.cos(Math.toRadians(angle));
		double zPolarDiff = distance * StrictMath.sin(Math.toRadians(angle));
		
		polarX = coord1.getXPos() + xPolarDiff;
		polarZ = coord1.getZPos() + zPolarDiff;
		
		// limit checks
		FrontParameters frontParameters = PWCGContext.getInstance().getCurrentMap().getFrontParameters();

		if (polarX < 0.0)
		{
			polarX = 1000.0;
		}
		if (polarX > frontParameters.getxMax())
		{
			polarX = frontParameters.getxMax() - FrontParameters.MIN_DISTANCE_FROM_BORDER;
		}
        else if (polarX < frontParameters.getxMin())
        {
            polarX = frontParameters.getxMin() + FrontParameters.MIN_DISTANCE_FROM_BORDER;
        }

		if (polarZ < 0.0)
		{
			polarZ = 1000.0;
		}
		if (polarZ > frontParameters.getzMax())
		{
			polarZ = frontParameters.getzMax() - FrontParameters.MIN_DISTANCE_FROM_BORDER;
		}
		
		// Get the angle
		Coordinate polarCoord = new Coordinate();
		polarCoord.setXPos(polarX);
		polarCoord.setZPos(polarZ);
		double polarAngle = calcAngle(coord1, polarCoord);
		
		// Now calculate the new coordinates in cartesian terms
		
		double zDiff = distance * StrictMath.sin(Math.toRadians(polarAngle));  // This is reversed because x is n/s
		double xDiff = distance * StrictMath.cos(Math.toRadians(polarAngle));  // This is reversed because z is e/w
		
		// more limit checks
		double cartesianX = coord1.getXPos() + xDiff;
		double cartesianZ = coord1.getZPos() + zDiff;
		if (cartesianX < 0.0)
		{
			cartesianX = FrontParameters.MIN_DISTANCE_FROM_BORDER;
		}
		if (cartesianX > frontParameters.getxMax())
		{
			cartesianX = frontParameters.getxMax() - FrontParameters.MIN_DISTANCE_FROM_BORDER;
		}
		
		if (cartesianZ < 0.0)
		{
			cartesianZ = FrontParameters.MIN_DISTANCE_FROM_BORDER;
		}
		if (cartesianZ > frontParameters.getzMax())
		{
			cartesianZ = frontParameters.getzMax() - FrontParameters.MIN_DISTANCE_FROM_BORDER;
		}
		

		Coordinate cartesianCoord = new Coordinate();
		cartesianCoord.setXPos(cartesianX); // X moves from 0 to max (south to north)
		cartesianCoord.setZPos(cartesianZ); // Y moves from 0 to max (west to east)

		//double cartesianAngle = calcAngle(coord1, cartesianCoord);
		
		//double newDist = calcDist (coord1, cartesianCoord);
	    
		return cartesianCoord;
	}

    public static Coordinate calcNextCoordDogleg(final Coordinate coord1, double angle, double distanceZ, double distanceX) throws PWCGException 
    {
        Coordinate newCoordinate = coord1.copy();
        if (distanceZ != 0.0)
        {
            double angleZ = MathUtils.adjustAngle(angle, 90.0);
            newCoordinate = calcNextCoord(newCoordinate, angleZ, distanceZ);
        }
        
        if (distanceX != 0.0)
        {
            newCoordinate = calcNextCoord(newCoordinate, angle, distanceX);
        }

        return newCoordinate;
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
}
