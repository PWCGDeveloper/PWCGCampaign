package pwcg.aar.inmission.phase2.logeval;

import java.util.List;

import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.utils.MathUtils;

public class AARCrossedPathWithPlayerEvaluator 
{    
    public static double DISTANCE_TO_CROSS_PLAYER_PATH = 6000.0;
    
    public AARCrossedPathWithPlayerEvaluator()
    {
    }

    public boolean isCrossedPathWithPlayerFlight(LogVictory deadVehicle, List<LogDamage> vehiclesDamagedByPlayer, List<IAType17> waypointEvents)
    {
        boolean crossedPath = false;
        if (isCrossedPathByProximityToDamageEvent(deadVehicle, vehiclesDamagedByPlayer))
        {
            crossedPath = true;
        }
        if (isCrossedPathByProximityToWaypoint(deadVehicle, waypointEvents))
        {
            crossedPath = true;
        }
        
        return crossedPath;
    }
    
    private boolean isCrossedPathByProximityToDamageEvent(LogVictory deadVehicle, List<LogDamage> vehiclesDamagedByPlayer)
    {
        for (LogDamage damagedVehicle : vehiclesDamagedByPlayer)
        {
            if (deadVehicle.getVictim() == null || damagedVehicle.getVictim() == null || damagedVehicle.getVictor() == null)
            {
                return false;
            }
            
            if (deadVehicle.getVictim().getId().equals(damagedVehicle.getVictim().getId()))
            {
                return true;
            }
            
            if (deadVehicle.getVictim().getId().equals(damagedVehicle.getVictor().getId()))
            {
                return true;
            }
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

