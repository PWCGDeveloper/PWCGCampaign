package pwcg.aar.tabulate.debrief;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARCrewMemberLossPanelData;
import pwcg.aar.ui.events.CrewMemberStatusEventGenerator;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class CrewMemberLossPanelEventTabulator
{
    private Campaign campaign;
    private AARContext aarContext;

    private AARCrewMemberLossPanelData crewMemberLossPanelData = new AARCrewMemberLossPanelData();

    public CrewMemberLossPanelEventTabulator (Campaign campaign,AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
        
    public AARCrewMemberLossPanelData tabulateForAARCrewMemberLossPanel() throws PWCGException
    {
        CrewMemberStatusEventGenerator crewMemberLossEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        Map<Integer, CrewMemberStatusEvent> allCrewMembersLost = crewMemberLossEventGenerator.createCrewMemberLossEvents(aarContext.getPersonnelLosses());
        crewMemberLossPanelData.setSquadMembersLost(allCrewMembersLost);
        return crewMemberLossPanelData;
    }
}
