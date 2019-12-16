package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPathPlotter;

public class FlightPathProximityCalculator
{
	List<Flight> playerFlights;
    
    public FlightPathProximityCalculator (List<Flight> playerFlights)
    {
        this.playerFlights = playerFlights;
    }
    
    public boolean isInFlightPath(Coordinate position) throws PWCGException 
    {
        FlightPathPlotter virtualWaypointPlotter = new FlightPathPlotter();
        for (Flight playerFlight : playerFlights)
        {
	        List<Coordinate>flightPlotCoordinates = virtualWaypointPlotter.plotCoordinatesByMinute(playerFlight);
	        for (Coordinate flightPlotCoordinate : flightPlotCoordinates)
	        {
	            double distanceToPlayer = MathUtils.calcDist(flightPlotCoordinate, position);
	            if (distanceToPlayer < 15000)
	            {
	                return true;
	            }
	        }
        }

        return false;
    }

}
