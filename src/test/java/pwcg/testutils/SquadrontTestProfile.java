package pwcg.testutils;

public enum SquadrontTestProfile
{
    REGIMENT_503_PROFILE("Reg503", 10121503, "19420801"),
    JG_51_PROFILE_MOSCOW("JG51_Early", 20111051, "19411101"),
    JG_51_PROFILE_STALINGRAD("JG51_Late",20111051, "19420501"),
    JG_51_PROFILE_WEST("JG51_West",20111051, "19441101"),
    KG53_PROFILE("K53", 20131053, "19430301"),
    STG77_PROFILE("STG77", 20121077, "19420906"),
    TG2_PROFILE("TG2", 20142002, "19420906"),
    JASTA_11_PROFILE("Jasta11", 102002, "19170501"),
    ESC_103_PROFILE("Esc103", 101103, "19170701"),
    ESC_124_PROFILE("Esc124",101124, "19180218"),
    RFC_2_PROFILE("RFC2", 102002, "19180331"),
    ESC_2_PROFILE("Esc2", 101002, "19170801"),
    RAF_184_PROFILE("RAF184", 103083184, "19440801"),
    REGIMENT_11_PROFILE("Reg11", 10111011, "19430301"),
	FG_362_PROFILE("FG362", 102362377, "19440801");

   private int squadronId;
   private String dateString;
   private String key;
    
    private SquadrontTestProfile(String key, int squadronId, String dateString)
    {
        this.key = key;
        this.squadronId = squadronId;
        this.dateString = dateString;
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
}
