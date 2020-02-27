package pwcg.aar.ui.events.model;

import java.util.Date;

public class EndOfWarEvent extends AAREvent
{
    public EndOfWarEvent(Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
    }
}
