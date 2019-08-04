package pwcg.mission.flight;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;
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
        for (Flight aiFlight : mission.getMissionFlightBuilder().getAiFlights())
        {
            if (!aiFlight.isPlayerFlight())
            {
                for (Flight playerFlight : mission.getMissionFlightBuilder().getPlayerFlights())
                {
                    if (playerFlight.getSquadron().determineSide() != aiFlight.getSquadron().determineSide())
                    {
                        plotEncounter(playerFlight, aiFlight, playerEncounerDistance);
                    }
                }
            }
        }
    }

    private void plotEncounter(Flight playerFlight, Flight aiFlight, double encounterRadius) throws PWCGException 
    {
        // Plot the minute by minute path of each flight
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        List<VirtualWayPointCoordinate> thisFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(playerFlight);
        List<VirtualWayPointCoordinate> thatFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(aiFlight);

        double closestDistanceToThatFlight = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        // Compare the minute by minute plots to see when a flight intersects a
        // player flight or another flight
        for (int timeSliceOfFlight = 0; timeSliceOfFlight < 10000000; ++timeSliceOfFlight)
        {
            if (timeSliceOfFlight < thisFlightPath.size() && timeSliceOfFlight < thatFlightPath.size())
            {
                VirtualWayPointCoordinate thisFlightCoordinate = thisFlightPath.get(timeSliceOfFlight);
                VirtualWayPointCoordinate thatFlightCoordinate = thatFlightPath.get(timeSliceOfFlight);

                double distance = MathUtils.calcDist(thisFlightCoordinate.getCoordinate(), thatFlightCoordinate.getCoordinate());
                if (distance < encounterRadius)
                {                    
                    if (playerFlight.isPlayerFlight())
                    {
                        aiFlight.setContactWithPlayer(timeSliceOfFlight);
                    }
                }
                
                if (distance < closestDistanceToThatFlight)
                {
                    if (playerFlight.isPlayerFlight())
                    {
                        aiFlight.setClosestContactWithPlayerDistance(distance);
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
