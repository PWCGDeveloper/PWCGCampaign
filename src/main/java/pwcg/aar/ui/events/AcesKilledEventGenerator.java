package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class AcesKilledEventGenerator
{
    private Campaign campaign;
    
    public static int NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY = 15;
    
    public AcesKilledEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }


    public List<AceKilledEvent> createAceKilledEvents(List<SquadronMember> acesKilledInMissionAndElapsedTime) throws PWCGException
    {
        List<AceKilledEvent> aceKilledEvents = new ArrayList<>();
        for (SquadronMember ace : acesKilledInMissionAndElapsedTime)
        {
            if (ace == null)
            {
                continue;
            }
            
            Squadron aceSquad =  ace.determineSquadron();
            if (aceSquad != null)
            {
                AceKilledEvent aceKilledEvent = makeAceKilledEvent(ace, aceSquad);
                if (aceKilledEvent != null)
                {
                    aceKilledEvents.add(aceKilledEvent);
                }
            }
        }
        
        return aceKilledEvents;
    }
    
    private AceKilledEvent makeAceKilledEvent(SquadronMember ace, Squadron aceSquadron) throws PWCGException
    {
        AceKilledEvent aceKilledEvent = null;
        
        if (ace.getSquadronMemberVictories().getAirToAirVictoryCount() >= NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY)
        {
            String status = SquadronMemberStatus.pilotStatusToStatusDescription(ace.getPilotActiveStatus());
    
            int aceSerialNumber = ace.getSerialNumber();
            int aceSquadronId = aceSquadron.getSquadronId();
            boolean isNewsworthy = true;
            aceKilledEvent = new AceKilledEvent(campaign, status, aceSquadronId, aceSerialNumber, campaign.getDate(), isNewsworthy);
        }
        
        return aceKilledEvent;
    }

}
