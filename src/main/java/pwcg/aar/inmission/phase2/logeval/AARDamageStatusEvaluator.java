package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.core.exception.PWCGException;

public class AARDamageStatusEvaluator 
{    
    private Map <String, AARDamageStatus> vehiclesDamaged = new HashMap<>();
    private List <LogDamage> allDamageEventsInOrder = new ArrayList<>();

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
                addVehicleDamaged(atype2);
            }
        }
    }

    private void addVehicleDamaged(IAType2 atype2) throws PWCGException
    {
        LogDamage logDamage = null;
        String victimId = atype2.getVictim();
        String victorId = atype2.getVictor();
        
        if (uselessLog(atype2))
        {
            return;
        }
        
        if (!vehiclesDamaged.containsKey(victimId))
        {
            AARDamageStatus damageStatus = new AARDamageStatus();
            vehiclesDamaged.put(victimId, damageStatus);
        }
        
        AARDamageStatus damageStatus = vehiclesDamaged.get(victimId);
        logDamage = createDamageRecord(atype2);
        damageStatus.addDamage(victorId, logDamage);
        
        allDamageEventsInOrder.add(logDamage);
    }
    
    private boolean uselessLog(IAType2 atype2)
    {
        String victimId = atype2.getVictim();
        String victorId = atype2.getVictor();
        
        if (victimId == null || victorId == null)
        {
            return true;
        }

        if (victorId.equals(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY) || victimId.equals(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY))
        {
            return true;
        }
        
        return false;
    }

    private LogDamage createDamageRecord(IAType2 atype2) throws PWCGException
    {
        LogAIEntity logVictor = aarVehicleBuilder.getVehicle(atype2.getVictor());
        LogAIEntity logVictim = aarVehicleBuilder.getVehicle(atype2.getVictim());
        
        if (logVictor == null)
        {
            throw new PWCGException("useless damage log leak");
        }
                        
        LogDamage logDamage = new LogDamage(atype2.getSequenceNum());
        logDamage.setVictor(logVictor);
        logDamage.setVictim(logVictim);
        logDamage.setLocation(atype2.getLocation());
        logDamage.setDamageLevel(atype2.getDamageLevel());
        return logDamage;
    }

    public List<LogDamage> getAllDamageEvents()
    {
        return allDamageEventsInOrder;
    }

    public LogAIEntity getVictorByDamage(String victimId) throws PWCGException
    {
        LogAIEntity logVictor = null;
        AARDamageStatus damageStatus = vehiclesDamaged.get(victimId);
        if (damageStatus != null)
        {
            String victorId = damageStatus.getVictorBasedOnDamage();
            logVictor = aarVehicleBuilder.getVehicle(victorId);
        }
        return logVictor;
    }
}

