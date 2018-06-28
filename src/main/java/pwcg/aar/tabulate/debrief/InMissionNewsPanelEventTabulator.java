package pwcg.aar.tabulate.debrief;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.ui.display.model.AARNewsPanelData;
import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.core.exception.PWCGException;

public class InMissionNewsPanelEventTabulator
{
    private Campaign campaign;

    private AARPersonnelLosses personnelLosses;
    private AARNewsPanelData newsPanelData = new AARNewsPanelData();

    public InMissionNewsPanelEventTabulator (Campaign campaign, AARPersonnelLosses personnelLosses)
    {
        this.campaign = campaign;
        this.personnelLosses = personnelLosses;
    }
    
    public AARNewsPanelData createNewspaperEvents() throws PWCGException 
    {
        AcesKilledEventGenerator acesKilledEventGenerator = new AcesKilledEventGenerator(campaign);
        List<Ace> acesKilled = new ArrayList<>(personnelLosses.getAcesKilled().values());
        List<AceKilledEvent> acesKilledDuringElapsedTime = acesKilledEventGenerator.createAceKilledEvents(acesKilled);
        newsPanelData.setAcesKilledDuringElapsedTime(acesKilledDuringElapsedTime);
        return newsPanelData;
    }
}
