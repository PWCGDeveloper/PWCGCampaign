package pwcg.aar.inmission.phase2.logeval;

import java.util.List;

import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;

public class AARCrossedPathWithPlayerEvaluator 
{    
    public static double DISTANCE_TO_CROSS_PLAYER_PATH = 6000.0;

    private Campaign campaign;
    
    public AARCrossedPathWithPlayerEvaluator(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public boolean isCrossedPathWithPlayerFlight(LogVictory deadVehicle, List<LogDamage> damageEvents, List<IAType17> waypointEvents) throws PWCGException
    {
        boolean crossedPath = false;
        if (isCrossedPathByProximityToDamageEvent(deadVehicle, damageEvents))
        {
            crossedPath = true;
        }
        if (isCrossedPathByProximityToWaypoint(deadVehicle, waypointEvents))
        {
            crossedPath = true;
        }
        
        return crossedPath;
    }
    
    private boolean isCrossedPathByProximityToDamageEvent(LogVictory deadVehicle, List<LogDamage> damageEvents) throws PWCGException
    {
        for (LogDamage damageEvent : damageEvents)
        {
            if (deadVehicle.getVictim() == null || damageEvent.getVictim() == null || damageEvent.getVictor() == null)
            {
                return false;
            }
            
            LogAIEntity otherVehicle = null;

            if (deadVehicle.getVictim().getId().equals(damageEvent.getVictim().getId()))
            {
                otherVehicle = damageEvent.getVictor();
            }
            
            if (deadVehicle.getVictim().getId().equals(damageEvent.getVictor().getId()))
            {
                otherVehicle = damageEvent.getVictim();
            }

            if (otherVehicle instanceof LogPlane)
            {
                LogPlane otherPlane = (LogPlane) otherVehicle;

                if (otherPlane.isLogPlaneFromPlayerSquadron(campaign))
                {
                    return true;
                }
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

