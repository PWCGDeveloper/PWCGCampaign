package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.event.IAType3;

public class AARDestroyedStatusEvaluator
{
    private List<LogVictory> deadLogVehicles = new ArrayList<>();
    private Map<Integer, LogPilot> deadLogPilots = new HashMap<>();

    private AARVehicleBuilder vehicleBuilder;
    private AARLogEventData logEventData;
    private AARDamageStatusEvaluator damageStatusEvaluator;

    public AARDestroyedStatusEvaluator(AARLogEventData logEventData, AARVehicleBuilder aarVehicleBuilder, AARDamageStatusEvaluator damageStatusEvaluator)
    {
        this.logEventData = logEventData;
        this.vehicleBuilder = aarVehicleBuilder;
        this.damageStatusEvaluator = damageStatusEvaluator;
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
            addDamagedByInformation(logVictory, logVictim);
        }

        deadLogVehicles.add(logVictory);
    }

    private void addDamagedByInformation(LogVictory logVictory, LogAIEntity logVictim) throws PWCGException
    {
        AARDamageStatus damageStatus = damageStatusEvaluator.getDamageStatusForVehicle(logVictim.getId());
        if (damageStatus != null)
        {
            logVictory.setDamageInformation(damageStatus);
        }
        else
        {
            logVictory.setDamageInformation(new AARDamageStatus(logVictim.getId()));
        }
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
