package pwcg.mission.ground.org;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class GroundUnitPositionCalculatorLineAcross
{
    public static List<Coordinate> createLineAcrossVehiclePositions(Coordinate centralVehicleCoordinate, double facingAngle, int numvehicles, double spacing) throws PWCGException 
    {
        Coordinate firstVehicleCoordinate = calculateFirstUnitPlacement(centralVehicleCoordinate, numvehicles, spacing, facingAngle);
        List<Coordinate> vehicleLocations = calculateVehicleCoordinates(numvehicles, spacing, facingAngle, firstVehicleCoordinate);
        return vehicleLocations;        
    }

    private static Coordinate calculateFirstUnitPlacement(Coordinate centralVehicleCoordinate, int numvehicles, double spacing, double facingAngle) throws PWCGException
    {
        double initialPlacementOrientation = MathUtils.adjustAngle (facingAngle, 270.0);
        double distanceToFirstVehicle = spacing * (numvehicles / 2);
        Coordinate firstVehicleCoordinate = centralVehicleCoordinate.copy();
        if (distanceToFirstVehicle > 1.0)
        {
            firstVehicleCoordinate = MathUtils.calcNextCoord(centralVehicleCoordinate, initialPlacementOrientation, distanceToFirstVehicle);
        }
        return firstVehicleCoordinate;
    }

    private static List<Coordinate> calculateVehicleCoordinates(int numvehicles, double spacing, double facingAngle, Coordinate firstVehicleCoordinate)
            throws PWCGException
    {
        double placementOrientation = MathUtils.adjustAngle (facingAngle, 90.0);        
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
