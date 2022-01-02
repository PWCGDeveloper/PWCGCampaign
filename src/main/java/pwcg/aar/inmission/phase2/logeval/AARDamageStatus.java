package pwcg.aar.inmission.phase2.logeval;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;

public class AARDamageStatus
{
    String vehicleId;
    Map<String, Double> damageByVictor = new HashMap<>(); 
        
    public AARDamageStatus (String vehicleId)
    {
        this.vehicleId = vehicleId;
    }
    
    public void addDamage(String victorId, LogDamage damageRecord)
    {
        if (!damageByVictor.containsKey(victorId))
        {
            damageByVictor.put(victorId, 0.0);
        }
        
        Double accumulatedDamageByVictor = damageByVictor.get(victorId);
        accumulatedDamageByVictor += damageRecord.getDamageLevel();
        damageByVictor.put(victorId, accumulatedDamageByVictor);
    }

    public String getVictorBasedOnDamage()
    { 
        Double maxDamageByVictor = 0.0;
        String selectedVictor = "";
        for (String victorId : damageByVictor.keySet())
        {
            double damage = damageByVictor.get(victorId);
            if (damage > maxDamageByVictor)
            {
                maxDamageByVictor = damage;
                selectedVictor = victorId;                
            }
        }
        
        return selectedVictor;
    }
    
    public boolean didCrewMemberDamagePlane(String victorId)
    {
        return damageByVictor.containsKey(victorId);
    }
}
