package pwcg.aar.tabulate.debrief;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.VictoryEvents;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;

public class VictoryPanelEventTabulator
{
    private AARContext aarContext;
    
    private VictoryEventGenerator victoryEventGenerator;
    private VictoryEvents victoryEvents = new VictoryEvents();
    
    public VictoryPanelEventTabulator (Campaign campaign, AARContext aarContext)
    {
        this.aarContext = aarContext;
        victoryEventGenerator = new VictoryEventGenerator(campaign);
    }
        
    public VictoryEvents createVictoryEvents() throws PWCGException
    {
        Map<Integer, List<Victory>> victoryAwardByCrewMemberOutOfMission = aarContext.getPersonnelAcheivements().getVictoriesByCrewMember();
        List<VictoryEvent> victoriesOutOfMission = victoryEventGenerator.createCrewMemberVictoryEvents(victoryAwardByCrewMemberOutOfMission);
        for (VictoryEvent victoryEvent : victoriesOutOfMission)
        {
            victoryEvents.addVictory(victoryEvent);
        }
        return victoryEvents;
    }
}
