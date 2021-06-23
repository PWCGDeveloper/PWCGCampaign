package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.VictoryEvent;

public class VictoryEvents
{
    private List<VictoryEvent> victoryEvents = new ArrayList<>();

    public List<VictoryEvent> getOutOfMissionVictoryEvents()
    {
        return victoryEvents;
    }

    public void addVictory(VictoryEvent victory)
    {
        victoryEvents.add(victory);
        
    }

    public void merge(VictoryEvents victoryEventsToMerge)
    {
        victoryEvents.addAll(victoryEventsToMerge.getOutOfMissionVictoryEvents());
    }
}
