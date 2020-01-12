package pwcg.mission.flight.plot;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class FlightProximityAnalyzer
{
    private Mission mission = null;

    public FlightProximityAnalyzer (Mission mission)
    {
        this.mission = mission;
    }

    public void plotFlightEncounters() throws PWCGException 
    {
        plotPlayerFlightEncounters();
    }

    private void plotPlayerFlightEncounters() throws PWCGException 
    {
        int playerEncounerDistance = VirtualWayPoint.VWP_TRIGGGER_DISTANCE;
        for (IFlight aiFlight : mission.getMissionFlightBuilder().getAiFlights())
        {
            if (!aiFlight.getFlightData().getFlightInformation().isPlayerFlight())
            {
                for (IFlight playerFlight : mission.getMissionFlightBuilder().getPlayerFlights())
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
                    if (playerFlight.getFlightData().getFlightInformation().isPlayerFlight())
                    {
                        aiFlight.getFlightData().getFlightPlayerContact().setContactWithPlayer(timeSliceOfFlight);
                    }
                }
                
                if (distance < closestDistanceToThatFlight)
                {
                    if (playerFlight.getFlightData().getFlightInformation().isPlayerFlight())
                    {
                        aiFlight.getFlightData().getFlightPlayerContact().setClosestContactWithPlayerDistance(distance);
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
