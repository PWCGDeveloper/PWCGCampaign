package pwcg.campaign.group.airfield;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;

public class RunwayBlockageDetector
{
    private static int RUNWAY_EXCLUSION_ZONE_FROM_START = 2500;
    
    public static boolean isRunwayBlocked(FrontMapIdentifier map, Airfield airfield, Coordinate coordinate) throws PWCGException
    {
      
        List<Coordinate> checkCoordinatesForRunways = getCheckCoordinatesForRunways(map, airfield);
        for (Coordinate runwayCoordinate : checkCoordinatesForRunways)
        {
            CoordinateBox runwayExclusionZone = CoordinateBox.coordinateBoxFromCenter(map, runwayCoordinate, 100);
            if (runwayExclusionZone.isInBox(coordinate))
            {
                return true;
            }
        }
        
        return false;
    }
    

    private static  List<Coordinate> getCheckCoordinatesForRunways(FrontMapIdentifier map, Airfield airfield) throws PWCGException
    {
        List<Coordinate> checkCoordinatesForRunways = new ArrayList<>();
        for (Runway runway : airfield.getAllRunways())
        {
            final int increment = 50;
            final int runwayLength = RUNWAY_EXCLUSION_ZONE_FROM_START;
            final int numIncrements = runwayLength / increment;

            Coordinate runwayCoordinate = runway.getStartPos().copy();
            double runwayAngle = runway.getHeading();
            checkCoordinatesForRunways.add(runwayCoordinate);
            for (int i = 0; i < numIncrements; ++i)
            {
                runwayCoordinate = MathUtils.calcNextCoord(map, runwayCoordinate, runwayAngle, increment);
                checkCoordinatesForRunways.add(runwayCoordinate);
            }
        }
        return checkCoordinatesForRunways;
    }
}
