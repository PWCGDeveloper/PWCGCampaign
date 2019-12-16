package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.plane.PlaneMCU;

public class FlightPathPlotter
{
    public List<Coordinate> plotCoordinatesByMinute(Flight flight) throws PWCGException 
    {        
        List<Coordinate> allMissionCoordinates = flight.getAllMissionCoordinates();
        if (allMissionCoordinates == null || allMissionCoordinates.size() < 2)
        {
            return this.generatePathNoWaypoints(flight);
        }
        
        double cruiseSpeedKPH = flight.getFlightCruisingSpeed();
        double movementPerInterval = (cruiseSpeedKPH / 60) * 1000;

        List<Coordinate> flightPath = new ArrayList<Coordinate>();
        for (int index = 1; index < allMissionCoordinates.size(); ++index)
        {
            Coordinate legStartPosition = allMissionCoordinates.get(index-1);
            Coordinate legEndPosition = allMissionCoordinates.get(index);            
            List<Coordinate> vwpForLeg = generateVwpForLeg(movementPerInterval, legStartPosition, legEndPosition);
            flightPath.addAll(vwpForLeg);
        }

        return flightPath;
    }

    private List<Coordinate> generateVwpForLeg(
                    double movementPerInterval, 
                    Coordinate legStartPosition,
                    Coordinate legEndPosition) throws PWCGException
    {
        List<Coordinate> flightPathForLeg = new ArrayList<Coordinate>();
        double wpDistance = MathUtils.calcDist(legStartPosition, legEndPosition);
        double numVirtualWp = (wpDistance / movementPerInterval) + 1;        
        double angle = MathUtils.calcAngle(legStartPosition, legEndPosition);
        
        Coordinate flightPosition = legStartPosition.copy();        
        for (int numVirtWpBetweenWP = 0; numVirtWpBetweenWP < numVirtualWp; ++numVirtWpBetweenWP)
        {
            Coordinate nextCoordinate = calculateNextFlightPathCoordinate(movementPerInterval, legEndPosition, angle, flightPosition);
            flightPathForLeg.add(nextCoordinate);
            flightPosition = nextCoordinate;
        }
        
        return flightPathForLeg;
    }

    private Coordinate calculateNextFlightPathCoordinate(double movementPerInterval, Coordinate legEndPosition, double angle, Coordinate flightPosition) throws PWCGException
    {
        double distanceToWP = MathUtils.calcDist(flightPosition, legEndPosition);
        Coordinate nextCoordinate = null;
        if (distanceToWP > movementPerInterval)
        {
            nextCoordinate = MathUtils.calcNextCoord(flightPosition, angle, movementPerInterval);
        }
        else
        {
            nextCoordinate = legEndPosition.copy();
        }
        return nextCoordinate;
    }

    private List<Coordinate> generatePathNoWaypoints(Flight flight)
    {
        List<Coordinate> circlingFlightPath = new ArrayList<Coordinate>();
        PlaneMCU leadPlane = flight.getPlanes().get(0);
        for (int i = 0; i < 60; ++i)
        {
            circlingFlightPath.add(leadPlane.getPosition());
        }
        
        return circlingFlightPath;
    }
}