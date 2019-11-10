package pwcg.product.fc.plane;

import pwcg.campaign.plane.IPlaneAttributeMapping;
import pwcg.campaign.plane.PlaneType;

public enum FCPlaneAttributeMapping implements IPlaneAttributeMapping
{
    ALBATROSD5("albatrosd5"),
    PFALZD3A("pfalzd3a"),
    FOKKERDR1("fokkerdr1"),
    FOKKERD7("fokkerd7"),
    FOKKERD7F("fokkerd7f"),
    HALBERSTADTCLII("halberstadtcl2"),
    HALBERSTADTCLIIAU("halberstadtcl2au"),

    SPAD13("spad13"),

    SE5A("se5a"),
    DOLPHIN("sopdolphin"),
    CAMEL("sopcamel"),
    BRISTOLF2BF2("bristolf2bf2"),
    BRISTOLF2BF3("bristolf2bf3"), 
    BALLOON(PlaneType.BALLOON);
    
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
