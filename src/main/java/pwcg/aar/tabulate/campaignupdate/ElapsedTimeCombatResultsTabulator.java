package pwcg.aar.tabulate.campaignupdate;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARElapsedTimeCombatResultsData;
import pwcg.aar.ui.events.PilotStatusEventGenerator;
import pwcg.aar.ui.events.PlaneStatusEventGenerator;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;

public class ElapsedTimeCombatResultsTabulator 
{
    private AARContext aarContext;
    
    private PilotStatusEventGenerator pilotStatusEventGenerator;
    private PlaneStatusEventGenerator planeStatusEventGenerator;
    private VictoryEventGenerator victoryEventGenerator;
    private AARElapsedTimeCombatResultsData elapsedTimeCombatResultsData = new AARElapsedTimeCombatResultsData();
    
    public ElapsedTimeCombatResultsTabulator (Campaign campaign, AARContext aarContext)
    {
        this.aarContext = aarContext;
        
        pilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        planeStatusEventGenerator = new PlaneStatusEventGenerator(campaign);
        victoryEventGenerator = new VictoryEventGenerator(campaign);
    }
        
    public AARElapsedTimeCombatResultsData tabulateCombatResultsForElapsedTime() throws PWCGException
    {
        createLossesForPilots();
        createLossesForEquipment();
        createVictoryEventsForSquadronMembers();
        return elapsedTimeCombatResultsData;
    }

    private void createLossesForPilots() throws PWCGException
    {
        Map<Integer, PilotStatusEvent> pilotsLostInMission = pilotStatusEventGenerator.createPilotLossEvents(aarContext.getPersonnelLosses());
        for (PilotStatusEvent pilotLostEvent : pilotsLostInMission.values())
        {
            elapsedTimeCombatResultsData.addPilotLost(pilotLostEvent);
        }
    }

    private void createLossesForEquipment() throws PWCGException
    {
        Map<Integer, PlaneStatusEvent> planesLostInMission = planeStatusEventGenerator.createPlaneLossEvents(aarContext.getEquipmentLosses());
        for (PlaneStatusEvent planeLostEvent : planesLostInMission.values())
        {
            elapsedTimeCombatResultsData.addPlaneLost(planeLostEvent);
        }
    }

    private void createVictoryEventsForSquadronMembers() throws PWCGException
    {
        Map<Integer, List<Victory>> victoryAwardByPilotInMission = aarContext.getReconciledMissionVictoryData().getVictoryAwardsByPilot();
        List<VictoryEvent> victoriesInMission = victoryEventGenerator.createPilotVictoryEvents(victoryAwardByPilotInMission);
        for (VictoryEvent victoryEvent : victoriesInMission)
        {
            elapsedTimeCombatResultsData.addVictory(victoryEvent);
        }
    }
}
