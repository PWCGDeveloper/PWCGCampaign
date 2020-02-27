package pwcg.aar.ui.events.model;

import java.util.Date;

public class AAREvent 
{
	private Date date = null;
    private boolean isNewsWorthy = true;
    private int eventId = 1;
    private static int eventIdCounter = 1;

	public AAREvent(Date date, boolean isNewsWorthy)
	{
        this.date = date;
        this.isNewsWorthy = isNewsWorthy;

        eventId = eventIdCounter;
	    ++eventIdCounter;
	}

	public Date getDate() 
	{
		return date;
	}

    public boolean isNewsWorthy()
    {
        return this.isNewsWorthy;
    }

    public int getEventId()
    {
        return eventId;
    }
}
