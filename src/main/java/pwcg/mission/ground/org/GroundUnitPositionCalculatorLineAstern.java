package pwcg.mission.ground.org;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class GroundUnitPositionCalculatorLineAstern
{
    public static List<Coordinate> createLineAsternVehiclePositions(Coordinate firstVehicleCoordinate, double facingAngle, int numvehicles, double spacing) throws PWCGException 
    {
        List<Coordinate> vehicleLocations = calculateVehicleCoordinates(numvehicles, spacing, facingAngle, firstVehicleCoordinate);
        return vehicleLocations;        
    }

    private static List<Coordinate> calculateVehicleCoordinates(int numvehicles, double spacing, double facingAngle, Coordinate firstVehicleCoordinate)
            throws PWCGException
    {
        double placementOrientation = MathUtils.adjustAngle (facingAngle, 180.0);        
        Coordinate unitCoords = firstVehicleCoordinate.copy();
        List<Coordinate> vehicleLocations = new ArrayList<>();
        for (int i = 0; i < numvehicles; ++i)
        {   
            vehicleLocations.add(unitCoords);
            unitCoords = MathUtils.calcNextCoord(unitCoords, placementOrientation, spacing);
        }
        return vehicleLocations;
    }

}
