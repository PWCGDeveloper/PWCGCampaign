package pwcg.campaign.plane;

public enum PlaneAttributeMapping
{
    BF109_E7("bf109e7", "static_bf109e7", "static_bf109e7_open", "static_bf109e7_net"),
    BF109_F2("bf109f2", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_F4("bf109f4", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_G2("bf109g2", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_G4("bf109g4", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_G6("bf109g6", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_G6_LATE("bf109g6late", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_G14("bf109g14", "static_bf109","static_bf109_open", "static_bf109_net"),
    BF109_K4("bf109k4", "static_bf109k4", "static_bf109_net"),
    BF110_E2("bf110e2", "static_bf110e2", "static_bf110e2_open"),
    BF110_G2("bf110g2", "static_bf110e2", "static_bf110e2_open"),
    FW190_A3("fw190a3", "static_fw190a8", "static_bf109_net"),
    FW190_A5("fw190a5", "static_fw190a8", "static_bf109_net"),
    FW190_A6("fw190a6", "static_fw190a8", "static_bf109_net"),
    FW190_A8("fw190a8", "static_fw190a8", "static_bf109_net"),
    FW190_D9("fw190d9", "static_fw190d9", "static_bf109_net"),
    ME262_A("me262a", "static_me262a", "static_bf109_net"),
        
    Ma202_SER8("mc202s8", "static_bf109_net"),

    HE111_H6("he111h6", "static_he111", "static_he111_open"),
    HE111_H16("he111h16", "static_he111", "static_he111_open"),
    HS129_B2S("hs129b2", "static_hs129b2"),
    JU88_A4("ju88a4", "static_ju88", "static_ju88_open", "static_ju88_net"),
    JU87_D3("ju87d3", "static_ju87", "static_ju87_net", "static_ju87_open"),
    JU52("ju523mg4e", "static_ju52"),

    I16_T24("i16t24", "static_i16", "static_i16_net"),
    MIG3_S24("mig3s24", "static_mig3", "static_mig3_net"),
    LAGG3_S29("lagg3s29", "static_lagg3", "static_lagg3_net", "static_lagg3_w1", "static_lagg3_w2"),
    LA5_S8("la5s8", "static_lagg3", "static_lagg3_net"),
    LA5N_S2("la5fns2", "static_lagg3", "static_lagg3_net"),
    
    YAK1_S69("yak1s69", "static_yak1_open", "static_yak1", "static_yak1_net"),
    YAK1_S127("yak1s127", "static_yak1_open", "static_yak1", "static_yak1_net"),
    YAK7B_S36("yak7bs36", "static_yak1_open", "static_yak1", "static_yak1_net"),
    YAK9_S1("yak9s1", "static_yak1_open", "static_yak1", "static_yak1_net"),
    YAK9T_S1("yak9ts1", "static_yak1_open", "static_yak1", "static_yak1_net"),
    P38_J25("p38j25", "static_p38j25"),
    P39_L1("p39l1", "static_p39l1", "static_lagg3_net"),
    P40_E1("p40e1", "static_p40e1", "static_lagg3_net"),
    P47_D22("p47d22", "static_p47d28"),
    P47_D28("p47d28", "static_p47d28"),
    P51_B5("p51b5", "static_p51d15"),
    P51_D15("p51d15", "static_p51d15"),
    HURRICANE_MKII("hurricanemkii", "static_lagg3_net"),
    SPITFIRE_MKVB("spitfiremkvb", "static_spitfiremkixe", "static_lagg3_net"),
    SPITFIRE_MKIXE("spitfiremkixe", "static_spitfiremkixe", "static_lagg3_net"),
    SPITFIRE_MKXIV("spitfiremkxiv", "static_spitfiremkixe", "static_lagg3_net"),
    TEMPEST_MKVS2("tempestmkvs2", "static_tempestmkvs2", "static_lagg3_net"),
    TYPHOON_MKIB("typhoonmkib", "static_typhoon", "static_lagg3_net"),
    
    U2_VS("u2vs", "static_u2vs"),
    IL2_M41("il2m41", "static_il2", "static_il2_net"),
    IL2_M42("il2m42", "static_il2", "static_il2_net"),
    IL2_M43("il2m43", "static_il2", "static_il2_net"),
    PE2_S35("pe2s35", "static_pe2", "static_pe2_open"),
    PE2_S87("pe2s87", "static_pe2", "static_pe2_open"),
    A20B("a20b", "static_a20b"),
    C47("c47a", "static_a20b"),
    B25("b25draf", "static_b25d");

    
	private String planeType;
	private String[] staticPlaneMatches;
	 
	PlaneAttributeMapping(String planeType, String ... staticPlaneMatches)
	{
        this.planeType = planeType;
		this.staticPlaneMatches = staticPlaneMatches;		
	}

	public String getPlaneType() 
	{
		return planeType;
	}

	public String[] getStaticPlaneMatches() 
	{
		return staticPlaneMatches;
	}
}
