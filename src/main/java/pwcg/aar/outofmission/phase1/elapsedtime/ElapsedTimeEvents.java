package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.ui.events.model.EndOfWarEvent;
import pwcg.aar.ui.events.model.SquadronMoveEvent;

public class ElapsedTimeEvents
{
    private SquadronMoveEvent squadronMoveEvent;
    private EndOfWarEvent endOfWarEvent;

    public EndOfWarEvent getEndOfWarEvent()
    {
        return endOfWarEvent;
    }

    public void setEndOfWarEvent(EndOfWarEvent endOfWarEvent)
    {
        this.endOfWarEvent = endOfWarEvent;
    }

    public SquadronMoveEvent getSquadronMoveEvent()
    {
        return squadronMoveEvent;
    }

    public void setSquadronMoveEvent(SquadronMoveEvent squadronMoveEvent)
    {
        this.squadronMoveEvent = squadronMoveEvent;
    }
}
