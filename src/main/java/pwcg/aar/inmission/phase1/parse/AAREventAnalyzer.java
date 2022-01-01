package pwcg.aar.inmission.phase1.parse;

import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType12;
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
            PWCGLogger.log(LogLevel.DEBUG, victim + " killed by " + victor);
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
}
