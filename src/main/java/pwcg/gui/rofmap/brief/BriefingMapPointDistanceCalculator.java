package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class BriefingMapPointDistanceCalculator
{
    public static int calculateHeading(Coordinate previousPosition, Coordinate thisMapPoint) throws PWCGException
    {
        int headingAsInt = 0;
        if (previousPosition != null)
        {
            double angle = MathUtils.calcAngle(previousPosition, thisMapPoint);
            headingAsInt = Double.valueOf(angle).intValue();
        }
        return headingAsInt;
    }

    public static int calculateDistanceAsInteger(Coordinate previousPosition, Coordinate thisMapPoint)
    {
        int distanceAsInt = 0;
        if (previousPosition != null)
        {
            double distanceExact = MathUtils.calcDist(previousPosition, thisMapPoint);
            distanceAsInt = Double.valueOf(distanceExact).intValue();
        }

        return distanceAsInt;
    }

}
