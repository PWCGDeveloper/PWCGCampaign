package pwcg.mission.flight;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;

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
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        List<VirtualWayPointCoordinate> playerFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(playerFlight);

        double closestDistanceToThatFlight = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        for (VirtualWayPointCoordinate playerFlightDataPoint : playerFlightPath)
        {
            double distance = MathUtils.calcDist(playerFlightDataPoint.getCoordinate(), objectCoordinate);
            if (distance < closestDistanceToThatFlight)
            {
                closestDistanceToThatFlight = distance;
            }
        }
        
        return closestDistanceToThatFlight;
    }
}
