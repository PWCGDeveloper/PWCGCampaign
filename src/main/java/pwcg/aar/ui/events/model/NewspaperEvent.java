package pwcg.aar.ui.events.model;

import pwcg.campaign.api.Side;

public class NewspaperEvent  extends AAREvent
{
	private String newspaperFile = "";
	private Side side;

	public NewspaperEvent ()
	{
	}

	public String getNewspaperFile() 
	{
		return newspaperFile;
	}

	public void setNewspaperFile(String newspaperFile) 
	{
		this.newspaperFile = newspaperFile;
	}

	public Side getSide() 
	{
		return side;
	}

	public void setSide(Side side)
	{
		this.side = side;
	}
}
