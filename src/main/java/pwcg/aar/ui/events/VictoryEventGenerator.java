package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
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
        VictoryEvent victoryEvent = new VictoryEvent(pilot.getSquadronId());
        victoryEvent.setPilotName(pilot.getNameAndRank());
        victoryEvent.setDate(campaign.getDate());
        Squadron squadronName = PWCGContext.getInstance().getSquadronManager().getSquadron(pilot.getSquadronId());
        victoryEvent.setSquadron(squadronName.determineDisplayName(campaign.getDate()));

        victoryEvent.setVictory(victory);
        
        return victoryEvent;
     }

}
