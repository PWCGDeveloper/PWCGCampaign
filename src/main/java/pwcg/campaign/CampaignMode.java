package pwcg.campaign;

public enum CampaignMode
{
    CAMPAIGN_MODE_NONE("No Campaign Mode Selected"),
    CAMPAIGN_MODE_SINGLE("Single Player"),
    CAMPAIGN_MODE_COOP("Cooperative");
    
    String campaignModeName;
    
    CampaignMode(String campaignModeName)
    {
        this.campaignModeName = campaignModeName;
    }

    public String getCampaignModeName()
    {
        return campaignModeName;
    }
}
