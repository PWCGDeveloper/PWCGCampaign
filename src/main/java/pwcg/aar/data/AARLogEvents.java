package pwcg.aar.data;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.aar.ui.events.model.AAREvent;
import pwcg.campaign.Campaign;

public class AARLogEvents
{
    private Campaign campaign;
    private Map<Integer, AAREvent> campaignLogEvents = new TreeMap<>();

    public AARLogEvents(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void addEvent(AAREvent event)
    {
        campaignLogEvents.put(event.getEventId(), event);
    }

    public void addEvents(List<? extends AAREvent> events)
    {
        for (AAREvent event : events)
        {
            if (!(event.getDate().before(campaign.getDate())))
            {
                addEvent(event);
            }
        }
    }

    public void merge(AARLogEvents source)
    {
        campaignLogEvents.putAll(source.getCampaignLogEvents());
    }

    public Map<Integer, AAREvent> getCampaignLogEvents()
    {
        return campaignLogEvents;
    }
}
