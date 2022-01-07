package pwcg.campaign.tank;

public enum TankAttributeMapping
{
    PZKW_III_L("_pziii-l"),
    PZKW_III_M("_pziii-m"),
    PZKW_IV_G("_pziv-g"),
    PANTHER_D("_pzv-d"),
    TIGER_I("_pzvi-h1"),
    SDKFZ_10_AAA("_sdkfz10"),
    ELEFANT("_sdkfz184"),
    T34_EARLY("_t34-76stz"),
    T34_LATE("_t34-76uvz-43"),
    KV1_S("_kv1s"),
    SU122("_su122"),
    SU152("_su152"),
    GAZ_AAA("_gaz-mm-72k"),
    SHERMAN_M4A2("_m4a2");
    
	private String tankType;
	 
    TankAttributeMapping(String tankType)
	{
        this.tankType = tankType;
	}

	public String getTankType() 
	{
		return tankType;
	}
}
