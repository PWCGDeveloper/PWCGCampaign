package pwcg.campaign.ww1.plane;

import pwcg.campaign.plane.IPlaneAttributeMapping;

public enum RoFPlaneAttributeMapping implements IPlaneAttributeMapping
{
    FOKKERE3("fokkere3", "st_fokkere3_DM1"),
    ALBATROSD2("albatrosd2", "st_albatrosd2_DM1"),
    ALBATROSD2LATE("albatrosd2late", "st_albatrosd2late_DM1"),
    ALBATROSD3("albatrosd3", "st_albatrosd3_DM1"),
    ALBATROSD5("albatrosd5","st_albatrosd5_DM1"),
    PFALZD3A("pfalzd3a", "st_pfalzd3a_DM1"),
    PFALZD12("pfalzd12", "st_pfalzd12_DM1"),
    FOKKERDR1("fokkerdr1","st_fokkerdr1_DM1"),
    FOKKERD7("fokkerd7", "st_fokkerd7_DM1"),
    FOKKERD7F("fokkerd7f", "st_fokkerd7f_DM1"),
    FOKKERD8("fokkerd8", "st_fokkerd8_DM1"),
    DFWC5("dfwc5", "st_dfwc5_DM1"),
    HALBERSTADTDII("halberstadtd2", "st_halberstadtd2_DM1"),
    HALBERSTADTCLII("halberstadtcl2", "st_halberstadtcl2_DM1"),
    HALBERSTADTCLIIAU("halberstadtcl2au", "st_halberstadtcl2au_DM1"),
    ROLANDCII("rolc2a","st_rolc2a_DM1"),

    GOTHAGV("gothag5", "st_dfwc5_DM1"),

    HBW12("brandw12"),

    NIEUPORT11("nieuport11", "st_nieuport11_DM1"),
    NIEUPORT17("nieuport17","st_nieuport17_DM1"),
    NIEUPORT17LEWIS("nieuport17brit", "st_nieuport17brit_DM1"),
    NIEUPORT17RUSSIAN("nieuport17rus", "st_nieuport17brit_DM1"),
    NIEUPORT28("nieuport28", "st_nieuport28_DM1"),
    HANRIOTHD1("hanhd1","st_nieuport17_DM1"),
    HANRIOTHD2("hanhd2","st_nieuport17_DM1"),
    SPAD7EARLY("spad7early", "st_spad7early_DM1"),
    SPAD7("spad7", "st_spad7_DM1"),
    SPAD13("spad13", "st_spad13_DM1"),
    BREGUET14("breguet14", "st_breguet14_DM1"),

    DH2("dh2", "st_dh2_DM1"),
    SE5A("se5a", "st_se5a_DM1"),
    PUP("soppup", "st_soppup_DM1"),
    TRIPE("soptriplane", "st_soptriplane_DM1"),
    DOLPHIN("sopdolphin","st_se5a_DM1"),
    CAMEL("sopcamel", "st_sopcamel_DM1"),
    BRISTOLF2BF2("bristolf2bf2", "st_bristolf2bf2_DM1"),
    BRISTOLF2BF3("bristolf2bf3", "st_bristolf2bf3_DM1"),
    HP400("hp400", "st_re8_DM1"),
    FE2B("fe2b", "st_fe2b_DM1"),
    RE8("re8", "st_re8_DM1"),
    DH4("dh4", "st_dh4_DM1"),
    SOPSTR("sopstr","st_sopstr_DM1"),
    SOPSTRB("sopstrb", "st_sopstrb_DM1"),

    FELIXSTOWEF2A("felixf2a"),

    S16("s16", "st_nieuport11_DM1"),
    S22("s22", "st_nieuport11_DM1"),

    BALLOON("Balloon");

	private String planeType;
	private String[] staticPlaneMatches;
	 
	RoFPlaneAttributeMapping(String planeType, String ... staticPlaneMatches)
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
