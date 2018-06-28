package pwcg.mission.flight.waypoint;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.plane.Plane;

public class FormationGenerator
{
    public List<Coordinate> createPlaneInitialPosition(
                    List <Plane> planes, 
                    Coordinate startPosition,
                    Orientation orientation) throws PWCGException 
    {
        List<Coordinate> flightCoordinates = new ArrayList<Coordinate>();
        
        Coordinate leadPlaneCoords = startPosition.copy();
        flightCoordinates.add(leadPlaneCoords.copy());
        
        for (int i = 1; i < planes.size(); ++i)
        {
            double echelonLeftAngle = MathUtils.adjustAngle(orientation.getyOri(), 330);
            double metersSpacing = 120.0;
            Coordinate nextPlaneCoords = MathUtils.calcNextCoord(leadPlaneCoords, echelonLeftAngle, metersSpacing * i);
            nextPlaneCoords.setYPos(leadPlaneCoords.getYPos() + (80 * i));
            
            flightCoordinates.add(nextPlaneCoords);
        }
        
        return flightCoordinates;
    }
}
