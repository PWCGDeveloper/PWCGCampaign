package pwcg.aar.inmission.phase2.logeval.victory;

import pwcg.aar.inmission.phase2.logeval.AARDamageStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
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
        LogAIEntity victorBasedOnDamage = getPlayerAsVictorBasedOnDamage(victoryResult);
        return victorBasedOnDamage;
    }
    
    private LogAIEntity getPlayerAsVictorBasedOnDamage(LogVictory victoryResult)
    {
        LogAIEntity victor = null;
        
        for (LogDamage damageResult : aarDamageStatusEvaluator.getVehiclesDamagedByPlayer())
        {
            if (damageResult.getVictim().getId().equals(victoryResult.getVictim().getId()))
            {
                victor = damageResult.getVictor();
            }
        }
        
        return victor;
    }
}

