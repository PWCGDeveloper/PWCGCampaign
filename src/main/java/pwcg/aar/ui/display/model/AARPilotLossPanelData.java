package pwcg.aar.ui.display.model;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.ui.events.model.PilotStatusEvent;

public class AARPilotLossPanelData
{
    private Map<Integer, PilotStatusEvent> squadMembersLost = new HashMap<>();

    public Map<Integer, PilotStatusEvent> getSquadMembersLost()
    {
        return squadMembersLost;
    }

    public void setSquadMembersLost(Map<Integer, PilotStatusEvent> squadMembersLost)
    {
        this.squadMembersLost = squadMembersLost;
    }

    public void merge(AARPilotLossPanelData pilotLossPanelData)
    {
        squadMembersLost.putAll(pilotLossPanelData.getSquadMembersLost());        
    }
}
