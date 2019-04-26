package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.EndOfWarEvent;
import pwcg.aar.ui.events.model.SquadronMoveEvent;

public class ElapsedTimeEvents
{
    private List<SquadronMoveEvent> squadronMoveEvents = new ArrayList<>();
    private EndOfWarEvent endOfWarEvent;

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
}
