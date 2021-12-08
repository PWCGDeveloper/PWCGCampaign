package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.AARLogEventData;
import pwcg.core.logfiles.event.IAType2;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

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
        String victimId = atype2.getVictim();
        String victorId = atype2.getVictor();

        LogDamage logDamage;
        logDamage = createDamageRecord(atype2);
        if (uselessLog(atype2) || victimId == null || victorId == null)
        {
            return;
        }

        addDamageEntryByVictor(logDamage, victimId, victorId);        
        addChronologicalDamageRecord(logDamage);
    }

    private void addDamageEntryByVictor(LogDamage logDamage, String victimId, String victorId) throws PWCGException
    {
        LogAIEntity logVictim = aarVehicleBuilder.getVehicle(victimId);
        if (!vehiclesDamaged.containsKey(logVictim.getId()))
        {
            AARDamageStatus vehicleDamaged = new AARDamageStatus(victimId);
            vehiclesDamaged.put(logVictim.getId(), vehicleDamaged);
        }
        AARDamageStatus vehicleDamaged = vehiclesDamaged.get(victimId);
        vehicleDamaged.addDamage(victorId, logDamage);
    }

    private void addChronologicalDamageRecord(LogDamage logDamage) throws PWCGException
    {
        if (logDamage != null)
        {
            allDamageEventsInOrder.add(logDamage);
        }
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
        
        if (logVictor != null && logVictim != null)
        {
            LogDamage logDamage = new LogDamage(atype2.getSequenceNum());
            logDamage.setVictor(logVictor);
            logDamage.setVictim(logVictim);
            logDamage.setLocation(atype2.getLocation());
            logDamage.setDamageLevel(atype2.getDamageLevel());
            return logDamage;
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "Damage record has no victor");
        }
        
        return null;
    }

    public List<LogDamage> getAllDamageEvents()
    {
        return allDamageEventsInOrder;
    }

    public LogAIEntity getVictorByDamage(LogAIEntity logVictim) throws PWCGException
    {
        LogAIEntity logVictor = null;
        AARDamageStatus damageStatus = vehiclesDamaged.get(logVictim.getId());
        if (damageStatus != null)
        {
            String victorId = damageStatus.getVictorBasedOnDamage();
            logVictor = aarVehicleBuilder.getVehicle(victorId);
        }
        return logVictor;
    }

    public AARDamageStatus getDamageStatusForVehicle(String victimId) throws PWCGException
    {
        return vehiclesDamaged.get(victimId);
    }
}

