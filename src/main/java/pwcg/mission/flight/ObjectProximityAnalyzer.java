package pwcg.mission.flight;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;

public class ObjectProximityAnalyzer
{
    private Flight playerFlight;

    public ObjectProximityAnalyzer (Flight playerFlight)
    {
        this.playerFlight = playerFlight;
    }

    public double plotProximityToPlayerFlightPath(Coordinate objectCoordinate) throws PWCGException 
    {
        // Plot the minute by minute path of each flight
        FlightPathPlotter virtualWaypointPlotter = new FlightPathPlotter();
        List<Coordinate> playerFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(playerFlight);

        double closestDistanceToThatFlight = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        for (Coordinate playerFlightDataPoint : playerFlightPath)
        {
            double distance = MathUtils.calcDist(playerFlightDataPoint, objectCoordinate);
            if (distance < closestDistanceToThatFlight)
            {
                closestDistanceToThatFlight = distance;
            }
        }
        
        return closestDistanceToThatFlight;
    }
}
