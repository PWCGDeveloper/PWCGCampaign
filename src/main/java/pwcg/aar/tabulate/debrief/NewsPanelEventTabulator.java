package pwcg.aar.tabulate.debrief;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARNewsPanelData;
import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.aar.ui.events.NewspaperEventGenerator;
import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.aar.ui.events.model.NewspaperEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.core.exception.PWCGException;

public class NewsPanelEventTabulator
{
    private Campaign campaign;
    private AARContext aarContext;

    private AARNewsPanelData newsPanelData = new AARNewsPanelData();

    public NewsPanelEventTabulator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public AARNewsPanelData createNewspaperEvents() throws PWCGException 
    {
        createDateBasedNewspaperEvents();        
        createAcesKilledForNewspaperEvents();        
        return newsPanelData;
    }

    private AARNewsPanelData createDateBasedNewspaperEvents() throws PWCGException 
    {
        NewspaperEventGenerator newspaperEventGenerator = new NewspaperEventGenerator(campaign, aarContext.getNewDate());
        List<NewspaperEvent> newspaperEventList = newspaperEventGenerator.createNewspaperEventsForElapsedTime();
        newsPanelData.setNewspaperEventsDuringElapsedTime(newspaperEventList);
        
        return newsPanelData;
    }
    
    private AARNewsPanelData createAcesKilledForNewspaperEvents() throws PWCGException 
    {
        List<Ace> acesKilledInMissionAndElapsedTime = mergeAcesKilledInMissionAndElapsedTime();
        
        AcesKilledEventGenerator acesKilledEventGenerator = new AcesKilledEventGenerator(campaign);
        List<AceKilledEvent> acesKilledDuringElapsedTime = acesKilledEventGenerator.createAceKilledEvents(acesKilledInMissionAndElapsedTime);
        newsPanelData.setAcesKilledDuringElapsedTime(acesKilledDuringElapsedTime);
        
        return newsPanelData;
    }
    
    private List <Ace> mergeAcesKilledInMissionAndElapsedTime()
    {
        Map<Integer, Ace> acesKilledMap = new HashMap<>();
        acesKilledMap.putAll(aarContext.getReconciledInMissionData().getPersonnelLosses().getAcesKilled());        
        acesKilledMap.putAll(aarContext.getReconciledOutOfMissionData().getPersonnelLosses().getAcesKilled());        
        return new ArrayList<Ace>(acesKilledMap.values());
    }


}
