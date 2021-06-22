package pwcg.aar.tabulate.campaignupdate;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.CampaignUpdateEvents;
import pwcg.aar.ui.events.PilotStatusEventGenerator;
import pwcg.aar.ui.events.PlaneStatusEventGenerator;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateEventGenerator 
{
    private AARContext aarContext;
    
    private PilotStatusEventGenerator pilotStatusEventGenerator;
    private PlaneStatusEventGenerator planeStatusEventGenerator;
    private VictoryEventGenerator victoryEventGenerator;
    private CampaignUpdateEvents campaignUpdateEvents = new CampaignUpdateEvents();
    
    public CampaignUpdateEventGenerator (Campaign campaign, AARContext aarContext)
    {
        this.aarContext = aarContext;
        
        pilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        planeStatusEventGenerator = new PlaneStatusEventGenerator(campaign);
        victoryEventGenerator = new VictoryEventGenerator(campaign);
    }
        
    public CampaignUpdateEvents tabulateCombatResultsForElapsedTime() throws PWCGException
    {
        createPilotLossEvents();
        createEquipmentLossEvents();
        createVictoryEvents();
        return campaignUpdateEvents;
    }

    private void createPilotLossEvents() throws PWCGException
    {
        Map<Integer, PilotStatusEvent> pilotsLostInMission = pilotStatusEventGenerator.createPilotLossEvents(aarContext.getPersonnelLosses());
        for (PilotStatusEvent pilotLostEvent : pilotsLostInMission.values())
        {
            campaignUpdateEvents.addPilotLost(pilotLostEvent);
        }
    }

    private void createEquipmentLossEvents() throws PWCGException
    {
        Map<Integer, PlaneStatusEvent> planesLostInMission = planeStatusEventGenerator.createPlaneLossEvents(aarContext.getEquipmentLosses());
        for (PlaneStatusEvent planeLostEvent : planesLostInMission.values())
        {
            campaignUpdateEvents.addPlaneLost(planeLostEvent);
        }
    }

    private void createVictoryEvents() throws PWCGException
    {
        Map<Integer, List<Victory>> victoryAwardByPilotInMission = aarContext.getPersonnelAcheivements().getVictoriesByPilot();
        List<VictoryEvent> victoriesInMission = victoryEventGenerator.createPilotVictoryEvents(victoryAwardByPilotInMission);
        for (VictoryEvent victoryEvent : victoriesInMission)
        {
            campaignUpdateEvents.addVictory(victoryEvent);
        }
    }
}
