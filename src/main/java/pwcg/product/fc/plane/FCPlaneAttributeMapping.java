package pwcg.product.fc.plane;

import pwcg.campaign.plane.IPlaneAttributeMapping;

public enum FCPlaneAttributeMapping implements IPlaneAttributeMapping
{
     A20B("a20b", "static_a20b");

    
	private String planeType;
	private String[] staticPlaneMatches;
	 
	FCPlaneAttributeMapping(String planeType, String ... staticPlaneMatches)
	{
        this.planeType = planeType;
		this.staticPlaneMatches = staticPlaneMatches;		
	}

	@Override
	public String getPlaneType() 
	{
		return planeType;
	}

	@Override
	public String[] getStaticPlaneMatches() 
	{
		return staticPlaneMatches;
	}
}
