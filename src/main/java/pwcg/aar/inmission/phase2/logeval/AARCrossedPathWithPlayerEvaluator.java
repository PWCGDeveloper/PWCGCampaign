package pwcg.aar.inmission.phase2.logeval;

import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.logfiles.event.IAType17;
import pwcg.core.utils.MathUtils;

public class AARCrossedPathWithPlayerEvaluator 
{    
    public static double DISTANCE_TO_CROSS_PLAYER_PATH = 6000.0;

    public boolean isCrossedPathWithPlayerFlight(LogVictory deadVehicle, AARPlayerLocator aarPlayerLocator, List<IAType17> waypointEvents)
    {
        boolean crossedPath = false;
        if (isCrossedPathByProximityToDamageEvent(deadVehicle, aarPlayerLocator))
        {
            crossedPath = true;
        }
        if (isCrossedPathByProximityToWaypoint(deadVehicle, waypointEvents))
        {
            crossedPath = true;
        }
        
        return crossedPath;
    }
    
    private boolean isCrossedPathByProximityToDamageEvent(LogVictory deadVehicle, AARPlayerLocator aarPlayerLocator)
    {
        double crashDistanceFromPlayer = aarPlayerLocator.closestPlayerDistance(deadVehicle.getLocation());
        if (crashDistanceFromPlayer <= DISTANCE_TO_CROSS_PLAYER_PATH)
        {
            return true;
        }

        return false;
    }
    
    private boolean isCrossedPathByProximityToWaypoint(LogVictory deadVehicle, List<IAType17> waypointEvents)
    {
        for (IAType17 waypointEvent : waypointEvents)
        {
            double crashDistanceFromWaypoint = MathUtils.calcDist(deadVehicle.getLocation(), waypointEvent.getLocation());
            if (crashDistanceFromWaypoint <= DISTANCE_TO_CROSS_PLAYER_PATH)
            {
                return true;
            }
       }
        
        return false;
    }

}

