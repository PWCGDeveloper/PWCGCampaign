package pwcg.aar.ui.events.model;

import java.util.Date;

public class AAREvent 
{
	private Date date = null;
    private boolean isNewsWorthy = true;
    private int eventId = 1;
    private static int eventIdCounter = 1;

	public AAREvent()
	{
	    eventId = eventIdCounter;
	    ++eventIdCounter;
	}

	public Date getDate() 
	{
		return date;
	}

	public void setDate(Date date) 
	{
		this.date = date;
	}

    public boolean isNewsWorthy()
    {
        return this.isNewsWorthy;
    }

    public void setNewsWorthy(boolean isNewsWorthy)
    {
        this.isNewsWorthy = isNewsWorthy;
    }

    public int getEventId()
    {
        return eventId;
    }
}
