package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;

public class AARDamageStatus
{
    private Map<String, List<LogDamage>> vehicleDamaged = new HashMap<>();
    
    public AARDamageStatus()
    {
    }
    
    public void addDamage(String victorId, LogDamage damageRecord)
    {
        if (!vehicleDamaged.containsKey(victorId))
        {
            vehicleDamaged.put(victorId, new ArrayList<>());
        }
        vehicleDamaged.get(victorId).add(damageRecord);
    }

    public String getVictorBasedOnDamage()
    {
        Map<String, Double> accumulatedDamageByVictor = accumulateDamageByVictor();
        String selectedVictor = AARLogParser.UNKNOWN_MISSION_LOG_ENTITY;
        double selectedVictorDamage = 0.0;
        for (String victorId : accumulatedDamageByVictor.keySet())
        {
            double damageByVictor = accumulatedDamageByVictor.get(victorId);
            if (damageByVictor > selectedVictorDamage)
            {
                selectedVictor = victorId;
                selectedVictorDamage = damageByVictor;
            }
        }
        
        return selectedVictor;
    }

    private Map<String, Double> accumulateDamageByVictor()
    {
        Map<String, Double> accumulatedDamageByVictor = new HashMap<>();
        for (String victorId : vehicleDamaged.keySet())
        {
            if (!accumulatedDamageByVictor.containsKey(victorId))
            {
                accumulatedDamageByVictor.put(victorId, Double.valueOf(0.0));
            }
            
            List<LogDamage> logDamageList = vehicleDamaged.get(victorId);
            double accumulatedDamage = 0;
            for (LogDamage logDamage : logDamageList)
            {
                accumulatedDamage += logDamage.getDamageLevel();
            }
            accumulatedDamageByVictor.put(victorId, accumulatedDamage);
        }
        return accumulatedDamageByVictor;
    }
}
