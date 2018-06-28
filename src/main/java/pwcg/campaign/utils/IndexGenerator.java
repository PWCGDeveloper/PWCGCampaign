package pwcg.campaign.utils;

public class IndexGenerator 
{
	private static IndexGenerator instance = new IndexGenerator();
    private static int index = 10;
    
	private IndexGenerator()
	{
		
	}
	
	public static IndexGenerator getInstance()
	{
		return instance;
	}
    
    public int getNextIndex()
    {
        index = index + 1;
        return index;
    }
}
