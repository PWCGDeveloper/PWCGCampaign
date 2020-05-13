package pwcg.mission.mcu;

public class McuEvent 
{
    static public int ONPILOTWOUNDED = 1;
    static public int ONPLANECRASHED = 2;    
    static public int ONPLANECRITICALDAMAGED = 3;    
    static public int ONPLANEBINGOFUEL = 7;        
    static public int ONPLANEBINGOAMMO = 8;        
    static public int ONPLANEDAMAGED = 12;    
    static public int ONKILLED = 13;

    private int type = 14;
    private int tarId = 0;

    public int getType() 
    {
		return type;
	}
	public void setType(int type) 
	{
		this.type = type;
	}
	public int getTarId() 
	{
		return tarId;
	}
	public void setTarId(int tarId) 
	{
		this.tarId = tarId;
	}
}
