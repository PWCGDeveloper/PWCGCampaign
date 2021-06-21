package pwcg.aar.tabulate.debrief;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AAROutOfMissionVictoryPanelData;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;

public class SquadronOutOfMissionVictoryPanelEventTabulator
{
    private AARContext aarContext;
    
    private VictoryEventGenerator victoryEventGenerator;
    private AAROutOfMissionVictoryPanelData squadMembersOutOfMissionVictoryData = new AAROutOfMissionVictoryPanelData();
    
    public SquadronOutOfMissionVictoryPanelEventTabulator (Campaign campaign, AARContext aarContext)
    {
        this.aarContext = aarContext;
        
        victoryEventGenerator = new VictoryEventGenerator(campaign);
    }
        
    public AAROutOfMissionVictoryPanelData tabulateOutOfMissionVictoriesForSquadron() throws PWCGException
    {
        createVictoryEventsForSquadronMembers();
        return squadMembersOutOfMissionVictoryData;
    }

    private void createVictoryEventsForSquadronMembers() throws PWCGException
    {
        Map<Integer, List<Victory>> victoryAwardByPilotInMission = aarContext.getReconciledMissionVictoryData().getVictoryAwardsByPilot();
        List<VictoryEvent> victoriesInMission = victoryEventGenerator.createPilotVictoryEvents(victoryAwardByPilotInMission);
        for (VictoryEvent victoryEvent : victoriesInMission)
        {
            squadMembersOutOfMissionVictoryData.addVictory(victoryEvent);
        }
        
        Map<Integer, List<Victory>> victoryAwardByPilotOutOfMission = aarContext.getPersonnelAcheivements().getVictoriesByPilot();
        List<VictoryEvent> victoriesOutOfMission = victoryEventGenerator.createPilotVictoryEvents(victoryAwardByPilotOutOfMission);
        for (VictoryEvent victoryEvent : victoriesOutOfMission)
        {
            squadMembersOutOfMissionVictoryData.addVictory(victoryEvent);
        }
    }
}
