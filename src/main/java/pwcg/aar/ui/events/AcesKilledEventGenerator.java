package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
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


    public List<AceKilledEvent> createAceKilledEvents(List<Ace> acesKilledInMissionAndElapsedTime) throws PWCGException
    {
        List<AceKilledEvent> aceKilledEvents = new ArrayList<>();
        for (Ace ace : acesKilledInMissionAndElapsedTime)
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
    
    private AceKilledEvent makeAceKilledEvent(SquadronMember ace, Squadron squad) throws PWCGException
    {
        AceKilledEvent aceKilledEvent = null;
        
        if (ace.getSquadronMemberVictories().getAirToAirVictories() >= NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY)
        {
            String status = SquadronMemberStatus.pilotStatusToStatusDescription(ace.getPilotActiveStatus());
    
            aceKilledEvent = new AceKilledEvent(ace.getSquadronId(), ace.getSerialNumber());
    
            aceKilledEvent.setPilotName(ace.getNameAndRank());
            aceKilledEvent.setDate(campaign.getDate());
            String squadName = "";
            if (!(squad == null))           
            {
                squadName = squad.determineDisplayName(campaign.getDate());
            }
            aceKilledEvent.setSquadron(squadName);
            aceKilledEvent.setStatus(status);
        }
        
        return aceKilledEvent;
    }

}
