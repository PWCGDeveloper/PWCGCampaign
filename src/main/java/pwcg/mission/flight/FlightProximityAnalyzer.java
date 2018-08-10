package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.MissionFlightBuilder;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class FlightProximityAnalyzer
{
    private MissionFlightBuilder missionFlightBuilder = null;

    public FlightProximityAnalyzer (MissionFlightBuilder mission)
    {
        this.missionFlightBuilder = mission;
    }

    public void plotFlightEncounters() throws PWCGException 
    {
        plotPlayerFlightEncounters();
    }

    public double proximityToPlayerAirbase(Flight aiFlight) throws PWCGException 
    {
        // Plot the minute by minute path of each flight
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        List<VirtualWayPointCoordinate> aiFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(aiFlight);

        double closestDistanceToPlayerField = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        
        Coordinate playerFieldCoordinte = PWCGContextManager.getInstance().getCampaign().getPosition().copy();
        for (VirtualWayPointCoordinate vwp : aiFlightPath)
        {
            double distanceToPlayerField = MathUtils.calcDist(playerFieldCoordinte, vwp.getCoordinate());
            if (closestDistanceToPlayerField > distanceToPlayerField)
            {
                closestDistanceToPlayerField = distanceToPlayerField;
            }
        }
        
        return closestDistanceToPlayerField;
    }

    private void plotPlayerFlightEncounters() throws PWCGException 
    {
        // Plot my flight against every other flight
        int playerEncounerDistance = VirtualWayPoint.VWP_TRIGGGER_DISTANCE;
        
        for (Flight flight : missionFlightBuilder.getMissionFlights())
        {
            plotEncounter(missionFlightBuilder.getPlayerFlight(), flight, playerEncounerDistance);
        }
    }

    private void plotEncounter(Flight playerFlight, Flight aiFlight, double encounterRadius) throws PWCGException 
    {
        // Plot the minute by minute path of each flight
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        List<VirtualWayPointCoordinate> thisFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(playerFlight);
        List<VirtualWayPointCoordinate> thatFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(aiFlight);

        double closestDistanceToThatFlight = -1.0;
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
                    else
                    {
                        playerFlight.setFirstContactWithEnemy(timeSliceOfFlight, aiFlight);
                        aiFlight.setFirstContactWithEnemy(timeSliceOfFlight, playerFlight);
                    }
                }
                
                if (closestDistanceToThatFlight == -1.0 || distance < closestDistanceToThatFlight)
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
