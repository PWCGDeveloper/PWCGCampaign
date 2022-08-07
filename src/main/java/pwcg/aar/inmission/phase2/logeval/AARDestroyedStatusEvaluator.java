package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.ClaimDenier;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType3;

public class AARDestroyedStatusEvaluator
{
    private List<LogVictory> deadLogVehicles = new ArrayList<>();
    private Map<Integer, LogPilot> deadLogPilots = new HashMap<>();

    private AARVehicleBuilder vehicleBuilder;
    private LogEventData logEventData;

    public AARDestroyedStatusEvaluator(LogEventData logEventData, AARVehicleBuilder aarVehicleBuilder)
    {
        this.logEventData = logEventData;
        this.vehicleBuilder = aarVehicleBuilder;
    }

    public void buildDeadLists() throws PWCGException
    {
        for (IAType3 atype3 : logEventData.getDestroyedEvents())
        {                    
            LogAIEntity logVictor = vehicleBuilder.getVehicle(atype3.getVictor());
            LogAIEntity logVictim = vehicleBuilder.getVehicle(atype3.getVictim());

            LogVictory logVictory = new LogVictory(atype3.getSequenceNum());
            logVictory.setLocation(atype3.getLocation());

            if (logVictim != null)
            {
                addDeadVehicle(logVictor, logVictim, logVictory);
            }
            else
            {
                addDeadPilot(atype3);
            }
        }
    }

    private void addDeadVehicle(LogAIEntity logVictor, LogAIEntity logVictim, LogVictory logVictory) throws PWCGException
    {
        logVictory.setVictim(logVictim);
        if (logVictor != null)
        {
            logVictory.setVictor(logVictor);
        }
        else
        {
            LogAIEntity logVictorByDamage = getMostDamagedBy(logVictim);
            if (logVictorByDamage != null)
            {
                logVictory.setVictor(logVictorByDamage);
            }
        }

        deadLogVehicles.add(logVictory);
    }

    private LogAIEntity getMostDamagedBy(LogAIEntity logVictim) throws PWCGException
    {
        LogAIEntity logVictorByDamage = new LogUnknown();
                
        Map<String, Double> damagedBy = logEventData.getDamagedBy(logVictim.getId());
        double mostDamage = 0.0;
        for (String mostDamagedById : damagedBy.keySet())
        {
            if (mostDamagedById.equals(ClaimDenier.UNKNOWN))
            {
                continue;
            }

            double damagedByValue = damagedBy.get(mostDamagedById);
            if (damagedByValue > mostDamage)
            {
                logVictorByDamage = vehicleBuilder.getVehicle(mostDamagedById);
                mostDamage = damagedByValue;
            }
        }
        return logVictorByDamage;
    }

    private void addDeadPilot(IAType3 atype3)
    {
        LogPilot deadPilot = matchDeadBotToCrewMember(atype3);
        if (deadPilot != null)
        {
            deadLogPilots.put(deadPilot.getSerialNumber(), deadPilot);
        }
    }

    private LogPilot matchDeadBotToCrewMember(IAType3 atype3)
    {
        for (LogPlane planeEntity : vehicleBuilder.getLogPlanes().values())
        {
            LogPilot crewMemberEntity = planeEntity.getLogPilot();
            if (crewMemberEntity.getBotId().equals(atype3.getVictim()))
            {
                return crewMemberEntity;
            }
        }

        return null;
    }

    public boolean didCrewMemberDie(int serialNumber)
    {
        if (deadLogPilots.containsKey(serialNumber))
        {
            return true;
        }

        return false;
    }

    public List<LogVictory> getDeadLogVehicleList()
    {
        return deadLogVehicles;
    }
}
