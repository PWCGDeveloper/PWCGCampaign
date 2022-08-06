package pwcg.aar.inmission.phase1.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.logfiles.event.IAType2;
import pwcg.core.logfiles.event.IAType3;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class AAREventAnalyzer
{
    private LogEventData logEventData = new LogEventData();

    public AAREventAnalyzer(LogEventData logEventData)
    {
        this.logEventData = logEventData;
    }
    
    public void analyze()
    {
        for (IAType3 destroyed : logEventData.getDestroyedEvents())
        {
            String victim = getVehicleName(destroyed.getVictim());
            String victor = getVehicleName(destroyed.getVictor());
            if (!victor.equals("Unknown"))
            {
                PWCGLogger.log(LogLevel.INFO, victim + " killed by " + victor);
            }
            else
            {
                List<IAType12> damagedBy = getDamagedBy(destroyed);
                if (damagedBy.isEmpty())
                {
                    PWCGLogger.log(LogLevel.INFO, victim + " killed by and damaged by nobody");
                }
                else
                {
                    for (IAType12 damagedVictor : damagedBy)
                    {
                        PWCGLogger.log(LogLevel.INFO, victim + " killed by and nobody, damged by " + getVehicleName(damagedVictor.getId()));
                    }
                }
            }
        }
    }

    private String getVehicleName(String id)
    {
        IAType12 vehicle = logEventData.getVehicle(id);
        if (vehicle != null)
        {
            return vehicle.getName() + " (" +  vehicle.getId() + ")";
        }
        else
        {
            return "Unknown";
        }
    }
    
    private List<IAType12> getDamagedBy(IAType3 destroyed)
    {
        Map<String, IAType12> damagedBy = new TreeMap<>();
        for (IAType2 damaged : logEventData.getDamageEvents())
        {
            IAType12 damagedVictim = logEventData.getVehicle(damaged.getVictim());
            IAType12 damagedVictor = logEventData.getVehicle(damaged.getVictim());
            if (destroyed.getVictim().equals(damagedVictim.getPid()))
            {
                damagedBy.put(damagedVictor.getId(), damagedVictor);
            }
        }
        return new ArrayList<>(damagedBy.values());
    }
}
