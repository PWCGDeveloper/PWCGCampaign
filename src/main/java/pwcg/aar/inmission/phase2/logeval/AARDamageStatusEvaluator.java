package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.core.exception.PWCGException;

/**
 * Determines which vehicles were damaged by other entities
 * 
 * @author Patrick Wilson
 *
 */
public class AARDamageStatusEvaluator 
{    
    private Map<String, Map<String, LogDamage>> damageStatus = new HashMap<>();

    private AARVehicleBuilder aarVehicleBuilder = null;
    private AARLogEventData logEventData = null;

    public AARDamageStatusEvaluator(AARLogEventData logEventData, AARVehicleBuilder aarVehicleBuilder)
    {
        this.logEventData = logEventData;        
        this.aarVehicleBuilder = aarVehicleBuilder;
    }

    public void buildDamagedList() throws PWCGException 
    {
        for (IAType2 atype2 : logEventData.getDamageEvents())
        {
            if (logEventData.isVehicle(atype2.getVictim()))
            {
                addVehicleDamagedByPlayer(atype2);
            }
        }
    }

    private void addVehicleDamagedByPlayer(IAType2 atype2) throws PWCGException
    {
        LogDamage logDamage = null;
        String victimId = atype2.getVictim();
        String victorId = atype2.getVictor();
        if (!damageStatus.containsKey(victimId))
        {
            damageStatus.put(victimId, new HashMap<>());
        }

        Map<String, LogDamage> victimDamageMap = damageStatus.get(victimId);
        if (!victimDamageMap.containsKey(victorId))
        {
            logDamage = createDamageRecord(atype2);
            victimDamageMap.put(victorId, logDamage);
        }
        else
        {
            logDamage = victimDamageMap.get(victorId);
            logDamage.addDamage(atype2.getDamageLevel());
        }
    }

    private LogDamage createDamageRecord(IAType2 atype2) throws PWCGException
    {
        LogAIEntity logVictor = aarVehicleBuilder.getVehicle(atype2.getVictor());
        LogAIEntity logVictim = aarVehicleBuilder.getVehicle(atype2.getVictim());
                        
        LogDamage logDamage = new LogDamage(atype2.getSequenceNum());
        if (logVictor != null)
        {
            logDamage.setVictor(logVictor);
        }
        logDamage.setVictim(logVictim);
        logDamage.setDamageAmount(atype2.getDamageLevel());
        logDamage.setLocation(atype2.getLocation());
        return logDamage;
    }

    public List<LogDamage> getVehiclesDamaged()
    {
        return damageStatus.values().stream().flatMap(x -> x.values().stream()).collect(Collectors.toList());
    }
   
    public List<LogDamage> getDamageEventsForVehicle(String victimId)
    {
        if (damageStatus.containsKey(victimId))
            return new ArrayList<>(damageStatus.get(victimId).values());
        return new ArrayList<>();
    }

}

