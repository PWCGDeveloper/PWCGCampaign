package pwcg.mission.flight.plot;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;

public class FlightPathToWaypointPlotter
{
    private IFlight flight;
    
    public FlightPathToWaypointPlotter(IFlight flight)
    {
        this.flight = flight;
    }
    
    public int plotTimeToWaypointAction(WaypointAction requestedAction) throws PWCGException 
    {        
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(requestedAction);
        if (targetMissionPoint == null)
        {
            return 0;
        }
        
        double totalDistance = calculateTotalDistanceToRequestedAction(requestedAction);
        Double timeInHours = (totalDistance / 1000) / flight.getFlightCruisingSpeed();
        int timeInSeconds = Double.valueOf(timeInHours * 60 * 60).intValue();
        return timeInSeconds;
    }

    private double calculateTotalDistanceToRequestedAction(WaypointAction requestedAction) throws PWCGException
    {
        double totalDistance = 0;
        MissionPoint previousMissionPoint = null;
        for (MissionPoint missionPoint : flight.getWaypointPackage().getMissionPoints())
        {
            if (previousMissionPoint != null)
            {
                double legDistance = MathUtils.calcDist(previousMissionPoint.getPosition(), missionPoint.getPosition());
                totalDistance += legDistance;
            }
            
            previousMissionPoint = missionPoint;
            
            if (missionPoint.getAction() == requestedAction)
            {
                break;
            }            
        }
        return totalDistance;
    }
}