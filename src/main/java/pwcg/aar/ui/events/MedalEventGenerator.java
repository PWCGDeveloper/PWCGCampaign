package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.MedalEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class MedalEventGenerator
{
    private Campaign campaign;
    
    public MedalEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<MedalEvent> createCrewMemberMedalEvents(Map<Integer, Map<String, Medal>> medalAwardByCrewMember) throws PWCGException
    {
        List<MedalEvent> medalEventsForCrewMembers = new ArrayList<>();
        
        for (Integer crewMemberSerialNumber : medalAwardByCrewMember.keySet())
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(crewMemberSerialNumber);
            if (crewMember != null)
            {
                Map<String, Medal> medalsForCrewMember = medalAwardByCrewMember.get(crewMemberSerialNumber);
                for (Medal medal : medalsForCrewMember.values())
                {
                    MedalEvent medalEvent = makeMedalEvent(crewMember, medal);
                    medalEventsForCrewMembers.add(medalEvent);
                }
            }
        }
        
        return medalEventsForCrewMembers;
    }
    
    private MedalEvent makeMedalEvent(CrewMember crewMember, Medal medal) throws PWCGException
    {
        boolean isNewsworthy = true;
        if (medal.getMedalName().contains("CrewMembers Badge"))
        {
            isNewsworthy = false;
        }

        MedalEvent medalEvent = new MedalEvent(campaign, medal.getMedalName(), crewMember.getCompanyId(), crewMember.getSerialNumber(), campaign.getDate(), isNewsworthy);
        return medalEvent;
    }
}
