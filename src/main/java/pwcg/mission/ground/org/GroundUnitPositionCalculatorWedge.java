package pwcg.mission.ground.org;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class GroundUnitPositionCalculatorWedge
{
    public static List<Coordinate> createWedgeVehiclePositions(FrontMapIdentifier mapidentifier, Coordinate firstVehicleCoordinate, double facingAngle, int numvehicles, double spacing) throws PWCGException 
    {
        List<Coordinate> vehicleLocations = calculateVehicleCoordinates(mapidentifier, numvehicles, spacing, facingAngle, firstVehicleCoordinate);
        return vehicleLocations;        
    }

    private static List<Coordinate> calculateVehicleCoordinates(FrontMapIdentifier mapidentifier, int numvehicles, double spacing, double facingAngle, Coordinate firstVehicleCoordinate)
            throws PWCGException
    {
        List<Coordinate> vehicleLocations = new ArrayList<>();
        for (int i = 0; i < numvehicles; ++i)
        {   
            if (i == 0)
            {
                vehicleLocations.add(firstVehicleCoordinate.copy());
            }
            else if ((i % 2) == 0)
            {
                double placementOrientation = MathUtils.adjustAngle (facingAngle, 130.0); 
                double distance = spacing * ((i / 2) + 1);
                Coordinate unitCoords = MathUtils.calcNextCoord(mapidentifier, firstVehicleCoordinate, placementOrientation, distance);
                vehicleLocations.add(unitCoords);
            } 
            else
            {
                double placementOrientation = MathUtils.adjustAngle (facingAngle, 240.0); 
                double distance = spacing * ((i / 2) + 1);
                Coordinate unitCoords = MathUtils.calcNextCoord(mapidentifier, firstVehicleCoordinate, placementOrientation, distance);
                vehicleLocations.add(unitCoords);
            }
        }
        return vehicleLocations;
    }

}
