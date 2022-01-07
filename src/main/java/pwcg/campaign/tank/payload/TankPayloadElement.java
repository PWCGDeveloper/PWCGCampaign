package pwcg.campaign.tank.payload;

public enum TankPayloadElement 
{
    STANDARD(TankPayloadElementCategory.WEAPON, "Standard Armament", 50);

	private String description;
    private int weight;
	private TankPayloadElementCategory category;
	 
	TankPayloadElement(TankPayloadElementCategory category, String description, int weight)
	{
        this.category = category;
        this.description = description;
		this.weight = weight;		
	}

	public String getDescription() 
	{
		return description;
	}

	public int getWeight() 
	{
		return weight;
	}

	public TankPayloadElementCategory getCategory() 
	{
		return category;
	}
}
