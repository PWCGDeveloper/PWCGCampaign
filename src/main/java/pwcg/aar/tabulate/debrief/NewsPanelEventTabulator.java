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
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.newspapers.Newspaper;
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
        List<Newspaper> newspapers = newspaperEventGenerator.createNewspaperEventsForElapsedTime();
        newsPanelData.setNewspaperEventsDuringElapsedTime(newspapers);
        
        return newsPanelData;
    }
    
    private AARNewsPanelData createAcesKilledForNewspaperEvents() throws PWCGException 
    {
        List<CrewMember> acesKilledInMissionAndElapsedTime = mergeAcesKilledInMissionAndElapsedTime();
        
        AcesKilledEventGenerator acesKilledEventGenerator = new AcesKilledEventGenerator(campaign);
        List<AceKilledEvent> acesKilledDuringElapsedTime = acesKilledEventGenerator.createAceKilledEvents(acesKilledInMissionAndElapsedTime);
        newsPanelData.setAcesKilledDuringElapsedTime(acesKilledDuringElapsedTime);
        
        return newsPanelData;
    }
    
    private List <CrewMember> mergeAcesKilledInMissionAndElapsedTime() throws PWCGException
    {
        Map<Integer, CrewMember> acesKilledMap = new HashMap<>();
        acesKilledMap.putAll(aarContext.getPersonnelLosses().getAcesKilled(campaign));        
        return new ArrayList<CrewMember>(acesKilledMap.values());
    }
}
