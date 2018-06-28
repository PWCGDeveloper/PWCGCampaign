package pwcg.campaign.ww2.plane;

import pwcg.campaign.plane.IPlaneAttributeMapping;

public enum BosPlaneAttributeMapping implements IPlaneAttributeMapping
{
    BF109_E7("bf109e7", "static_bf109e7"),
    BF109_F2("bf109f2", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_F4("bf109f4", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_G2("bf109g2", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_G4("bf109g4", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_G6("bf109g6", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF110_E2("bf110e2", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF110_G2("bf110g2", "static_bf109","static_bf109_open", "static_bf109_net"),
    FW190_A3("fw190a3", "static_bf109","static_bf109_open", "static_bf109_net"),
    FW190_A5("fw190a5", "static_bf109","static_bf109_open", "static_bf109_net"),
    Ma202_SER8("mc202s8", "static_bf109_net"),

    HE111_H6("he111h6", "static_he111"),
    HE111_H16("he111h16", "static_he111"),
    HS129_B2S("hs129b2", "static_bf110e2_open"),
    JU88_A4("ju88a4", "static_ju88_open", "static_ju87_net"),
    JU87_D3("ju87d3", "static_ju87_net"),
    JU52("ju523mg4e", "static_ju88_open"),

    I16_T24("i16t24", "static_i16"),
    MIG3_S24("mig3s24", "static_mig3_net"),
    LAGG3_S29("lagg3s29", "static_lagg3", "static_mig3_net"),
    LA5_S8("la5s8", "static_lagg3", "static_mig3_net"),
    LA5N_S2("la5fns2", "static_lagg3", "static_mig3_net"),
    
    YAK1_S69("yak1s69", "static_yak1_open", "static_mig3_net"),
    YAK1_S127("yak1s127", "static_yak1_open", "static_mig3_net"),
    YAK7B_S36("yak7bs36", "static_yak1_open", "static_mig3_net"),
    P39L1("p39l1", "static_yak1_open","static_mig3_net", "static_lagg3"),
    P40_E1("p40e1", "static_mig3_net"),
    SPITFIRE_MKVB("spitfiremkvb", "static_mig3_net"),

    IL2_M41("il2m41", "static_il2", "static_il2_net"),
    IL2_M42("il2m42", "static_il2", "static_il2_net"),
    IL2_M43("il2m43", "static_il2", "static_il2_net"),
    PE2_S35("pe2s35", "static_pe2", "static_pe2_open"),
    PE2_S87("pe2s87", "static_pe2", "static_pe2_open"),
    A20B("a20b", "static_pe2", "static_pe2_open");
    // A20

    
	private String planeType;
	private String[] staticPlaneMatches;
	 
	BosPlaneAttributeMapping(String planeType, String ... staticPlaneMatches)
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
