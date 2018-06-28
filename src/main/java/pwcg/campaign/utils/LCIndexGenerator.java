package pwcg.campaign.utils;

public class LCIndexGenerator 
{
	private static LCIndexGenerator instance = new LCIndexGenerator();

	// 0 and 1 are always used by the mission description and 2 is the author.  Three will always be empty
	private static int index = 2;
	
	private LCIndexGenerator()
	{
		
	}
	
	public static LCIndexGenerator getInstance()
	{
		return instance;
	}
	
	public int getNextIndex()
	{
		index = index + 1;
		return index;
	}

}
