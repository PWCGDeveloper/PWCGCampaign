package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;

public class VictoryEventGenerator
{
    private Campaign campaign;
    
    public VictoryEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<VictoryEvent> createCrewMemberVictoryEvents(Map<Integer, List<Victory>> victoryAwardByCrewMember) throws PWCGException
    {
        List<VictoryEvent> victoryEventsForCrewMembers = new ArrayList<>();
        
        for (Integer serialNumber : victoryAwardByCrewMember.keySet())
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (crewMember != null)
            {
                List<Victory> victoriesForCrewMember = victoryAwardByCrewMember.get(serialNumber);
                for (Victory victory : victoriesForCrewMember)
                {
                    VictoryEvent victoryEvent = makeVictoryEvent(crewMember, victory);
                    victoryEventsForCrewMembers.add(victoryEvent);
                }
            }
        }
        
        return victoryEventsForCrewMembers;
    }

    private VictoryEvent makeVictoryEvent(CrewMember crewMember, Victory victory) throws PWCGException
    {
        boolean isNewsWorthy = true;
        VictoryEvent victoryEvent = new VictoryEvent(campaign, victory, crewMember.getCompanyId(), crewMember.getSerialNumber(), campaign.getDate(), isNewsWorthy);
        return victoryEvent;
     }

}
