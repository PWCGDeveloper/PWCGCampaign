package pwcg.aar.ui.events;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;

public class CrewMemberStatusEventGenerator
{    
    private Campaign campaign;
	private Map<Integer, CrewMemberStatusEvent> crewMembersLost = new HashMap<>();
    
    public CrewMemberStatusEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Map<Integer, CrewMemberStatusEvent> createCrewMemberLossEvents(AARPersonnelLosses personnelResults) throws PWCGException
    {
        for (CrewMember crewMember : personnelResults.getPersonnelKilled().values())
        {
            CrewMemberStatusEvent crewMemberKiaEvent = makeCrewMemberLostEvent(crewMember, CrewMemberStatus.STATUS_KIA);
            crewMembersLost.put(crewMember.getSerialNumber(), crewMemberKiaEvent);
        }
        
        for (CrewMember crewMember : personnelResults.getPersonnelCaptured().values())
        {
            CrewMemberStatusEvent crewMemberCapturedEvent = makeCrewMemberLostEvent(crewMember, CrewMemberStatus.STATUS_CAPTURED);
            crewMembersLost.put(crewMember.getSerialNumber(),crewMemberCapturedEvent);
        }
        
        for (CrewMember crewMember : personnelResults.getPersonnelMaimed().values())
        {
            CrewMemberStatusEvent crewMemberWoundedEvent = makeCrewMemberLostEvent(crewMember, CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
            crewMembersLost.put(crewMember.getSerialNumber(),crewMemberWoundedEvent);
        }
        
        for (CrewMember crewMember : personnelResults.getAcesKilled(campaign).values())
        {
            CrewMemberStatusEvent aceKiaEvent = makeCrewMemberLostEvent(crewMember, CrewMemberStatus.STATUS_KIA);
            crewMembersLost.put(crewMember.getSerialNumber(),aceKiaEvent);
        }
        
        return crewMembersLost;
    }

    protected CrewMemberStatusEvent makeCrewMemberLostEvent(CrewMember crewMember, int crewMemberStatus) throws PWCGException
    {
        boolean isNewsworthy = true;
        CrewMemberStatusEvent crewMemberLostEvent = new CrewMemberStatusEvent(campaign, crewMemberStatus, crewMember.getCompanyId(), crewMember.getSerialNumber(), campaign.getDate(), isNewsworthy);
        return crewMemberLostEvent;
    }

}
