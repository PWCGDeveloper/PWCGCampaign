package pwcg.aar.inmission.phase2.logeval.victory;

import pwcg.aar.inmission.phase2.logeval.AARDamageStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;

public class AARFuzzyByPlayerDamaged 
{
    private AARDamageStatusEvaluator aarDamageStatusEvaluator = null;
    
    public AARFuzzyByPlayerDamaged(AARDamageStatusEvaluator aarDamageStatusEvaluator)
    {
        this.aarDamageStatusEvaluator = aarDamageStatusEvaluator;
    }

    public LogAIEntity getVictorBasedOnDamage(LogVictory victoryResult) throws PWCGException 
    {
        LogAIEntity victor = null;
        double maxDamage = Double.NEGATIVE_INFINITY;
        
        for (LogDamage damageResult : aarDamageStatusEvaluator.getDamageEventsForVehicle(victoryResult.getVictim().getId()))
        {
            if (damageResult.getVictor() != null && !(damageResult.getVictor() instanceof LogUnknown) && damageResult.getDamageAmount() > maxDamage)
            {
                victor = damageResult.getVictor();
                maxDamage = damageResult.getDamageAmount();
            }
        }

        return victor;
    }
}

