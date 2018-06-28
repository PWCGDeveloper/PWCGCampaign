package pwcg.aar.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.AAREvent;

public class AARLogEvents
{
    private List<AAREvent> campaignLogEvents = new ArrayList<>();

    public void addEvent(AAREvent event)
    {
        campaignLogEvents.add(event);
    }

    public void addEvents(List<? extends AAREvent> events)
    {
        campaignLogEvents.addAll(events);
    }

    public void merge(AARLogEvents source)
    {
        campaignLogEvents.addAll(source.getCampaignLogEvents());
    }

    public List<AAREvent> getCampaignLogEvents()
    {
        return campaignLogEvents;
    }
}
