package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.VictoryEvent;

public class AAROutOfMissionVictoryPanelData
{
    private List<VictoryEvent> squadMembersOutOfMissionVictories = new ArrayList<>();

    public List<VictoryEvent> getOutOfMissionVictoryEvents()
    {
        return squadMembersOutOfMissionVictories;
    }

    public void addVictory(VictoryEvent victory)
    {
        squadMembersOutOfMissionVictories.add(victory);
        
    }

    public void merge(AAROutOfMissionVictoryPanelData outOfMissionVictoryPanelData)
    {
        squadMembersOutOfMissionVictories.addAll(outOfMissionVictoryPanelData.getOutOfMissionVictoryEvents());
    }
}
