package pwcg.coop.model;

public class CoopPersona
{
    private String coopUser;
    private String campaignName;
    private int serialNumber;

    public String getCoopUsername()
    {
        return coopUser;
    }

    public void setCoopUsername(String coopUser)
    {
        this.coopUser = coopUser;
    }

    public String getCampaignName()
    {
        return campaignName;
    }

    public void setCampaignName(String campaignName)
    {
        this.campaignName = campaignName;
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }
}
