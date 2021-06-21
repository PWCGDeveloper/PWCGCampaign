package pwcg.aar.outofmission.phase4.ElapsedTIme;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.EndOfWarEvent;
import pwcg.aar.ui.events.model.SquadronMoveEvent;

public class ElapsedTimeEvents
{
    private List<SquadronMoveEvent> squadronMoveEvents = new ArrayList<>();
    private EndOfWarEvent endOfWarEvent = null;

    public EndOfWarEvent getEndOfWarEvent()
    {
        return endOfWarEvent;
    }

    public void setEndOfWarEvent(EndOfWarEvent endOfWarEvent)
    {
        this.endOfWarEvent = endOfWarEvent;
    }

    public List<SquadronMoveEvent> getSquadronMoveEvents()
    {
        return squadronMoveEvents;
    }

    public void addSquadronMoveEvent(SquadronMoveEvent squadronMoveEvent)
    {
        this.squadronMoveEvents.add(squadronMoveEvent);
    }
    
    public void merge(ElapsedTimeEvents elapsedTimeEvents)
    {
        squadronMoveEvents.addAll(elapsedTimeEvents.getSquadronMoveEvents());
        if (endOfWarEvent == null)
        {
            endOfWarEvent = elapsedTimeEvents.getEndOfWarEvent();
        }
    }
}
