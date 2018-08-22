package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.core.exception.PWCGException;

/**
 * Determines which vehicles were damaged by the player
 * 
 * @author Patrick Wilson
 *
 */
public class AARDamageStatusEvaluator 
{    
    private Map <String, LogDamage> vehiclesDamagedByPlayer = new HashMap <String, LogDamage>();

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
        if (!vehiclesDamagedByPlayer.containsKey(victimId))
        {
            logDamage = createDamageRecord(atype2);
            vehiclesDamagedByPlayer.put(victimId, logDamage);
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
        logDamage.setLocation(atype2.getLocation());
        return logDamage;
    }

    public List<LogDamage> getVehiclesDamagedByPlayer()
    {
        return new ArrayList<LogDamage>(vehiclesDamagedByPlayer.values());
    }
   
}

