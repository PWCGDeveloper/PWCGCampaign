package pwcg.product.fc.plane;

import pwcg.campaign.plane.IPlaneAttributeMapping;
import pwcg.campaign.plane.PlaneType;

public enum FCPlaneAttributeMapping implements IPlaneAttributeMapping
{
    FOKKERE3("fokkere3"),
    ALBATROSD2("albatrosd2"),
    ALBATROSD3("albatrosd3"),    
    ALBATROSD5("albatrosd5"),
    PFALZD3A("pfalzd3a"),
    PFALZD12("pfalzd12"),
    FOKKERDR1("fokkerdr1"),
    FOKKERD7("fokkerd7"),
    FOKKERD7F("fokkerd7f"),
    FOKKERD8("fokkerd8"),
    SSWD4("schuckertdiv"),
    DFWCV("dfwc5"),
    GOTHAGV("gothag5"),
    HALBERSTADTD2("halberstadtd2"),
    HALBERSTADTCLII("halberstadtcl2"),
    HALBERSTADTCLIIAU("halberstadtcl2au"),
    ROLANDC2A("rolandc2a"),

    NIEUPORT11("nieuport11"),
    NIEUPORT17("nieuport17"),
    NIEUPORT17GBR("nieuport17gbr"),
    NIEUPORT28("nieuport28"),
    SPAD7EARLY("spad7early"),
    SPAD7LATE("spad7late"),
    SPAD13("spad13"),
    HANRIOTHD1("hanriothd1"),
    
    DH2("aircodh2"),
    TRIPE("soptriplane"),
    PUP("soppup"),
    CAMEL("sopcamel"),
    SNIPE("sopsnipe"),
    SE5A("se5a"),
    DOLPHIN("sopdolphin"),
    STRUTTER("sopstrutter"),
    STRUTTERB("sopstrutterb"),
    FE2B("fe2b"),
    RE8("re8"),
    DH4("aircodh4"),
    BREGUET14("breguet14"),
    BRISTOLF2BF2("bristolf2bf2"),
    BRISTOLF2BF3("bristolf2bf3"), 
    HANDLEYPAGE400("handleypage400"), 
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
