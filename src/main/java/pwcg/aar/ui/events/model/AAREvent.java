package pwcg.aar.ui.events.model;

import java.util.Date;

public class AAREvent 
{
	private Date date = null;
	private boolean isNewsWorthy = true;

	public AAREvent()
	{
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
}
