package pwcg.aar.ui.display.model;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.ui.events.model.CrewMemberStatusEvent;

public class AARCrewMemberLossPanelData
{
    private Map<Integer, CrewMemberStatusEvent> squadMembersLost = new HashMap<>();

    public Map<Integer, CrewMemberStatusEvent> getSquadMembersLost()
    {
        return squadMembersLost;
    }

    public void setSquadMembersLost(Map<Integer, CrewMemberStatusEvent> squadMembersLost)
    {
        this.squadMembersLost = squadMembersLost;
    }
    

    public void merge(AARCrewMemberLossPanelData eventData)
    {
        for (CrewMemberStatusEvent event : eventData.getSquadMembersLost().values())
        {
            squadMembersLost.put(event.getCrewMemberSerialNumber(), event);
        }
    }
}
