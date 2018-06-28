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
        plotAIFlightEncounters();
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

    private void plotAIFlightEncounters() throws PWCGException 
    {
        // Mission flights
        for (Flight alliedFlight : missionFlightBuilder.getAlliedAiFlights())
        {
            for (Flight axisFlight : missionFlightBuilder.getAxisAiFlights())
            {
                // Set first point only for flights we are going to keep
                // Dependency: plotPlayerFlightEncounters must be executed first
                plotEncounter(alliedFlight, axisFlight, 3500.0);
            }
        }
    }

    private void plotEncounter(Flight thisFlight, Flight thatFlight, double encounterRadius) throws PWCGException 
    {
        // Plot the minute by minute path of each flight
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        List<VirtualWayPointCoordinate> thisFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(thisFlight);
        List<VirtualWayPointCoordinate> thatFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(thatFlight);

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
                    if (thisFlight.isPlayerFlight())
                    {
                        thatFlight.setContactWithPlayer(timeSliceOfFlight);
                    }
                    else
                    {
                        thisFlight.setFirstContactWithEnemy(timeSliceOfFlight, thatFlight);
                        thatFlight.setFirstContactWithEnemy(timeSliceOfFlight, thisFlight);
                    }
                }
                
                if (closestDistanceToThatFlight == -1.0 || distance < closestDistanceToThatFlight)
                {
                    if (thisFlight.isPlayerFlight())
                    {
                        thatFlight.setClosestContactWithPlayerDistance(distance);
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

    public static double proximityToPosition(Flight thisFlight, Coordinate referencePosition) throws PWCGException 
    {
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        List<VirtualWayPointCoordinate> thisFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(thisFlight);

        double closestDistanceToReferencePosition = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        
        // Compare the minute by minute plots to see when a flight intersects a
        // player flight or another flight
        for (VirtualWayPointCoordinate thisFlightCoordinate : thisFlightPath)
        {
            double distance = MathUtils.calcDist(thisFlightCoordinate.getCoordinate(), referencePosition);
            if (distance < closestDistanceToReferencePosition)
            {
                closestDistanceToReferencePosition = distance;
            }
        }
        
        return closestDistanceToReferencePosition;
    }
    

    public static double proximityToPlayerAirbase(Flight thisFlight) throws PWCGException 
    {
        // Plot the minute by minute path of each flight
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        List<VirtualWayPointCoordinate> thisFlightPath = virtualWaypointPlotter.plotCoordinatesByMinute(thisFlight);

        double closestDistanceToPlayerField = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        
        Coordinate playerFieldCoordinte = PWCGContextManager.getInstance().getCampaign().getPosition().copy();
        
        // Compare the minute by minute plots to see when a flight intersects a
        // player flight or another flight
        for (VirtualWayPointCoordinate vwp : thisFlightPath)
        {
            double distanceToPlayerField = MathUtils.calcDist(playerFieldCoordinte, vwp.getCoordinate());
            if (closestDistanceToPlayerField > distanceToPlayerField)
            {
                closestDistanceToPlayerField = distanceToPlayerField;
            }
        }
        
        return closestDistanceToPlayerField;
    }

}
