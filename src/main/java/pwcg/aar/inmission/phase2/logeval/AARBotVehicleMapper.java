package pwcg.aar.inmission.phase2.logeval;

import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class AARBotVehicleMapper 
{
    private AARLogEventData logEventData;
    private Map <String, LogPlane> planeAiEntities;
    
    public AARBotVehicleMapper (AARLogEventData logEventData)
    {
        this.logEventData = logEventData;
    }

    public void mapBotsToCrews(Map <String, LogPlane> planeAiEntities) throws PWCGException
    {
        this.planeAiEntities = planeAiEntities;        
        mapBotsToCrewPosition(logEventData.getBots());
    }
    
    private void mapBotsToCrewPosition(List<IAType12> botList) throws PWCGException
    {
        for (IAType12 atype12Bot : botList)
        {
            LogPlane planeResult = getMissionResultPlaneById(atype12Bot.getPid());
            if (planeResult != null)
            {
                if (atype12Bot.getType().contains("BotPilot") || atype12Bot.getType().contains("Common Bot"))
                {
                    mapBotPilot(atype12Bot, planeResult);
                }
            }
            else
            {
                Logger.log(LogLevel.ERROR, "While adding bot = No plane found for bot: " + atype12Bot.getId() + " for plane id: " + atype12Bot.getPid());
            }
        }
    }

    private void mapBotPilot(IAType12 atype12, LogPlane planeResult) throws PWCGException
    {
        Logger.log(LogLevel.DEBUG, "Add Pilot bot for : " + planeResult.getLogPilot().getSerialNumber() + "   " + atype12.getId());
        planeResult.mapBotToCrew(atype12.getId());
    }
    
    private LogPlane getMissionResultPlaneById(String id)
    {
        for (LogPlane planeResult : planeAiEntities.values())
        {
            if (planeResult.isWithPlane(id))
            {
                return planeResult;
            }
        }

        return null;
    }

}

