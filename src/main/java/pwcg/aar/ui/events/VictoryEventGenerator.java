package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;

public class VictoryEventGenerator
{
    private Campaign campaign;
    
    public VictoryEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<VictoryEvent> createPilotVictoryEvents(Map<Integer, List<Victory>> victoryAwardByPilot) throws PWCGException
    {
        List<VictoryEvent> victoryEventsForSquadronMembers = new ArrayList<>();
        
        for (Integer serialNumber : victoryAwardByPilot.keySet())
        {
            SquadronMember squadronMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (squadronMember != null)
            {
                List<Victory> victoriesForSquadronMember = victoryAwardByPilot.get(serialNumber);
                for (Victory victory : victoriesForSquadronMember)
                {
                    VictoryEvent victoryEvent = makeVictoryEvent(squadronMember, victory);
                    victoryEventsForSquadronMembers.add(victoryEvent);
                }
            }
        }
        
        return victoryEventsForSquadronMembers;
    }

    private VictoryEvent makeVictoryEvent(SquadronMember pilot, Victory victory) throws PWCGException
    {
        boolean isNewsWorthy = true;
        VictoryEvent victoryEvent = new VictoryEvent(campaign, victory, pilot.getSquadronId(), pilot.getSerialNumber(), campaign.getDate(), isNewsWorthy);
        return victoryEvent;
     }

}
