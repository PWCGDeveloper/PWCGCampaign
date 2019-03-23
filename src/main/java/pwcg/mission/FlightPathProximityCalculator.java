package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.VirtualWaypointPlotter;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;

public class FlightPathProximityCalculator
{
	List<Flight> playerFlights;
    
    public FlightPathProximityCalculator (List<Flight> playerFlights)
    {
        this.playerFlights = playerFlights;
    }
    
    public boolean isInFlightPath(Coordinate position) throws PWCGException 
    {
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        for (Flight playerFlight : playerFlights)
        {
	        List<VirtualWayPointCoordinate> vwpCoordinates = virtualWaypointPlotter.plotCoordinatesByMinute(playerFlight);
	        for (VirtualWayPointCoordinate vwpCoordinate : vwpCoordinates)
	        {
	            double distanceToPlayer = MathUtils.calcDist(vwpCoordinate.getCoordinate(), position);
	            if (distanceToPlayer < 15000)
	            {
	                return true;
	            }
	        }
        }

        return false;
    }

}
