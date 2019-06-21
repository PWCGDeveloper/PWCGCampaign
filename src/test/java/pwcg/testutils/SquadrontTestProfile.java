package pwcg.testutils;

public enum SquadrontTestProfile
{
    REGIMENT_503_PROFILE("Reg503", 10121503, "19420801", false),
    JG_51_PROFILE_MOSCOW("JG51_Early", 20111051, "19411101", false),
    JG_51_PROFILE_STALINGRAD("JG51_Late",20111051, "19420501", false),
    JG_51_PROFILE_WEST("JG51_West",20111051, "19441101", false),
    KG53_PROFILE("K53", 20131053, "19430301", false),
    STG77_PROFILE("STG77", 20121077, "19420906", false),
    TG2_PROFILE("TG2", 20142002, "19420906", false),
    JASTA_11_PROFILE("Jasta11", 102002, "19170501", false),
    ESC_103_PROFILE("Esc103", 101103, "19170701", false),
    ESC_124_PROFILE("Esc124",101124, "19180218", false),
    RFC_2_PROFILE("RFC2", 102002, "19180331", false),
    ESC_2_PROFILE("Esc2", 101002, "19170801", false),
    RAF_184_PROFILE("RAF184", 103083184, "19440801", false),
    REGIMENT_11_PROFILE("Reg11", 10111011, "19430301", false),
	FG_362_PROFILE("FG362", 102362377, "19440801", false),
	COOP_PROFILE("JG51_Late",20111051, "19420501", true);

   private int squadronId;
   private String dateString;
   private String key;
   private boolean isCoop;
    
    private SquadrontTestProfile(String key, int squadronId, String dateString, boolean isCoop)
    {
        this.key = key;
        this.squadronId = squadronId;
        this.dateString = dateString;
        this.isCoop = isCoop;
    }

    public String getKey()
    {
        return key;
    }

    public int getSquadronId()
    {
        return squadronId;
    }

    public String getDateString()
    {
        return dateString;
    }

	public boolean isCoop() 
	{
		return isCoop;
	}
}
