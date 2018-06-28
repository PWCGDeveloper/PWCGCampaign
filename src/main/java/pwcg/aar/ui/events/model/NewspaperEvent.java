package pwcg.aar.ui.events.model;

public class NewspaperEvent  extends AAREvent
{
	private String newspaperFile = "";

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
}
