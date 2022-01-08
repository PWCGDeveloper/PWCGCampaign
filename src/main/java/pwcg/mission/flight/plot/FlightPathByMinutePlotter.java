package pwcg.mission.flight.plot;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;

public class FlightPathByMinutePlotter
{
    public List<Coordinate> plotCoordinatesByMinute(IFlight flight) throws PWCGException 
    {        
        List<MissionPoint> allMissionCoordinates = flight.getWaypointPackage().getMissionPoints();
        if (allMissionCoordinates == null || allMissionCoordinates.size() < 2)
        {
            return this.generatePathNoWaypoints(flight);
        }
        
        double cruiseSpeedKPH = flight.getFlightCruisingSpeed();
        double movementPerInterval = (cruiseSpeedKPH / 60) * 1000;

        List<Coordinate> flightPath = new ArrayList<Coordinate>();
        for (int index = 1; index < allMissionCoordinates.size(); ++index)
        {
            Coordinate legStartPosition = allMissionCoordinates.get(index-1).getPosition();
            Coordinate legEndPosition = allMissionCoordinates.get(index).getPosition();            
            List<Coordinate> vwpForLeg = generatePlotPointsForLeg(movementPerInterval, legStartPosition, legEndPosition);
            flightPath.addAll(vwpForLeg);
        }

        return flightPath;
    }

    private List<Coordinate> generatePlotPointsForLeg(
                    double movementPerInterval, 
                    Coordinate legStartPosition,
                    Coordinate legEndPosition) throws PWCGException
    {
        List<Coordinate> flightPathForLeg = new ArrayList<Coordinate>();
        double wpDistance = MathUtils.calcDist(legStartPosition, legEndPosition);
        double numVirtualWp = (wpDistance / movementPerInterval) + 1;        
        double angle = MathUtils.calcAngle(legStartPosition, legEndPosition);
        
        Coordinate flightPosition = legStartPosition.copy();        
        for (int numPlotPointsBetweenWP = 0; numPlotPointsBetweenWP < numVirtualWp; ++numPlotPointsBetweenWP)
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

    private List<Coordinate> generatePathNoWaypoints(IFlight flight)
    {
        List<Coordinate> circlingFlightPath = new ArrayList<Coordinate>();
        PlaneMcu leadPlane = flight.getFlightPlanes().getFlightLeader();
        for (int i = 0; i < 240; ++i)
        {
            circlingFlightPath.add(leadPlane.getPosition());
        }
        
        return circlingFlightPath;
    }
}