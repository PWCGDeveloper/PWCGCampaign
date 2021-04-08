package pwcg.mission.flight.plot;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.group.virtual.VirtualWaypoint;

public class FlightProximityAnalyzer
{
    private List<IFlight> playerFlights;
    private List<IFlight> aFlights;

    public FlightProximityAnalyzer (List<IFlight> playerFlights, List<IFlight> aFlights)
    {
        this.playerFlights = playerFlights;
        this.aFlights = aFlights;
    }

    public void plotFlightEncounters() throws PWCGException 
    {
        plotPlayerFlightEncounters();
    }

    private void plotPlayerFlightEncounters() throws PWCGException 
    {
        int playerEncounerDistance = VirtualWaypoint.VWP_TRIGGGER_DISTANCE;
        for (IFlight aiFlight : aFlights)
        {
            if (!aiFlight.isPlayerFlight())
            {
                for (IFlight playerFlight : playerFlights)
                {
                    plotEncounter(playerFlight, aiFlight, playerEncounerDistance);
                }
            }
        }
    }

    private void plotEncounter(IFlight playerFlight, IFlight aiFlight, double encounterRadius) throws PWCGException 
    {
        FlightPathByMinutePlotter virtualWaypointPlotter = new FlightPathByMinutePlotter();
        List<Coordinate> thisFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(playerFlight);
        List<Coordinate> thatFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(aiFlight);

        double closestDistanceToThatFlight = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        // Compare the minute by minute plots to see when a flight intersects a
        // player flight or another flight
        for (int timeSliceOfFlight = 0; timeSliceOfFlight < 10000000; ++timeSliceOfFlight)
        {
            if (timeSliceOfFlight < thisFlightPath.size() && timeSliceOfFlight < thatFlightPath.size())
            {
                Coordinate thisFlightCoordinate = thisFlightPath.get(timeSliceOfFlight);
                Coordinate thatFlightCoordinate = thatFlightPath.get(timeSliceOfFlight);

                double distance = MathUtils.calcDist(thisFlightCoordinate, thatFlightCoordinate);
                if (distance < encounterRadius)
                {                    
                    if (playerFlight.isPlayerFlight())
                    {
                        aiFlight.getFlightPlayerContact().setContactWithPlayer(timeSliceOfFlight);
                    }
                }
                
                if (distance < closestDistanceToThatFlight)
                {
                    if (playerFlight.isPlayerFlight())
                    {
                        aiFlight.getFlightPlayerContact().setClosestContactWithPlayerDistance(distance);
                        closestDistanceToThatFlight = distance;
                    }
                }
            }
            else
            {
                break;
            }
        }
    }
}
