package pwcg.aar.inmission.phase2.logeval;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;

public class AARDamageStatus
{
    private String victimId;
    private Map<String, LogDamage> vehicleDamaged = new HashMap<>();
    
    public AARDamageStatus(String victimId)
    {
        this.victimId = victimId;
    }
    
    public void addDamage(String victorId, LogDamage damageRecord)
    {
        vehicleDamaged.put(victorId, damageRecord);
    }

    public String getVictimId()
    {
        return victimId;
    }

    public Map<String, LogDamage> getVehicleDamaged()
    {
        return vehicleDamaged;
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
                accumulatedDamageByVictor.put(victorId, new Double(0.0));
            }
            
            LogDamage logDamage = vehicleDamaged.get(victorId);
            Double accumulatedDamage = accumulatedDamageByVictor.get(victorId);
            accumulatedDamage += logDamage.getDamageLevel();
            accumulatedDamageByVictor.put(victorId, accumulatedDamage);
        }
        return accumulatedDamageByVictor;
    }
}
