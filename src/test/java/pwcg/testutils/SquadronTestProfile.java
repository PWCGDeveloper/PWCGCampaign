package pwcg.testutils;

import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.FrontMapIdentifier;

public enum SquadronTestProfile
{
    GROSS_DEUTSCHLAND_PROFILE("Gross Deutschland", 201001001, "19420801", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.STALINGRAD_MAP, false),

    COOP_COMPETITIVE_PROFILE("Gross Deutschland",201001001, "19420801", CampaignMode.CAMPAIGN_MODE_COOP, FrontMapIdentifier.STALINGRAD_MAP, true),
    COOP_COOPERATIVE_PROFILE("Gross Deutschland",201001001, "19420801", CampaignMode.CAMPAIGN_MODE_COOP, FrontMapIdentifier.STALINGRAD_MAP, false);

   private int squadronId;
   private String dateString;
   private String key;
   private CampaignMode campaignMode;
   private boolean competitive = false;
   private FrontMapIdentifier mapidentifier = FrontMapIdentifier.NO_MAP;
    
   private SquadronTestProfile(String key, int squadronId, String dateString, CampaignMode campaignMode, FrontMapIdentifier mapidentifier, boolean iscompetitive)
   {
       this.key = key;
       this.squadronId = squadronId;
       this.dateString = dateString;
       this.campaignMode = campaignMode;
       this.competitive = iscompetitive;
       this.mapidentifier = mapidentifier;
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

    FrontMapIdentifier getMapIdentifier()
    {
        return mapidentifier;
    }
}
