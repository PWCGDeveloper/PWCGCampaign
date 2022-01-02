package pwcg.aar.tabulate.campaignupdate;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.CampaignUpdateEvents;
import pwcg.aar.ui.events.CrewMemberStatusEventGenerator;
import pwcg.aar.ui.events.PlaneStatusEventGenerator;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateEventGenerator 
{
    private AARContext aarContext;
    
    private CrewMemberStatusEventGenerator crewMemberStatusEventGenerator;
    private PlaneStatusEventGenerator planeStatusEventGenerator;
    private VictoryEventGenerator victoryEventGenerator;
    private CampaignUpdateEvents campaignUpdateEvents = new CampaignUpdateEvents();
    
    public CampaignUpdateEventGenerator (Campaign campaign, AARContext aarContext)
    {
        this.aarContext = aarContext;
        
        crewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        planeStatusEventGenerator = new PlaneStatusEventGenerator(campaign);
        victoryEventGenerator = new VictoryEventGenerator(campaign);
    }
        
    public CampaignUpdateEvents tabulateCombatResultsForElapsedTime() throws PWCGException
    {
        createCrewMemberLossEvents();
        createEquipmentLossEvents();
        createVictoryEvents();
        return campaignUpdateEvents;
    }

    private void createCrewMemberLossEvents() throws PWCGException
    {
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = crewMemberStatusEventGenerator.createCrewMemberLossEvents(aarContext.getPersonnelLosses());
        for (CrewMemberStatusEvent crewMemberLostEvent : crewMembersLostInMission.values())
        {
            campaignUpdateEvents.addCrewMemberLost(crewMemberLostEvent);
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
        Map<Integer, List<Victory>> victoryAwardByCrewMemberInMission = aarContext.getPersonnelAcheivements().getVictoriesByCrewMember();
        List<VictoryEvent> victoriesInMission = victoryEventGenerator.createCrewMemberVictoryEvents(victoryAwardByCrewMemberInMission);
        for (VictoryEvent victoryEvent : victoriesInMission)
        {
            campaignUpdateEvents.addVictory(victoryEvent);
        }
    }
}
