package pwcg.campaign;

import java.util.Date;

import pwcg.campaign.crewmember.SerialNumber;

public class CampaignData
{
	private Date date = null;
	private String name = "";
    private boolean isCoop = false;
    private CampaignMode campaignMode = CampaignMode.CAMPAIGN_MODE_NONE;
    private SerialNumber serialNumber = new SerialNumber();
    private int referencePlayerSerialNumber = 0;

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

    public SerialNumber getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(SerialNumber serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public CampaignMode getCampaignMode()
    {
        return campaignMode;
    }

    public void setCampaignMode(CampaignMode campaignMode)
    {
        this.campaignMode = campaignMode;
    }

    public boolean isCoop()
    {
        return isCoop;
    }

    public int getReferencePlayerSerialNumber()
    {
        return referencePlayerSerialNumber;
    }

    public void setReferencePlayerSerialNumber(int referencePlayerSerialNumber)
    {
        this.referencePlayerSerialNumber = referencePlayerSerialNumber;
    }
}
