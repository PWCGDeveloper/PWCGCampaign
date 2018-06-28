package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.MedalEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class MedalEventGenerator
{
    private Campaign campaign;
    
    public MedalEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<MedalEvent> createPilotMedalEvents(Map<Integer, Map<String, Medal>> medalAwardByPilot) throws PWCGException
    {
        List<MedalEvent> medalEventsForSquadronMembers = new ArrayList<>();
        
        for (Integer pilotSerialNumber : medalAwardByPilot.keySet())
        {
            SquadronMember squadronMember = campaign.getPersonnelManager().getActiveCampaignMember(pilotSerialNumber);
            if (squadronMember != null)
            {
                Map<String, Medal> medalsForSquadronMember = medalAwardByPilot.get(pilotSerialNumber);
                for (Medal medal : medalsForSquadronMember.values())
                {
                    MedalEvent medalEvent = makeMedalEvent(squadronMember, medal);
                    medalEventsForSquadronMembers.add(medalEvent);
                }
            }
        }
        
        return medalEventsForSquadronMembers;
    }
    
    private MedalEvent makeMedalEvent(SquadronMember pilot, Medal medal) throws PWCGException
    {
        MedalEvent medalEvent = new MedalEvent();
        medalEvent.setPilot(pilot);
        medalEvent.setMedal(medal.getMedalName());
        medalEvent.setDate(campaign.getDate());
        Squadron squadron = pilot.determineSquadron();
        if (squadron != null)
        {
            medalEvent.setSquadron(squadron.determineDisplayName(campaign.getDate()));
        }
        
        if (medal.getMedalName().contains("Pilots Badge"))
        {
            medalEvent.setNewsWorthy(false);
        }
        
        return medalEvent;
    }
}
