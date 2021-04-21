package pwcg.testutils;

import pwcg.campaign.CampaignMode;

public enum SquadronTestProfile
{
    REGIMENT_503_PROFILE("Reg503", 10121503, "19420801", CampaignMode.CAMPAIGN_MODE_SINGLE),
    JG_51_PROFILE_MOSCOW("JG51_Early", 20111051, "19411101", CampaignMode.CAMPAIGN_MODE_SINGLE),
    JG_51_PROFILE_STALINGRAD("JG51_Stalingrad",20111051, "19420501", CampaignMode.CAMPAIGN_MODE_SINGLE),
    JG_51_PROFILE_STALINGRAD_FW190("JG51_Stalingrad FW190",20111051, "19420901", CampaignMode.CAMPAIGN_MODE_SINGLE),
    JG_52_PROFILE_STALINGRAD("JG52_Stalingrad",20112052, "19420801", CampaignMode.CAMPAIGN_MODE_SINGLE),
    JG_26_PROFILE_WEST("JG26_West",20111026, "19441101", CampaignMode.CAMPAIGN_MODE_SINGLE),
    KG53__STALINGRAD_PROFILE("K53 Stalingrad", 20131053, "19420801", CampaignMode.CAMPAIGN_MODE_SINGLE),
    KG53_PROFILE("K53", 20131053, "19430301", CampaignMode.CAMPAIGN_MODE_SINGLE),
    STG77_PROFILE("STG77", 20121077, "19420906", CampaignMode.CAMPAIGN_MODE_SINGLE),
    TG2_PROFILE("TG2", 20142002, "19420906", CampaignMode.CAMPAIGN_MODE_SINGLE),
    REGIMENT_11_PROFILE("Reg11", 10111011, "19430301", CampaignMode.CAMPAIGN_MODE_SINGLE),
    REGIMENT_321_PROFILE("Reg321", 10131321, "19430901", CampaignMode.CAMPAIGN_MODE_SINGLE),
    STG77_KUBAN_PROFILE("STG77 Kuban", 20121077, "19430901", CampaignMode.CAMPAIGN_MODE_SINGLE),
    EAST1944_PROFILE("East1944", 10131321, "19440820", CampaignMode.CAMPAIGN_MODE_SINGLE),
    EAST1945_PROFILE("East1945", 10131321, "19450301", CampaignMode.CAMPAIGN_MODE_SINGLE),
    FG_354_BODENPLATTE_PROFILE("FG354 Bodenplatte", 102362377, "19450101", CampaignMode.CAMPAIGN_MODE_SINGLE),
    RAF_326_BODENPLATTE_PROFILE("RAF326 Bodenplatte", 103083184, "19450101", CampaignMode.CAMPAIGN_MODE_SINGLE),
    FG_362_PROFILE("FG362", 102352486, "19440901", CampaignMode.CAMPAIGN_MODE_SINGLE),
    RAF_184_PROFILE("RAF184", 103083184, "19440901", CampaignMode.CAMPAIGN_MODE_SINGLE),

    JASTA_11_PROFILE("Jasta11", 401011, "19171001", CampaignMode.CAMPAIGN_MODE_SINGLE),
    JASTA_16_PROFILE("Jasta16", 401011, "19171001", CampaignMode.CAMPAIGN_MODE_SINGLE),
    ESC_103_PROFILE("Esc103", 301103, "19170801", CampaignMode.CAMPAIGN_MODE_SINGLE),
    ESC_3_PROFILE("Esc3", 301003, "19170801", CampaignMode.CAMPAIGN_MODE_SINGLE),
    RFC_2_PROFILE("RFC2", 302002, "19180331", CampaignMode.CAMPAIGN_MODE_SINGLE),
    RFC_46_PROFILE("RFC46", 302046, "19170801", CampaignMode.CAMPAIGN_MODE_SINGLE),

    COOP_COMPETITIVE_PROFILE("JG51_Late_Competitive",20111051, "19420501", CampaignMode.CAMPAIGN_MODE_COOP, true),
    COOP_COOPERATIVE_PROFILE("JG51_Late_Coop",20111051, "19420501", CampaignMode.CAMPAIGN_MODE_COOP, false);

   private int squadronId;
   private String dateString;
   private String key;
   private CampaignMode campaignMode;
   private boolean competitive = false;
   
   private SquadronTestProfile(String key, int squadronId, String dateString, CampaignMode campaignMode)
   {
       this.key = key;
       this.squadronId = squadronId;
       this.dateString = dateString;
       this.campaignMode = campaignMode;
   }
   
   private SquadronTestProfile(String key, int squadronId, String dateString, CampaignMode campaignMode, boolean iscompetitive)
   {
       this.key = key;
       this.squadronId = squadronId;
       this.dateString = dateString;
       this.campaignMode = campaignMode;
       this.competitive = iscompetitive;
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

	public CampaignMode getCampaignMode() 
	{
		return campaignMode;
	}

    public boolean isCompetitive()
    {
        return competitive;
    }
}
