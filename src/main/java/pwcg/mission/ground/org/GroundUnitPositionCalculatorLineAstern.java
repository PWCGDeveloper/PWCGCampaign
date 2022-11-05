package pwcg.mission.ground.org;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class GroundUnitPositionCalculatorLineAstern
{
    public static List<Coordinate> createLineAsternVehiclePositions(FrontMapIdentifier mapidentifier, Coordinate firstVehicleCoordinate, double facingAngle, int numvehicles, double spacing) throws PWCGException 
    {
        List<Coordinate> vehicleLocations = calculateVehicleCoordinates(mapidentifier, numvehicles, spacing, facingAngle, firstVehicleCoordinate);
        return vehicleLocations;        
    }

    private static List<Coordinate> calculateVehicleCoordinates(FrontMapIdentifier mapidentifier, int numvehicles, double spacing, double facingAngle, Coordinate firstVehicleCoordinate)
            throws PWCGException
    {
        double placementOrientation = MathUtils.adjustAngle (facingAngle, 180.0);        
        Coordinate unitCoords = firstVehicleCoordinate.copy();
        List<Coordinate> vehicleLocations = new ArrayList<>();
        for (int i = 0; i < numvehicles; ++i)
        {   
            vehicleLocations.add(unitCoords);
            unitCoords = MathUtils.calcNextCoord(mapidentifier, unitCoords, placementOrientation, spacing);
        }
        return vehicleLocations;
    }

}
