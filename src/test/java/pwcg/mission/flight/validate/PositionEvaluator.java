package pwcg.mission.flight.validate;

import org.junit.jupiter.api.Assertions;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;

public class PositionEvaluator
{

    public static void evaluatePlayerFlightDistance(Mission mission, int maxDisatance) throws PWCGException
    {
        boolean distanceIsGood = true;
        double playerDistance = mission.getPlayerDistanceToTarget();
        if (playerDistance > maxDisatance)
        {
            distanceIsGood = false;
        }
            
        Assertions.assertTrue (distanceIsGood);
    }
}
