package pwcg.aar.inmission.phase2.logeval.victory;

import pwcg.aar.inmission.phase2.logeval.AARDamageStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;

public class AARFuzzyByAccumulatedDamaged 
{
    private AARDamageStatusEvaluator aarDamageStatusEvaluator = null;
    
    public AARFuzzyByAccumulatedDamaged(AARDamageStatusEvaluator aarDamageStatusEvaluator)
    {
        this.aarDamageStatusEvaluator = aarDamageStatusEvaluator;
    }

    public LogAIEntity getVictorBasedOnDamage(LogVictory victoryResult) throws PWCGException 
    {
        LogAIEntity victim = victoryResult.getVictim();
        if (victim != null)
        {
            LogAIEntity victor = aarDamageStatusEvaluator.getVictorByDamage(victoryResult.getVictim().getId());
            return victor;
        }
        return null;
    }
}

