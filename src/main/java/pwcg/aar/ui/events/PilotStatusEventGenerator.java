package pwcg.aar.ui.events;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class PilotStatusEventGenerator
{    
    private Campaign campaign;
	private Map<Integer, PilotStatusEvent> pilotsLost = new HashMap<>();
    
    public PilotStatusEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Map<Integer, PilotStatusEvent> createPilotLossEvents(AARPersonnelLosses personnelResults) throws PWCGException
    {
        for (SquadronMember pilot : personnelResults.getPersonnelKilled().values())
        {
            PilotStatusEvent pilotKiaEvent = makePilotLostEvent(pilot, SquadronMemberStatus.STATUS_KIA);
            pilotsLost.put(pilot.getSerialNumber(), pilotKiaEvent);
        }
        
        for (SquadronMember pilot : personnelResults.getPersonnelCaptured().values())
        {
            PilotStatusEvent pilotCapturedEvent = makePilotLostEvent(pilot, SquadronMemberStatus.STATUS_CAPTURED);
            pilotsLost.put(pilot.getSerialNumber(),pilotCapturedEvent);
        }
        
        for (SquadronMember pilot : personnelResults.getPersonnelMaimed().values())
        {
            PilotStatusEvent pilotWoundedEvent = makePilotLostEvent(pilot, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
            pilotsLost.put(pilot.getSerialNumber(),pilotWoundedEvent);
        }
        
        for (SquadronMember pilot : personnelResults.getAcesKilled().values())
        {
            PilotStatusEvent aceKiaEvent = makePilotLostEvent(pilot, SquadronMemberStatus.STATUS_KIA);
            pilotsLost.put(pilot.getSerialNumber(),aceKiaEvent);
        }
        
        return pilotsLost;
    }

    protected PilotStatusEvent makePilotLostEvent(SquadronMember pilot, int pilotStatus) throws PWCGException
    {
        PilotStatusEvent pilotLostEvent = new PilotStatusEvent(pilot.getSquadronId());
        pilotLostEvent.setPilotName(pilot.getNameAndRank());
        pilotLostEvent.setSerialNumber(pilot.getSerialNumber());
        pilotLostEvent.setDate(campaign.getDate());
        Squadron squadron = pilot.determineSquadron();
        if (squadron != null)
        {
            pilotLostEvent.setSquadron(squadron.determineDisplayName(campaign.getDate()));     
        }
        pilotLostEvent.setStatus(pilotStatus);

        return pilotLostEvent;
    }

}
