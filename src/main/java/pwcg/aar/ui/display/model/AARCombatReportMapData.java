package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;

public class AARCombatReportMapData
{
    private List<LogBase> chronologicalEvents = new ArrayList<>();

    public List<LogBase> getChronologicalEvents()
    {
        return chronologicalEvents;
    }

    public void addChronologicalEvents(List<LogBase> chronologicalEvents)
    {
        this.chronologicalEvents.addAll(chronologicalEvents);
    }
}
