package pwcg.coop.model;

public class CoopDisplayRecord
{
    private String username = "unknown";
    private String pilorNameAndRank = "unknown";
    private String campaignName = "unknown";
    private String squadronName = "unknown";

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPilorNameAndRank()
    {
        return pilorNameAndRank;
    }

    public void setPilorNameAndRank(String pilorNameAndRank)
    {
        this.pilorNameAndRank = pilorNameAndRank;
    }

    public String getCampaignName()
    {
        return campaignName;
    }

    public void setCampaignName(String campaignName)
    {
        this.campaignName = campaignName;
    }

    public String getSquadronName()
    {
        return squadronName;
    }

    public void setSquadronName(String squadronName)
    {
        this.squadronName = squadronName;
    }

}
