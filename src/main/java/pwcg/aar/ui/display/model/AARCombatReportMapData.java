package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARCombatReportMapData
{
    private Campaign campaign;
    private List<LogBase> chronologicalEvents = new ArrayList<>();

    public AARCombatReportMapData(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public List<LogBase> getChronologicalEvents()
    {
        return chronologicalEvents;
    }

    public void addChronologicalEvents(List<LogBase> sourceChronologicalEvents) throws PWCGException
    {
        for (LogBase logEvent : sourceChronologicalEvents)
        {
            if (logEvent instanceof LogVictory)
            {
                LogVictory logVictory = (LogVictory)logEvent;
                if (isVictoryAssociatedWithPlayerSquadron(logVictory))
                {
                    chronologicalEvents.add(logEvent);
                }
            }
            else
            {
                chronologicalEvents.add(logEvent);
            }
        }
    }
    

    public boolean isVictoryAssociatedWithPlayerSquadron(LogVictory logVictory) throws PWCGException
    {
        if (logVictory.getVictor() instanceof LogPlane)
        {
            LogPlane logPlane = (LogPlane)logVictory.getVictor();
            if (logPlane.isLogPlaneFromPlayerSquadron(campaign))
            {
                return true;
            }
        }
        if (logVictory.getVictim() instanceof LogPlane)
        {
            LogPlane logPlane = (LogPlane)logVictory.getVictim();
            if (logPlane.isLogPlaneFromPlayerSquadron(campaign))
            {
                return true;
            }
        }
        
        return false;
    }
}
