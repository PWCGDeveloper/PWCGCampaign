package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.api.Side;

public class NewspaperEvent  extends AAREvent
{
	private String newspaperFile = "";
	private Side side;

    public NewspaperEvent(String newspaperFile, Side side, Date date, boolean isNewsWorthy)
	{
        super(date, isNewsWorthy);
        this.newspaperFile = newspaperFile;
        this.side = side;
	}

	public String getNewspaperFile() 
	{
		return newspaperFile;
	}

	public Side getSide() 
	{
		return side;
	}
}
