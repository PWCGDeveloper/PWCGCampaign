package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.core.exception.PWCGException;

public class AARCombatReportMapData
{
    private List<LogBase> chronologicalEvents = new ArrayList<>();

    public AARCombatReportMapData()
    {
    }
    
    public List<LogBase> getChronologicalEvents()
    {
        return chronologicalEvents;
    }

    public void addChronologicalEvents(List<LogBase> sourceChronologicalEvents) throws PWCGException
    {
        for (LogBase logEvent : sourceChronologicalEvents)
        {
            chronologicalEvents.add(logEvent);
        }
    }
}
