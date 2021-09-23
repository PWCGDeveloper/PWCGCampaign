package pwcg.product.fc.plane;

import pwcg.campaign.plane.IPlaneAttributeMapping;
import pwcg.campaign.plane.PlaneType;

public enum FCPlaneAttributeMapping implements IPlaneAttributeMapping
{
    ALBATROSD5("albatrosd5"),
    PFALZD3A("pfalzd3a"),
    PFALZD12("pfalzd12"),
    FOKKERDR1("fokkerdr1"),
    FOKKERD7("fokkerd7"),
    FOKKERD7F("fokkerd7f"),
    FOKKERD8("fokkerd8"),
    HALBERSTADTCLII("halberstadtcl2"),
    HALBERSTADTCLIIAU("halberstadtcl2au"),

    NIEUPORT28("nieuport28"),
    SPAD7EARLY("spad7early"),
    SPAD7LATE("spad7late"),
    SPAD13("spad13"),

    SE5A("se5a"),
    DOLPHIN("sopdolphin"),
    CAMEL("sopcamel"),
    DH4("aircodh4"),
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
