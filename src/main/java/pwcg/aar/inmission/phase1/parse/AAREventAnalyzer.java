package pwcg.aar.inmission.phase1.parse;

import java.util.Map;

import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.ClaimDenier;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.LogParser;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.logfiles.event.IAType3;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class AAREventAnalyzer
{
    private Campaign campaign;
    private String logFileName;
    private LogEventData logEventData = new LogEventData();

    public AAREventAnalyzer(Campaign campaign, String logFileName)
    {
        this.campaign = campaign;
        this.logFileName = logFileName;
    }

    
    public void analyzeLogSet()
    {
        try
        {
            LogParser logParser = new LogParser();
            this.logEventData = logParser.parseLogFilesForMission(campaign, logFileName);
            analyze();
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }

    }
    
    private void analyze() throws PWCGException
    {
                
        for (IAType3 destroyed : logEventData.getDestroyedEvents())
        {
            logKilledBy(destroyed);
            logDamagedBy(destroyed);
        }
    }


    private void logKilledBy(IAType3 destroyed)
    {
        String victim = getVehicleName(destroyed.getVictim());
        String victor = getVehicleName(destroyed.getVictor());
        
        if (victim.contains(ClaimDenier.UNKNOWN) || victim.contains("noname"))
        {
            victim = getVehicleName(destroyed.getVictim());
            return;
        }
        
        PWCGLogger.log(LogLevel.INFO, victim + " killed by " + victor);

    }


    private void logDamagedBy(IAType3 destroyed)
    {
        String victim = getVehicleName(destroyed.getVictim());

        Map<String, Double> damagedBy = logEventData.getDamagedBy(destroyed.getVictim());
        for (String damagedByVictorId : damagedBy.keySet())
        {
            Double damagePercentage = damagedBy.get(damagedByVictorId);
            PWCGLogger.log(LogLevel.INFO, victim + " damaged by " + getVehicleName(damagedByVictorId) + " Damage percentage is " + damagePercentage);
        }
    }

    private String getVehicleName(String id)
    {
        IAType12 vehicle = logEventData.getVehicle(id);
        IAType12 bot = logEventData.getBot(id);
        if (vehicle != null)
        {
            return vehicle.getName() + " (" +  vehicle.getId() + ")";
        }
        else if(bot != null)
        {
            return bot.getName() + " (" +  bot.getId() + ")";
        }
        else
        {
            return "Unknown (" + id + ")";
        }
    }
}
