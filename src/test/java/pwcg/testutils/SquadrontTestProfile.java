package pwcg.testutils;

import pwcg.campaign.CampaignMode;

public enum SquadrontTestProfile
{
    REGIMENT_503_PROFILE("Reg503", 10121503, "19420801", CampaignMode.CAMPAIGN_MODE_SINGLE),
    JG_51_PROFILE_MOSCOW("JG51_Early", 20111051, "19411101", CampaignMode.CAMPAIGN_MODE_SINGLE),
    JG_51_PROFILE_STALINGRAD("JG51_Late",20111051, "19420501", CampaignMode.CAMPAIGN_MODE_SINGLE),
    JG_51_PROFILE_WEST("JG51_West",20111051, "19441101", CampaignMode.CAMPAIGN_MODE_SINGLE),
    KG53_PROFILE("K53", 20131053, "19430301", CampaignMode.CAMPAIGN_MODE_SINGLE),
    STG77_PROFILE("STG77", 20121077, "19420906", CampaignMode.CAMPAIGN_MODE_SINGLE),
    TG2_PROFILE("TG2", 20142002, "19420906", CampaignMode.CAMPAIGN_MODE_SINGLE),
    REGIMENT_11_PROFILE("Reg11", 10111011, "19430301", CampaignMode.CAMPAIGN_MODE_SINGLE),
    REGIMENT_321_PROFILE("Reg321", 10131321, "19430901", CampaignMode.CAMPAIGN_MODE_SINGLE),
    FG_362_PROFILE("FG362", 102362377, "19440801", CampaignMode.CAMPAIGN_MODE_SINGLE),
    RAF_184_PROFILE("RAF184", 103083184, "19440801", CampaignMode.CAMPAIGN_MODE_SINGLE),

    JASTA_11_PROFILE("Jasta11", 102002, "19170501", CampaignMode.CAMPAIGN_MODE_SINGLE),
    ESC_103_PROFILE("Esc103", 101103, "19170701", CampaignMode.CAMPAIGN_MODE_SINGLE),
    ESC_124_PROFILE("Esc124",101124, "19180218", CampaignMode.CAMPAIGN_MODE_SINGLE),
    RFC_2_PROFILE("RFC2", 102002, "19180331", CampaignMode.CAMPAIGN_MODE_SINGLE),
    ESC_2_PROFILE("Esc2", 101002, "19170801", CampaignMode.CAMPAIGN_MODE_SINGLE),
    
    COOP_COMPETITIVE_PROFILE("JG51_Late",20111051, "19420501", CampaignMode.CAMPAIGN_MODE_COMPETITIVE),
    COOP_COOPERATIVE_PROFILE("JG51_Late",20111051, "19420501", CampaignMode.CAMPAIGN_MODE_COOP);

   private int squadronId;
   private String dateString;
   private String key;
   private CampaignMode campaignMode;
    
    private SquadrontTestProfile(String key, int squadronId, String dateString, CampaignMode campaignMode)
    {
        this.key = key;
        this.squadronId = squadronId;
        this.dateString = dateString;
        this.campaignMode = campaignMode;
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
}
